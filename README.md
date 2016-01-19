##LoopingViewpager
   默认自动循环滑动，也可以手动滑动控制
##Usage
```
  <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="200dp">

        <com.queen.views.AutoScrollViewPager
            android:id="@+id/scroll_pager"
            android:layout_width="match_parent"
            android:layout_height="240dp"/>

        <com.queen.views.ViewGroupIndicator
            android:id="@+id/scroll_pager_indicator"
            android:layout_width="wrap_content"
            android:layout_height="30dp"
            android:layout_alignParentBottom="true"
            indicator:selected_color="#ffffff"
            indicator:unselected_color="#ffffff"
            indicator:distance_from_two_indicator="10dp"
            indicator:radius="10dp"/>

    </RelativeLayout>
```

```
ids = new int[]{R.drawable.image1,
                R.drawable.image2,
                R.drawable.image3,
                R.drawable.image4,
                R.drawable.image5};

        pager = (AutoScrollViewPager) findViewById(R.id.scroll_pager);
        indicator = (ViewGroupIndicator) findViewById(R.id.scroll_pager_indicator);

        pager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return ids.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView iv = new ImageView(container.getContext());
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                iv.setImageDrawable(getResources().getDrawable(ids[position]));
                container.addView(iv);
                return iv;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });

        indicator.setParent(pager);
```

##效果图
![LoopingViewpager](http://7xq7wz.com1.z0.glb.clouddn.com/loopingViewpager.gif)
