/**
 * ***Copyright(c) : 2014-Present, VenomVendor.***
 * Author : VenomVendor
 * Dated : 17 Feb, 2014 3:29:01 AM
 * Project : LenDen-Android
 * Client : LenDen
 * Contact : info@VenomVendor.com
 * URL : https://www.google.co.in/search?q=VenomVendor
 * Copyright(c) : 2014-Present, VenomVendor.
 * License : This work is licensed under Attribution-NonCommercial 3.0 Unported
 * (CC BY-NC 3.0).
 * License info at http://creativecommons.org/licenses/by-nc/3.0/deed.en_US
 * Read More at http://creativecommons.org/licenses/by-nc/3.0/legalcode
 **/

package vee.HexWhale.LenDen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;

import com.actionbarsherlock.internal.nineoldandroids.animation.Animator;
import com.actionbarsherlock.internal.nineoldandroids.animation.Animator.AnimatorListener;
import com.actionbarsherlock.internal.nineoldandroids.animation.ObjectAnimator;

import vee.HexWhale.LenDen.aUI.Pagers.WalkThroughPager;
import vee.HexWhale.LenDen.viewpagerindicator.CirclePageIndicator;
import vee.HexWhale.LenDen.viewpagerindicator.PageIndicator;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WalkThrough extends Activity {

    private ViewPager pager;
    PageIndicator mIndicator;
    RelativeLayout mRL;
    private ScheduledExecutorService scheduleTaskExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.walkthrough);
        this.scheduleTaskExecutor = Executors.newScheduledThreadPool(1);
        this.pager = (ViewPager) this.findViewById(R.id.pager);
        this.mRL = (RelativeLayout) this.findViewById(R.id.wlk_thrg_rel_lyt);

        this.InitilizePager(); // XXX 1
    }

    private void InitilizePager() {
        final WalkThroughPager adapter = new WalkThroughPager(this);
        this.pager.setAdapter(adapter);
        final CirclePageIndicator indicator = (CirclePageIndicator) this.findViewById(R.id.indicator);
        this.mIndicator = indicator;
        indicator.setViewPager(this.pager);
        indicator.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(final int position) {
                final ObjectAnimator asdf = new ObjectAnimator();

                asdf.setPropertyName("alpha");
                asdf.setDuration(100);
                asdf.setTarget(WalkThrough.this.mRL);
                asdf.setFloatValues(1.0f, .7f);
                asdf.start();
                asdf.setRepeatCount(0);
                asdf.addListener(new AnimatorListener() {

                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                        final ObjectAnimator xx = new ObjectAnimator();

                        xx.setPropertyName("alpha");
                        xx.setDuration(100);
                        xx.setTarget(WalkThrough.this.mRL);
                        xx.setFloatValues(.8f, 1f);
                        xx.start();
                        xx.setRepeatCount(0);
                        if (position % 2 == 1) {
                            WalkThrough.this.mRL.setBackgroundResource(R.drawable.bg);
                        }
                        else {
                            WalkThrough.this.mRL.setBackgroundResource(R.drawable.walkthrough_bg);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                        final ObjectAnimator xx = new ObjectAnimator();

                        xx.setPropertyName("alpha");
                        xx.setDuration(100);
                        xx.setTarget(WalkThrough.this.mRL);
                        xx.setFloatValues(.8f, 1f);
                        xx.start();
                        xx.setRepeatCount(0);
                        if (position % 2 == 1) {
                            WalkThrough.this.mRL.setBackgroundResource(R.drawable.bg);
                        }
                        else {
                            WalkThrough.this.mRL.setBackgroundResource(R.drawable.walkthrough_bg);
                        }
                    }
                });

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        this.scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                WalkThrough.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int next = WalkThrough.this.pager.getCurrentItem() + 1;
                        next = (next % WalkThrough.this.pager.getAdapter().getCount());
                        WalkThrough.this.pager.setCurrentItem(next, true);
                    }
                });
            }
        }, 3000, 3000, TimeUnit.MILLISECONDS);

        mRL.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                shutDown();
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        this.shutDown();
        this.finish();
    }

    public void Signup(View v) {
        this.shutDown();
        this.startActivity(new Intent(this.getApplicationContext(), SignUp.class));
        this.AnimNext();
    }

    public void Login(View v) {
        this.shutDown();
        this.startActivity(new Intent(this.getApplicationContext(), Login.class));
        this.finish();
        this.AnimNext();
    }

    @Override
    protected void onDestroy() {
        this.shutDown();
        super.onDestroy();
    }

    private void shutDown() {
        this.scheduleTaskExecutor.shutdown();
    }

    private void AnimNext() {
        this.overridePendingTransition(R.anim.enter, R.anim.exit);
        return;
    }

}
