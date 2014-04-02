/**
 * ***Copyright(c)  :   2014-Present, VenomVendor.***
 * Author       :   VenomVendor
 * Dated        :   2 Apr, 2014 8:17:13 PM
 * Project      :   LenDen-Android
 * Client       :   LenDen
 * Contact      :   info@VenomVendor.com
 * URL          :   https://www.google.co.in/search?q=VenomVendor
 * Copyright(c) :   2014-Present, VenomVendor.
 * License      :   This work is licensed under Attribution-NonCommercial 3.0 Unported
 *                  License info at http://creativecommons.org/licenses/by-nc/3.0/deed.en_US
 *                  Read More at http://creativecommons.org/licenses/by-nc/3.0/legalcode
 **/

package vee.HexWhale.LenDen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.actionbarsherlock.internal.nineoldandroids.animation.Animator;
import com.actionbarsherlock.internal.nineoldandroids.animation.Animator.AnimatorListener;
import com.actionbarsherlock.internal.nineoldandroids.animation.ObjectAnimator;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

import vee.HexWhale.LenDen.Utils.Constants;
import vee.HexWhale.LenDen.Utils.Constants.API.STRING;
import vee.HexWhale.LenDen.aUI.Facebook;
import vee.HexWhale.LenDen.aUI.Facebook.FacebookListener;
import vee.HexWhale.LenDen.aUI.Pagers.WalkThroughPager;
import vee.HexWhale.LenDen.bg.Threads.TagGen;
import vee.HexWhale.LenDen.viewpagerindicator.CirclePageIndicator;
import vee.HexWhale.LenDen.viewpagerindicator.PageIndicator;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WalkThrough extends Activity {
    private String tag = "UNKNOWN";
    private ViewPager pager;
    PageIndicator mIndicator;
    RelativeLayout mRL;
    private ScheduledExecutorService scheduleTaskExecutor;
    String fbEmail, fbUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        tag = TagGen.getTag(this.getClass());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.walkthrough);
        scheduleTaskExecutor = Executors.newScheduledThreadPool(1);
        pager = (ViewPager) findViewById(R.id.pager);
        mRL = (RelativeLayout) findViewById(R.id.wlk_thrg_rel_lyt);

        InitilizePager(); // XXX 1
    }

    public void LoginSignupFB(View view)
    {
        LogR("Facebook Clicked");
        FBLogin();
    }

    protected void FBLogin() {
        final Facebook mFB = new Facebook(this, mFacebookListener);
        mFB.LogMeIn();
    }

    private final FacebookListener mFacebookListener = new FacebookListener() {

        @Override
        public void call(final Session session, final SessionState state, final Exception exception, final Context context) {

            if (session.isOpened()) {

                Request.newMeRequest(session, new Request.GraphUserCallback() {

                    @Override
                    public void onCompleted(GraphUser user, Response response) {

                        LogR("response :::::::::" + response);
                        LogR("user :::::::::" + user);

                        if (user != null && session.getAccessToken() != null) {
                            fbUserName = user.getUsername();
                            System.out.println("fbUserName : " + fbUserName);
                            try {
                                fbEmail = user.getProperty(STRING.FB_EMAIL).toString();
                            }
                            catch (final Exception e) {
                                e.printStackTrace();
                                try {
                                    final String fbSafeEmail = user.asMap().get(STRING.FB_EMAIL).toString();
                                    fbEmail = fbSafeEmail;
                                }
                                catch (final Exception e2) {
                                    e2.printStackTrace();
                                    fbEmail = null;
                                }
                            }
                            ToastL("EMAIL : " + fbEmail + "\nUSERNAME : " + fbUserName);
                            LogR("EMAIL : " + fbEmail + "\nUSERNAME : " + fbUserName);
                        }
                        else {
                            ToastL("Unable to Login...\nPlease try again later.");
                            LogR("::USER NULL::");
                            sFBLogout();
                        }
                    }

                }).executeAsync();

            }
            else
                if (state.isClosed()) {
                    LogR("Logged out...");
                }
        }

        @Override
        public void AlreadyLoggedIn() {
            ToastL("AlreadyLoggedIn");
        }
    };

    /**
     * <b>LogOut/Flush</b> the "session" stored in the APP.<br>
     * <b>REASON 1 :</b> User wants to logout.<br>
     * <b>REASON 2 :</b> Prev session is stored in the APP & is Invalid for current login.<br>
     * <b>REASON 3 :</b> User removed access for our app in his "<a href="https://www.facebook.com/settings?tab=applications">App Settings</a>".
     */
    private void sFBLogout() {
        Session session = Session.getActiveSession();
        if (!session.isClosed()) {
            session.closeAndClearTokenInformation();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
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
                final ObjectAnimator objAni = new ObjectAnimator();

                objAni.setPropertyName("alpha");
                objAni.setDuration(100);
                objAni.setTarget(mRL);
                objAni.setFloatValues(1.0f, .7f);
                objAni.start();
                objAni.setRepeatCount(0);
                objAni.addListener(new AnimatorListener() {

                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                        final ObjectAnimator objAnima = new ObjectAnimator();

                        objAnima.setPropertyName("alpha");
                        objAnima.setDuration(100);
                        objAnima.setTarget(mRL);
                        objAnima.setFloatValues(.8f, 1f);
                        objAnima.start();
                        objAnima.setRepeatCount(0);
                        if (position % 2 == 1) {
                            mRL.setBackgroundResource(R.drawable.bg);
                        }
                        else {
                            mRL.setBackgroundResource(R.drawable.walkthrough_bg);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                        final ObjectAnimator ojbAnim = new ObjectAnimator();

                        ojbAnim.setPropertyName("alpha");
                        ojbAnim.setDuration(100);
                        ojbAnim.setTarget(mRL);
                        ojbAnim.setFloatValues(.8f, 1f);
                        ojbAnim.start();
                        ojbAnim.setRepeatCount(0);
                        if (position % 2 == 1) {
                            mRL.setBackgroundResource(R.drawable.bg);
                        }
                        else {
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
                WalkThrough.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int next = pager.getCurrentItem() + 1;
                        next = (next % pager.getAdapter().getCount());
                        pager.setCurrentItem(next, true);
                    }
                });
            }
        }, 1000, 3000, TimeUnit.MILLISECONDS);

        pager.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                shutDown();
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        shutDown();
        finish();
    }

    public void Signup(View v) {
        shutDown();
        startActivity(new Intent(getApplicationContext(), SignUp.class));
        AnimNext();
    }

    public void Login(View v) {
        shutDown();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
        AnimNext();
    }

    @Override
    protected void onDestroy() {
        shutDown();
        super.onDestroy();
    }

    private void shutDown() {

        if (scheduleTaskExecutor.isShutdown())
        {
            return;
        }

        scheduleTaskExecutor.shutdown();

    }

    private void AnimNext() {
        overridePendingTransition(R.anim.enter, R.anim.exit);
        return;
    }

    /*******************************************************************/
    /**
     * @param RED
     */
    private void LogR(String msg) {
        if (Constants.enableLog)
        {
            Log.wtf(tag, msg);
        }
    }

    /**
     * @param text
     */
    private void ToastL(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    /*******************************************************************/

}
