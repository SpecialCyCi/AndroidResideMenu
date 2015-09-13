#AndroidResideMenu
------
### 中文说明请点击 [这里][1]

The idea of ResideMenu is from Dribble [1][2] and [2][3]. It has come true and run in iOS devices. [iOS ResideMenu][4]
This project is the RefsideMenu Android version. The visual effect is partly referred to iOS version of ResideMenu.
And thanks to the authors for the above idea and contribution.
<img src="https://github.com/SpecialCyCi/AndroidResideMenu/raw/master/1.png" width="320" height="568" />
<img src="https://github.com/SpecialCyCi/AndroidResideMenu/raw/master/2.gif" width="320" height="568" />

Now with 3D support !
<img src="https://github.com/SpecialCyCi/AndroidResideMenu/raw/master/3Dsupport.png" width="320" height="568" />
## DEMO
This copy is the demo.

## Version Migration

#### Upgrading to `v1.4` from `v1.3`, `v1.2`, `v1.1`, `v1.0` 

Duplicate the followed code in dispatchTouchEvent() of Activity, replace the old `dispatchTouchEvent()` code.

```java
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }
```

## Requirements

Run in Android 2.3 +

## Installation

### Gradle

```gradle
repositories {
    mavenCentral()
}
dependencies {
    compile 'com.specyci:residemenu:1.6+'
}
```

### Other

 1. import ResideMenu project to your workspace.
 2. make it as a dependency library project to your main project.
<br>**( see [example][5] )**

**or**

If you want to merge ResideMenu with your project, you should follow these steps.

 1. Copy all files from src/com/special/ResideMenu to your project.
 2. Copy libs/nineoldandroids-library-2.4.0.jar to your project’s corresponding path: libs/
 3. Copy res/drawable-hdpi/shadow.9.png to your project’s corresponding path: res/drawable-hdpi/
 4. Copy res/layout/residemenu.xml and residemenu_item.xml to your project’s corresponding path: res/layout

## Usage
init ResideMenu: write these code in Activity onCreate()
```java
        // attach to current activity;
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.menu_background);
        resideMenu.attachToActivity(this);

        // create menu items;
        String titles[] = { "Home", "Profile", "Calendar", "Settings" };
        int icon[] = { R.drawable.icon_home, R.drawable.icon_profile, R.drawable.icon_calendar, R.drawable.icon_settings };

        for (int i = 0; i < titles.length; i++){
            ResideMenuItem item = new ResideMenuItem(this, icon[i], titles[i]);
            item.setOnClickListener(this);
            resideMenu.addMenuItem(item,  ResideMenu.DIRECTION_LEFT); // or  ResideMenu.DIRECTION_RIGHT
        }
```
If you want to use slipping gesture to operate(lock/unlock) the menu, override this code in Acitivity dispatchTouchEvent() (please duplicate the followed code in dispatchTouchEvent() of Activity.
```java
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }
```
**On some occasions, the slipping gesture function for locking/unlocking menu, may have conflicts with your widgets, such as viewpager. By then you can add the viewpager to ignored view, please refer to next chapter – Ignored Views.**

open/close menu
```java
resideMenu.openMenu(ResideMenu.DIRECTION_LEFT); // or ResideMenu.DIRECTION_RIGHT
resideMenu.closeMenu();
```

listen in the menu state
```java
    resideMenu.setMenuListener(menuListener);
    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
            Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void closeMenu() {
            Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT).show();
        }
    };
```

disable a swipe direction
```java
  resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
```

## Custom Usage

Do your reside menu configurations, by creating an instance of ResideMenu with your custom layout's resource Ids. If you want to use default layout, just pass that variable as -1.

```java
        resideMenu = new ResideMenu(activity, R.layout.menu_left, R.layout.menu_right);
        resideMenu.setBackground(R.drawable.menu_background);
        resideMenu.attachToActivity(activity);
        resideMenu.setScaleValue(0.5f);

        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);
```

As your configuration's completed, now you can customize side menus by getting instances of them as following:

```java
        View leftMenu = resideMenu.getLeftMenuView();
        // TODO: Do whatever you need to with leftMenu
        View rightMenu = resideMenu.getRightMenuView();
        // TODO: Do whatever you need to with rightMenu
```

##Ignored Views
On some occasions, the slipping gesture function for locking/unlocking menu, may have conflicts with your widgets such as viewpager.By then you can add the viewpager to ignored view.
```java
        // add gesture operation's ignored views
        FrameLayout ignored_view = (FrameLayout) findViewById(R.id.ignored_view);
        resideMenu.addIgnoredView(ignored_view);
```
So that in ignored view’s workplace, the slipping gesture will not be allowed to operate menu.

##About me
A student from SCAU China.<br>
Email: specialcyci#gmail.com


  [1]: https://github.com/SpecialCyCi/AndroidResideMenu/blob/master/README_CN.md
  [2]: http://dribbble.com/shots/1116265-Instasave-iPhone-App
  [3]: http://dribbble.com/shots/1114754-Social-Feed-iOS7
  [4]: https://github.com/romaonthego/RESideMenu
  [5]: https://github.com/SpecialCyCi/AndroidResideMenu/blob/master/ResideMenuDemo/project.properties
