#AndroidResideMenu
------

The idea of ResideMenu is from Dribble [1] and [2]. It has come true and run in iOS devices. [iOS ResideMenu][3]
This project is the ResideMenu Android version. The visual effect is partly referred to iOS version of ResideMenu.
And thanks to the authors for the above idea and contribution.

<img src="https://github.com/SpecialCyCi/AndroidResideMenu/raw/master/2.gif" width="320" height="568" />

## Fixes
[Overlapping Soft NavigationBar to contentUI][4]

## [Default Usage][5]

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

  [1]: http://dribbble.com/shots/1116265-Instasave-iPhone-App
  [2]: http://dribbble.com/shots/1114754-Social-Feed-iOS7
  [3]: https://github.com/romaonthego/RESideMenu
  [4]: https://github.com/SpecialCyCi/AndroidResideMenu/issues/68
  [5]: https://github.com/SpecialCyCi/AndroidResideMenu#usage
