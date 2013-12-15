package com.special.ResideMenu.menu;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.*;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.special.ResideMenu.R;

import java.util.ArrayList;
import java.util.List;

/**
 * User: special
 * Date: 13-12-10
 * Time: 下午10:44
 * Mail: specialcyci@gmail.com
 */
public class ResideMenu extends FrameLayout implements GestureDetector.OnGestureListener{

    private ImageView iv_shadow;
    private ImageView iv_background;
    private LinearLayout layout_menu;
    private ScrollView sv_menu;
    private AnimatorSet scaleUp_shadow;
    private AnimatorSet scaleUp_activity;
    private AnimatorSet scaleDown_activity;
    private AnimatorSet scaleDown_shadow;
    /** the activity that view attach to */
    private Activity activity;
    /** the decorview of the activity    */
    private ViewGroup view_decor;
    /** the viewgroup of the activity    */
    private ViewGroup view_activity;
    /** the flag of menu open status     */
    private boolean              isOpened;
    private GestureDetector gestureDetector;
    private float shadow_ScaleX;
    /** the view which don't want to intercept touch event */
    private List<View> ignoredViews;
    private List<ResideMenuItem> menuItems;
    private DisplayMetrics displayMetrics = new DisplayMetrics();
    private OnMenuListener menuListener;

    public ResideMenu(Context context) {
        super(context);
        initViews(context);
    }

    private void initViews(Context context){
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.residemenu, this);
        sv_menu = (ScrollView) findViewById(R.id.sv_menu);
        iv_shadow = (ImageView) findViewById(R.id.iv_shadow);
        layout_menu = (LinearLayout) findViewById(R.id.layout_menu);
        iv_background = (ImageView) findViewById(R.id.iv_background);
    }

    /**
     * use the method to set up the activity which residemenu need to show;
     *
     * @param activity
     */
    public void attachToActivity(Activity activity){
        initValue(activity);
        setShadowScaleXByOrientation();
        buildAnimationSet();
    }

    private void initValue(Activity activity){
        this.activity   = activity;
        menuItems       = new ArrayList<ResideMenuItem>();
        gestureDetector = new GestureDetector(this);
        ignoredViews    = new ArrayList<View>();
        view_decor      = (ViewGroup)activity.getWindow().getDecorView();
        view_activity   = (ViewGroup) view_decor.getChildAt(0);
    }

    private void setShadowScaleXByOrientation(){
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            shadow_ScaleX = 0.5335f;
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            shadow_ScaleX = 0.56f;
        }
    }

    /**
     * set the menu background picture;
     *
     * @param imageResrouce
     */
    public void setBackground(int imageResrouce){
        iv_background.setImageResource(imageResrouce);
    }

    /**
     * the visiblity of shadow under the activity view;
     *
     * @param isVisible
     */
    public void setShadowVisible(boolean isVisible){
        if (isVisible)
            iv_shadow.setImageResource(R.drawable.shadow);
        else
            iv_shadow.setImageBitmap(null);
    }

    /**
     * add a single items;
     *
     * @param menuItem
     */
    public void addMenuItem(ResideMenuItem menuItem){
        this.menuItems.add(menuItem);
    }

    /**
     * set the menu items by array list;
     *
     * @param menuItems
     */
    public void setMenuItems(List<ResideMenuItem> menuItems){
        this.menuItems = menuItems;
    }

    public List<ResideMenuItem> getMenuItems() {
        return menuItems;
    }

    /**
     * if you need to do something on the action of closing or opening
     * menu, set the listener here.
     *
     * @return
     */
    public void setMenuListener(OnMenuListener menuListener) {
        this.menuListener = menuListener;
    }


    public OnMenuListener getMenuListener() {
        return menuListener;
    }

    /**
     * we need the call the method before the menu show, because the
     * padding of activity can't get at the moment of onCreateView();
     */
    private void setViewPadding(){
        this.setPadding(view_activity.getPaddingLeft(),
                view_activity.getPaddingTop(),
                view_activity.getPaddingRight(),
                view_activity.getPaddingBottom());
    }

    /**
     * show the reside menu;
     */
    public void openMenu(){
        if(!isOpened){
            isOpened = true;
            showOpenMenuRelative();
        }
    }

    private void removeMenuLayout(){
        ViewGroup parent = ((ViewGroup) sv_menu.getParent());
        parent.removeView(sv_menu);
    }

    /**
     * close the reslide menu;
     */
    public void closeMenu(){
        if(isOpened){
            isOpened = false;
            scaleUp_activity.start();
        }
    }

    /**
     * return the flag of menu status;
     *
     * @return
     */
    public boolean isOpened() {
        return isOpened;
    }

    /**
     *  call the method relative to open menu;
     */
    private void showOpenMenuRelative(){
        setViewPadding();
        scaleDown_activity.start();
        // remove self if has not remove
        if (getParent() != null) view_decor.removeView(this);
        if (sv_menu.getParent() != null) removeMenuLayout();
        view_decor.addView(this, 0);
        view_decor.addView(sv_menu);
    }

    private Animator.AnimatorListener animationListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            if (isOpened){
                layout_menu.removeAllViews();
                showMenuDelay();
                if (menuListener != null)
                    menuListener.openMenu();
            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            // reset the view;
            if(!isOpened){
                view_decor.removeView(ResideMenu.this);
                view_decor.removeView(sv_menu);
                if (menuListener != null)
                    menuListener.closeMenu();
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    private void showMenuDelay(){
        layout_menu.removeAllViews();
        for(int i = 0; i < menuItems.size() ; i ++)
            showMenuItem(menuItems.get(i), i);
    }

    /**
     *
     * @param menuItem
     * @param menu_index the position of the menu;
     * @return
     */
    private void showMenuItem(ResideMenuItem menuItem,int menu_index){

        layout_menu.addView(menuItem);
        ViewHelper.setAlpha(menuItem, 0);
        AnimatorSet scaleUp = new AnimatorSet();
        scaleUp.playTogether(
                ObjectAnimator.ofFloat(menuItem, "translationX", -100.f, 0.0f),
                ObjectAnimator.ofFloat(menuItem, "alpha", 0.0f, 1.0f)
        );

        scaleUp.setInterpolator(AnimationUtils.loadInterpolator(activity,
                android.R.anim.anticipate_overshoot_interpolator));
        // with animation;
        scaleUp.setStartDelay(50 * menu_index);
        scaleUp.setDuration(400).start();
    }

    private void buildAnimationSet(){
        scaleUp_activity   = buildScaleUpAnimation(view_activity,1.0f,1.0f);
        scaleUp_shadow     = buildScaleUpAnimation(iv_shadow,1.0f,1.0f);
        scaleDown_activity = buildScaleDownAnimation(view_activity,0.5f,0.5f);
        scaleDown_shadow   = buildScaleDownAnimation(iv_shadow,shadow_ScaleX,0.59f);
        scaleUp_activity.addListener(animationListener);
        scaleUp_activity.playTogether(scaleUp_shadow);
        scaleDown_shadow.addListener(animationListener);
        scaleDown_activity.playTogether(scaleDown_shadow);
    }

    /**
     * a helper method to build scale down animation;
     *
     * @param target
     * @param targetScaleX
     * @param targetScaleY
     * @return
     */
    private AnimatorSet buildScaleDownAnimation(View target,float targetScaleX,float targetScaleY){

        // set the pivotX and pivotY to scale;
        int pivotX = (int) (getScreenWidth()  * 1.5);
        int pivotY = (int) (getScreenHeight() * 0.5);

        ViewHelper.setPivotX(target, pivotX);
        ViewHelper.setPivotY(target, pivotY);
        AnimatorSet scaleDown = new AnimatorSet();
        scaleDown.playTogether(
                ObjectAnimator.ofFloat(target, "scaleX", targetScaleX),
                ObjectAnimator.ofFloat(target, "scaleY", targetScaleY)
        );

        scaleDown.setInterpolator(AnimationUtils.loadInterpolator(activity,
                android.R.anim.decelerate_interpolator));
        scaleDown.setDuration(250);
        return scaleDown;
    }

    /**
     * a helper method to build scale up animation;
     *
     * @param target
     * @param targetScaleX
     * @param targetScaleY
     * @return
     */
    private AnimatorSet buildScaleUpAnimation(View target,float targetScaleX,float targetScaleY){

        AnimatorSet scaleUp = new AnimatorSet();
        scaleUp.playTogether(
                ObjectAnimator.ofFloat(target, "scaleX", targetScaleX),
                ObjectAnimator.ofFloat(target, "scaleY", targetScaleY)
        );

        scaleUp.setDuration(250);
        return scaleUp;
    }

    /**
     * if there ware some view you don't want reside menu
     * to intercept their touch event,you can use the method
     * to set.
     *
     * @param v
     */
    public void addIgnoredView(View v){
        ignoredViews.add(v);
    }

    /**
     * remove the view from ignored view list;
     * @param v
     */
    public void removeIgnoredView(View v){
        ignoredViews.remove(v);
    }

    /**
     * clear the ignored view list;
     */
    public void clearIgnoredViewList(){
        ignoredViews.clear();
    }

    /**
     * if the motion evnent was relative to the view
     * which in ignored view list,return true;
     *
     * @param ev
     * @return
     */
    private boolean isInIgnoredView(MotionEvent ev) {
        Rect rect = new Rect();
        for (View v : ignoredViews) {
            v.getGlobalVisibleRect(rect);
            if (rect.contains((int) ev.getX(), (int) ev.getY()))
                return true;
        }
        return false;
    }

    //--------------------------------------------------------------------------
    //
    //  GestureListener
    //
    //--------------------------------------------------------------------------
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        gestureDetector.onTouchEvent(ev);
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {

        if(isInIgnoredView(motionEvent) || isInIgnoredView(motionEvent2))
            return false;

        int distanceX    = (int) (motionEvent2.getX() - motionEvent.getX());
        int distanceY    = (int) (motionEvent2.getY() - motionEvent.getY());
        int screenWidth  = (int) getScreenWidth();

        if(Math.abs(distanceY) > screenWidth * 0.3)
            return false;

        if(Math.abs(distanceX) > screenWidth * 0.3){
            if(distanceX > 0 && !isOpened ){
                // from left to right;
                openMenu();
            }else if(distanceX < 0 && isOpened){
                // from right th left;
                closeMenu();
            }
        }

        return false;
    }

    public int getScreenHeight(){
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public int getScreenWidth(){
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public interface OnMenuListener{

        /**
         * the method will call on the finished time of opening menu's animation.
         */
        public void openMenu();

        /**
         * the method will call on the finished time of closing menu's animation  .
         */
        public void closeMenu();
    }

}
