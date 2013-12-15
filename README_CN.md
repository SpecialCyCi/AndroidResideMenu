#AndroidResideMenu

------

ReisdeMenu 创意灵感来自于Dribbble[1][1]还有[2][2]，而这个是Android版的ResideMenu，在视觉效果上部分参考了[iOS版的RESideMenu][3]，并在此感谢以上作者的贡献。
<img src="https://github.com/SpecialCyCi/AndroidResideMenu/raw/master/1.png" width="320" height="568" />
<img src="https://github.com/SpecialCyCi/AndroidResideMenu/raw/master/2.gif" width="320" height="568" />

## DEMO

本代码即是DEMO，您可以下载后选择您喜欢的IDE运行。SDK版本建议使用4.0以上

## Requirements

运行在 Android 2.3 +

## Installation

如果您需要将ResideMenu使用在您的项目中，您需要完成以下步骤

### 1. 复制src/com/special/ResideMenu下的所有代码到您的项目相应位置
### 2. 复制libs/nineoldandroids-library-2.4.0.jar到您项目libs/下
### 3. 复制res/drawable-hdpi/shadow.9.png到您的项目相应位置

## Usage
写在Activity onCreate()中
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
如果您需要使用手势滑动开启/关闭菜单，请复写activity的dispatchTouchEvent()，代码如下
```java
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.onInterceptTouchEvent(ev) || super.dispatchTouchEvent(ev);
    }
```
**在某些场景下，手势滑动开启/关闭菜单可能与您的某些控件产生冲突，例如viewpager，这时您可以把viewpager添加到ignored view.请参见下节Ignored Views**

开启/关闭菜单
open or close menu
```java
resideMenu.openMenu();
resideMenu.closeMenu();
```
监听菜单状态
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
在某些场景下，手势滑动开启/关闭菜单可能与您的某些控件产生冲突，例如viewpager，这时您可以把viewpager添加到ignored view.
```java
        // add gesture operation's ignored views
        FrameLayout ignored_view = (FrameLayout) findViewById(R.id.ignored_view);
        resideMenu.addIgnoredView(ignored_view);
```
这样子在ignored_view操作的区域就不允许用手势滑动操作菜单.

##About me
A student from SCAU China.<br>
Email: specialcyci#gmail.com

[1]: http://dribbble.com/shots/1116265-Instasave-iPhone-App
[2]: http://dribbble.com/shots/1114754-Social-Feed-iOS7
[3]: https://github.com/romaonthego/RESideMenu
[4]: http://dribbble.com/shots/1116265-Instasave-iPhone-App
[5]: http://dribbble.com/shots/1114754-Social-Feed-iOS7