#AndroidResideMenu
------
### 中文说明请点击 [这里][1]

The idea of ResideMenu is from Dribble [1][2] and [2][3]. It has come true and run in iOS devices. [iOS ResideMenu][4]
This project is the RefsideMenu Android version. The visual effect is partly referred to iOS version of ResideMenu.
And thanks to the authors for the above idea and contribution.
<img src="https://github.com/SpecialCyCi/AndroidResideMenu/raw/master/1.png" width="320" height="568" />
<img src="https://github.com/SpecialCyCi/AndroidResideMenu/raw/master/2.gif" width="320" height="568" />

## DEMO
This copy is the demo.

## Requirements

Run in Android 2.3 +

## Installation

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
            resideMenu.addMenuItem(item);
        }
```
If you want to use slipping gesture to operate(lock/unlock) the menu, override this code in Acitivity dispatchTouchEvent() (please duplicate the followed code in dispatchTouchEvent() of Activity.
```java
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.onInterceptTouchEvent(ev) || super.dispatchTouchEvent(ev);
    }
```
**On some occasions, the slipping gesture function for locking/unlocking menu, may have conflicts with your widgets, such as viewpager. By then you can add the viewpager to ignored view, please refer to next chapter – Ignored Views.**

open/close menu
```java
resideMenu.openMenu();
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