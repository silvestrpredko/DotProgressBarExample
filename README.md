**DotProgressBar**
===================

### It`s a simple progress bar.

## **Example** ##

![enter image description here](https://lh3.googleusercontent.com/3E5v7TkH0DxXcdBI2pjJa9AVaogX2Kknh700wqt9wx8=s0 "DotProgressBar.gif")

[![enter image description here](http://style.anu.edu.au/_anu/images/icons/icon-google-play-small.png)](https://play.google.com/store/apps/details?id=com.silvestr.dotprogressbarexample&hl=en)

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-DotProgressBar-green.svg?style=true)](https://android-arsenal.com/details/1/3340)

### **Gradle**
```groovy
compile 'com.github.silvestrpredko:dot-progress-bar:1.1'
```    

## **Usage** ##
#### **XML**
```xml
<com.github.silvestrpredko.dotprogressbar.DotProgressBar
  android:id="@+id/dot_progress_bar"
  android:layout_width="match_parent"
  android:layout_height="50dp"
  custom:amount="5"
  custom:duration="@android:integer/config_mediumAnimTime"
  custom:endColor="@color/light_blue_A400"
  custom:startColor="@color/light_blue_A700"
  custom:animationDirection="left"/>
```
#### **Code**
```java
dotProgressBar.setStartColor(startColor);
dotProgressBar.setEndColor(endColor);
dotProgressBar.setDotAmount(amount);
dotProgressBar.setAnimationTime(time);

// or you can use builder

new DotProgressBarBuilder(this)
                .setDotAmount(5)
                .setStartColor(Color.BLACK)
                .setAnimationDirection(DotProgressBar.LEFT_DIRECTION)
                .build();
```

### [License](./LICENSE.md)
