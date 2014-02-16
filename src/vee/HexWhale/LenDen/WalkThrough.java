
package vee.HexWhale.LenDen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.RelativeLayout;

import com.actionbarsherlock.internal.nineoldandroids.animation.Animator;
import com.actionbarsherlock.internal.nineoldandroids.animation.Animator.AnimatorListener;
import com.actionbarsherlock.internal.nineoldandroids.animation.ObjectAnimator;

import vee.HexWhale.LenDen.aUI.WalkThroughPager;
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
        setContentView(R.layout.walkthrough);
        scheduleTaskExecutor = Executors.newScheduledThreadPool(1);
        pager = (ViewPager) findViewById(R.id.pager);
        mRL = (RelativeLayout) findViewById(R.id.wlk_thrg_rel_lyt);
        InitilizePager(); // XXX 1
    }

    private void InitilizePager() {
        final WalkThroughPager adapter = new WalkThroughPager(this);
        pager.setAdapter(adapter);
        final CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mIndicator = indicator;
        indicator.setViewPager(pager);
        indicator.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(final int position) {
                final ObjectAnimator asdf = new ObjectAnimator();

                asdf.setPropertyName("alpha");
                asdf.setDuration(100);
                asdf.setTarget(mRL);
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
                        xx.setTarget(mRL);
                        xx.setFloatValues(.8f, 1f);
                        xx.start();
                        xx.setRepeatCount(0);
                        if (position % 2 == 1) {
                            mRL.setBackgroundResource(R.drawable.bg);
                        } else {
                            mRL.setBackgroundResource(R.drawable.walkthrough_bg);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                        final ObjectAnimator xx = new ObjectAnimator();

                        xx.setPropertyName("alpha");
                        xx.setDuration(100);
                        xx.setTarget(mRL);
                        xx.setFloatValues(.8f, 1f);
                        xx.start();
                        xx.setRepeatCount(0);
                        if (position % 2 == 1) {
                            mRL.setBackgroundResource(R.drawable.bg);
                        } else {
                            mRL.setBackgroundResource(R.drawable.walkthrough_bg);
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

        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int next = pager.getCurrentItem() + 1;
                        next = (next % pager.getAdapter().getCount());
                        pager.setCurrentItem(next, true);
                    }
                });
            }
        }, 3000, 3000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onBackPressed() {
        shutDown();
        this.finish();
    }

    public void Signup(View v) {
        shutDown();
        startActivity(new Intent(getApplicationContext(), SignUp.class));
        AnimNext();
    }

    public void Login(View v) {
        shutDown();
        startActivity(new Intent(getApplicationContext(), Login.class));
        AnimNext();
    }

    @Override
    protected void onDestroy() {
        shutDown();
        super.onDestroy();
    }

    private void shutDown() {
        scheduleTaskExecutor.shutdown();
    }

    private void AnimNext() {
        overridePendingTransition(R.anim.enter, R.anim.exit);
        return;
    }

}
