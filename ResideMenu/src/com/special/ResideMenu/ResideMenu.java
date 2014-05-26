package com.special.ResideMenu;

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

import java.util.ArrayList;
import java.util.List;

/**
 * User: special
 * Date: 13-12-10
 * Time: 下午10:44
 * Mail: specialcyci@gmail.com
 */
public class ResideMenu extends FrameLayout implements GestureDetector.OnGestureListener{

    public static final int DIRECTION_LEFT  = 0;
    public static final int DIRECTION_RIGHT = 1;

    private ImageView iv_shadow;
    private ImageView iv_background;
    private LinearLayout layout_left_menu;
    private LinearLayout layout_right_menu;
    private ScrollView sv_left_menu;
    private ScrollView sv_right_menu;
    private ScrollView sv_menu;
    /** the activity that view attach to */
    private Activity activity;
    /** the decorview of the activity    */
    private ViewGroup view_decor;
    /** the viewgroup of the activity    */
    private TouchDisableView view_activity;
    /** the flag of menu open status     */
    private boolean              isOpened;
    private GestureDetector gestureDetector;
    private float shadow_AdjustScaleX;
    private float shadow_AdjustScaleY;
    /** the view which don't want to intercept touch event */
    private List<View> ignoredViews;
    private List<ResideMenuItem> leftMenuItems;
    private List<ResideMenuItem> rightMenuItems;
    private DisplayMetrics displayMetrics = new DisplayMetrics();
    private OnMenuListener menuListener;
    private float lastRawX;
    private boolean canScale = false;
    private int scaleDirection = DIRECTION_LEFT;
    private List<Integer> disabledDirection = new ArrayList<Integer>();

    public ResideMenu(Context context) {
        super(context);
        initViews(context);
    }

    private void initViews(Context context){
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.residemenu, this);
        sv_left_menu = (ScrollView) findViewById(R.id.sv_left_menu);
        sv_right_menu = (ScrollView) findViewById(R.id.sv_right_menu);
        iv_shadow = (ImageView) findViewById(R.id.iv_shadow);
        layout_left_menu = (LinearLayout) findViewById(R.id.layout_left_menu);
        layout_right_menu = (LinearLayout) findViewById(R.id.layout_right_menu);
        iv_background = (ImageView) findViewById(R.id.iv_background);
    }

    /**
     * use the method to set up the activity which residemenu need to show;
     *
     * @param activity
     */
    public void attachToActivity(Activity activity){
        initValue(activity);
        setShadowAdjustScaleXByOrientation();
        view_decor.addView(this, 0);
        setViewPadding();
    }

    private void initValue(Activity activity){
        this.activity   = activity;
        leftMenuItems   = new ArrayList<ResideMenuItem>();
        rightMenuItems  = new ArrayList<ResideMenuItem>();
        gestureDetector = new GestureDetector(this);
        ignoredViews    = new ArrayList<View>();
        view_decor      = (ViewGroup) activity.getWindow().getDecorView();
        view_activity   = new TouchDisableView(this.activity);

        View mContent   = view_decor.getChildAt(0);
        view_decor.removeViewAt(0);
        view_activity.setContent(mContent);
        view_decor.addView(view_activity, 0);
    }

    private void setShadowAdjustScaleXByOrientation(){
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            shadow_AdjustScaleX = 0.034f;
            shadow_AdjustScaleY = 0.12f;
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            shadow_AdjustScaleX = 0.06f;
            shadow_AdjustScaleY = 0.07f;
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
     * add a single items to left menu;
     *
     * @param menuItem
     */
    @Deprecated
    public void addMenuItem(ResideMenuItem menuItem){
        this.leftMenuItems.add(menuItem);
        layout_left_menu.addView(menuItem);
    }

    /**
     * add a single items;
     *
     * @param menuItem
     * @param direction
     */
    public void addMenuItem(ResideMenuItem menuItem, int direction){
        if (direction == DIRECTION_LEFT){
            this.leftMenuItems.add(menuItem);
            layout_left_menu.addView(menuItem);
        }else{
            this.rightMenuItems.add(menuItem);
            layout_right_menu.addView(menuItem);
        }
    }

    /**
     * set the menu items by array list to left menu;
     *
     * @param menuItems
     */
    @Deprecated
    public void setMenuItems(List<ResideMenuItem> menuItems){
        this.leftMenuItems = menuItems;
        rebuildMenu();
    }

    /**
     * set the menu items by array list;
     *
     * @param menuItems
     * @param direction
     */
    public void setMenuItems(List<ResideMenuItem> menuItems, int direction){
        if (direction == DIRECTION_LEFT)
            this.leftMenuItems = menuItems;
        else
            this.rightMenuItems = menuItems;
        rebuildMenu();
    }

    private void rebuildMenu(){
        layout_left_menu.removeAllViews();
        layout_right_menu.removeAllViews();
        for(int i = 0; i < leftMenuItems.size() ; i ++)
            layout_left_menu.addView(leftMenuItems.get(i), i);
        for(int i = 0; i < rightMenuItems.size() ; i ++)
            layout_right_menu.addView(rightMenuItems.get(i), i);
    }

    /**
     * get the left menu items;
     *
     * @return
     */
    @Deprecated
    public List<ResideMenuItem> getMenuItems() {
        return leftMenuItems;
    }

    /**
     * get the menu items;
     *
     * @return
     */
    public List<ResideMenuItem> getMenuItems(int direction) {
        if ( direction == DIRECTION_LEFT)
            return leftMenuItems;
        else
            return rightMenuItems;
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
    public void openMenu(int direction){
        if (isInDisableDirection(direction))
            throw new IllegalArgumentException("You have set this direction disable, but now you want to open menu in this direction.");
        setScaleDirection(direction);

        isOpened = true;
        AnimatorSet scaleDown_activity = buildScaleDownAnimation(view_activity, 0.5f, 0.5f);
        AnimatorSet scaleDown_shadow = buildScaleDownAnimation(iv_shadow, 0.5f + shadow_AdjustScaleX, 0.5f + shadow_AdjustScaleY);
        AnimatorSet alpha_menu = buildMenuAnimation(sv_menu, 1.0f);
        scaleDown_shadow.addListener(animationListener);
        scaleDown_activity.playTogether(scaleDown_shadow);
        scaleDown_activity.playTogether(alpha_menu);
        scaleDown_activity.start();
    }

    /**
     * close the reslide menu;
     */
    public void closeMenu(){

        isOpened = false;
        AnimatorSet scaleUp_activity = buildScaleUpAnimation(view_activity, 1.0f, 1.0f);
        AnimatorSet scaleUp_shadow = buildScaleUpAnimation(iv_shadow, 1.0f, 1.0f);
        AnimatorSet alpha_menu = buildMenuAnimation(sv_menu, 0.0f);
        scaleUp_activity.addListener(animationListener);
        scaleUp_activity.playTogether(scaleUp_shadow);
        scaleUp_activity.playTogether(alpha_menu);
        scaleUp_activity.start();
    }

    public void setDirectionDisable(int direction){
        disabledDirection.add(direction);
    }

    private boolean isInDisableDirection(int direction){
        return disabledDirection.contains(direction);
    }

    private void setScaleDirection(int direction){

        int screenWidth = getScreenWidth();
        float pivotX;
        float pivotY = getScreenHeight() * 0.5f;

        if (direction == DIRECTION_LEFT){
            sv_menu = sv_left_menu;
            pivotX  = screenWidth * 1.5f;
        }else{
            sv_menu = sv_right_menu;
            pivotX  = screenWidth * -0.5f;
        }

        ViewHelper.setPivotX(view_activity, pivotX);
        ViewHelper.setPivotY(view_activity, pivotY);
        ViewHelper.setPivotX(iv_shadow, pivotX);
        ViewHelper.setPivotY(iv_shadow, pivotY);
        scaleDirection = direction;
    }

    /**
     * return the flag of menu status;
     *
     * @return
     */
    public boolean isOpened() {
        return isOpened;
    }

    private OnClickListener viewActivityOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isOpened()) closeMenu();
        }
    };

    private Animator.AnimatorListener animationListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            if (isOpened){
                sv_menu.setVisibility(VISIBLE);
                if (menuListener != null)
                    menuListener.openMenu();
            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            // reset the view;
            if(isOpened){
                view_activity.setTouchDisable(true);
                view_activity.setOnClickListener(viewActivityOnClickListener);
            }else{
                view_activity.setTouchDisable(false);
                view_activity.setOnClickListener(null);
                sv_menu.setVisibility(GONE);
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

    /**
     * a helper method to build scale down animation;
     *
     * @param target
     * @param targetScaleX
     * @param targetScaleY
     * @return
     */
    private AnimatorSet buildScaleDownAnimation(View target,float targetScaleX,float targetScaleY){

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

    private AnimatorSet buildMenuAnimation(View target, float alpha){

        AnimatorSet alphaAnimation = new AnimatorSet();
        alphaAnimation.playTogether(
                ObjectAnimator.ofFloat(target, "alpha", alpha)
        );

        alphaAnimation.setDuration(250);
        return alphaAnimation;
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

    private void setScaleDirectionByRawX(float currentRawX){
        if (currentRawX < lastRawX)
            setScaleDirection(DIRECTION_RIGHT);
        else
            setScaleDirection(DIRECTION_LEFT);
    }

    private float getTargetScale(float currentRawX){
        float scaleFloatX = ((currentRawX - lastRawX) / getScreenWidth()) * 0.75f;
        scaleFloatX = scaleDirection == DIRECTION_RIGHT ? - scaleFloatX : scaleFloatX;

        float targetScale = ViewHelper.getScaleX(view_activity) - scaleFloatX;
        targetScale = targetScale > 1.0f ? 1.0f : targetScale;
        targetScale = targetScale < 0.5f ? 0.5f : targetScale;
        return targetScale;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float currentActivityScaleX = ViewHelper.getScaleX(view_activity);
        if (currentActivityScaleX == 1.0f)
            setScaleDirectionByRawX(ev.getRawX());

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                canScale = !isInIgnoredView(ev);
                break;

            case MotionEvent.ACTION_MOVE:
                if (!canScale || isInDisableDirection(scaleDirection))
                    break;
                if (currentActivityScaleX < 0.95)
                    sv_menu.setVisibility(VISIBLE);

                float targetScale = getTargetScale(ev.getRawX());
                ViewHelper.setScaleX(view_activity, targetScale);
                ViewHelper.setScaleY(view_activity, targetScale);
                ViewHelper.setScaleX(iv_shadow, targetScale + shadow_AdjustScaleX);
                ViewHelper.setScaleY(iv_shadow, targetScale + shadow_AdjustScaleY);
                ViewHelper.setAlpha(sv_menu, (1 - targetScale) * 2.0f);
                break;

            case MotionEvent.ACTION_UP:
                if (!canScale)
                    break;
                if (currentActivityScaleX > 0.75f){
                    closeMenu();
                }else{
                    openMenu(scaleDirection);
                }
                break;
        }
        lastRawX = ev.getRawX();
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
        return true;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
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
