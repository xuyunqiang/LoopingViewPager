package com.queen;

import android.app.Activity;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.queen.views.AutoScrollViewPager;
import com.queen.views.ViewGroupIndicator;

/**
 * Created by liukun on 15/2/9.
 */
public class ViewPagerActivity extends ActionBarActivity {

    private AutoScrollViewPager pager;

    private ViewGroupIndicator indicator;
    private int[] ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager_avtivity);

        getWindow().setBackgroundDrawable(null);

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

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Activity-->", "onStart");
        indicator.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //    if (isPowerOff()) {
        indicator.stop();
        //    }
        Log.i("Activity-->", "onStop");

    }

    private boolean isPowerOff() {
        PowerManager manager = (PowerManager) getSystemService(Activity.POWER_SERVICE);
        if (manager.isScreenOn()) {
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

     //   Log.e("ViewPagerActivity", "onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        Log.e("ViewPagerActivity", "onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);

//        pager.setCurrentItem(3);
    }
}
