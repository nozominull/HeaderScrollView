HeaderScrollView
==================

A ScrollView header that hides when scrolling down and reappears when scrolling up.more than [QuickReturnHeader][1].

DEPRECATED
----------------
plz use [StickyScrollViewItems][3]


screenshots
----------------
This is screenshots of sample activity.

init

![image](https://raw.github.com/xuyangbill/HeaderScrollView/master/screenshots/1.png)

up

![image](https://raw.github.com/xuyangbill/HeaderScrollView/master/screenshots/2.png)

up

![image](https://raw.github.com/xuyangbill/HeaderScrollView/master/screenshots/3.png)

up

![image](https://raw.github.com/xuyangbill/HeaderScrollView/master/screenshots/4.png)

down

![image](https://raw.github.com/xuyangbill/HeaderScrollView/master/screenshots/5.png)

down

![image](https://raw.github.com/xuyangbill/HeaderScrollView/master/screenshots/6.png)

down

![image](https://raw.github.com/xuyangbill/HeaderScrollView/master/screenshots/7.png)


How to use
-------------

*To run Sample App.*

  1. clone project.

  2. run on your android phone.

*To use HeaderScrollView.*

  1. clone project.

  2. Copy the files you need(all if u r lazy…) in com.nozomi.headerscrollview.view.

*Simple Example.*

```xml
 <RelativeLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_below="@+id/header" >

        <com.nozomi.headerscrollview.view.HeaderScrollView
            android:id="@+id/scroll"
            android:layout_width="fill_parent"
            android:layout_height="fill_parent" >
            
            <LinearLayout
                android:layout_width="fill_parent"
                android:layout_height="wrap_content"
                android:orientation="vertical" >
...

                <View
                    android:id="@+id/dummy"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content" />

...
            </LinearLayout>
        </com.nozomi.headerscrollview.view.HeaderScrollView>

        <TextView
            android:id="@+id/real"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="real"
            android:textSize="72sp" />
    </RelativeLayout>
```

```java
		View realView = findViewById(R.id.real);
		View dummyView = findViewById(R.id.dummy);

		HeaderScrollView scroll = (HeaderScrollView) findViewById(R.id.scroll);
		scroll.setHeaderAndFooter(realView, dummyView);
```
plz check code for more details.

TODO
-------------
seems the header has a little delay when scrolling.

[1]: https://github.com/ManuelPeinado/QuickReturnHeader
[2]: https://github.com/6a209/PullRefreshScrollView
[3]: https://github.com/emilsjolander/StickyScrollViewItems
