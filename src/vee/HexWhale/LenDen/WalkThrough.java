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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
import com.android.volley.VolleyError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

import org.json.JSONException;
import org.json.JSONObject;

import vee.HexWhale.LenDen.Parsers.Login.FBRegLogin;
import vee.HexWhale.LenDen.Storage.GlobalSharedPrefs;
import vee.HexWhale.LenDen.Storage.SettersNGetters;
import vee.HexWhale.LenDen.Utils.Constants;
import vee.HexWhale.LenDen.Utils.Constants.API.STRING;
import vee.HexWhale.LenDen.Utils.Constants.API.TYPE;
import vee.HexWhale.LenDen.Utils.Constants.API.URL;
import vee.HexWhale.LenDen.Utils.Constants.KEY;
import vee.HexWhale.LenDen.Utils.Constants.LOGIN_TYPE;
import vee.HexWhale.LenDen.aUI.Facebook;
import vee.HexWhale.LenDen.aUI.Facebook.FacebookListener;
import vee.HexWhale.LenDen.aUI.Pagers.WalkThroughPager;
import vee.HexWhale.LenDen.bg.Threads.FetcherListener;
import vee.HexWhale.LenDen.bg.Threads.GetData;
import vee.HexWhale.LenDen.bg.Threads.GetDataFromUrl;
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
    private static String fbFirstName, fbLastName, fbEmail, fbID, fbUserName, fbAccessToken;

    // XXX _INIT
    private GetDataFromUrl mDataFromUrl;
    GlobalSharedPrefs mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        tag = TagGen.getTag(this.getClass());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.walkthrough);
        mPrefs = new GlobalSharedPrefs(this);
        scheduleTaskExecutor = Executors.newScheduledThreadPool(1);
        pager = (ViewPager) findViewById(R.id.pager);
        mRL = (RelativeLayout) findViewById(R.id.wlk_thrg_rel_lyt);
        mDataFromUrl = new GetDataFromUrl(this, mFetcherListener);
        InitilizePager(); // XXX 1
    }

    private final FetcherListener mFetcherListener = new FetcherListener() {

        @Override
        public void finishedFetching(int type, String response) {

            LogR("+++ " + response);

        }

        @Override
        public void errorFetching(int type, VolleyError error) {
            try {
                error.printStackTrace();
                LogR("---" + "Error");
            }
            finally {
                ToastL("Error");
            }

        }

        @Override
        public void beforeParsing(int type) {

        }

        @Override
        public void startedParsing(int type) {

        }

        @Override
        public void ParsingException(Exception e) {
            e.printStackTrace();
        }

        @SuppressLint("InlinedApi")
        @Override
        public void finishedParsing(int typ) {
            final FBRegLogin fbRegLgn = SettersNGetters.getFbRegLogin();

            if (fbRegLgn != null) {
                if (fbRegLgn.getStatus().equalsIgnoreCase(STRING.SUCCESS)) {

                    final Intent intent = new Intent(getApplicationContext(), Home.class);

                    if (Build.VERSION.SDK_INT >= 11) {
                        System.out.println("FLAG_ACTIVITY_CLEAR_TASK");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    }
                    else {
                        System.out.println("FLAG_ACTIVITY_CLEAR_TOP");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    }

                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (mPrefs.getIntInPref(KEY.LOGIN_TYPE) != LOGIN_TYPE.FACEBOOK
                            || !fbEmail.equalsIgnoreCase(mPrefs.getStringInPref(KEY.MY_E_MAIL)))
                    {
                        mPrefs.setStringInPref(KEY.MY_F_NAME, "");
                        mPrefs.setStringInPref(KEY.MY_L_NAME, "");
                        mPrefs.setStringInPref(KEY.MY_E_MAIL, "");
                    }

                    mPrefs.setIntInPref(KEY.LOGIN_TYPE, LOGIN_TYPE.FACEBOOK);

                    startActivity(intent);
                    finish();
                    AnimNext();
                }
                else {
                    switch (typ) {
                        case TYPE.FB_REGISTER:

                            if (fbRegLgn.getError_message().equalsIgnoreCase(STRING.USER_ALREADY_EXISTS)
                                    || fbRegLgn.getError_code().equalsIgnoreCase(STRING.USER_ALREADY_EXISTS_ERROR))
                            {
                                mDataFromUrl.setAccessToken();
                                mDataFromUrl.GetString(TYPE.FB_LOGIN, getBody(TYPE.FB_LOGIN), GetData.getUrl(URL.FB_LOGIN));
                            } else {
                                ToastL(STRING.ERROR + fbRegLgn.getError_message());
                            }

                            break;
                        case TYPE.FB_LOGIN:
                            ToastL(STRING.ERROR + fbRegLgn.getError_message());
                            break;
                    }

                }
            }
            else {
                ToastL("{Unknown ERROR }");
            }
        }

        @Override
        public void tokenError(String tokenError) {
            ToastL(tokenError);
        }
    };

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

                            fbFirstName = user.getFirstName();
                            fbLastName = user.getLastName();
                            fbID = user.getId();
                            fbUserName = user.getUsername();
                            fbAccessToken = session.getAccessToken();

                            try {
                                fbEmail = user.getProperty(STRING.EMAIL).toString();
                            }
                            catch (final Exception e) {
                                e.printStackTrace();
                                try {
                                    final String fbSafeEmail = user.asMap().get(STRING.EMAIL).toString();
                                    fbEmail = fbSafeEmail;
                                }
                                catch (final Exception e2) {
                                    e2.printStackTrace();
                                    fbEmail = null;
                                }
                            }
                            startFBLogin();
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

    private void startFBLogin() {

        mDataFromUrl.setAccessToken();
        mDataFromUrl.GetString(TYPE.FB_REGISTER, getBody(TYPE.FB_REGISTER), GetData.getUrl(URL.FB_REGISTER));

    }

    private String getBody(int tokenType) {

        JSONObject mJsonObject = null;
        mJsonObject = new JSONObject();

        switch (tokenType) {
            case TYPE.FB_REGISTER:
                try {
                    mJsonObject.put(STRING.FIRSTNAME, fbFirstName);
                    if (fbLastName.equalsIgnoreCase(fbFirstName))
                    {
                        fbLastName = " ";
                    }
                    mJsonObject.put(STRING.LASTNAME, fbLastName);
                    mJsonObject.put(STRING.EMAIL, fbEmail);
                    mJsonObject.put(STRING.FB_ID, fbID);
                    mJsonObject.put(STRING.FB_TOKEN, fbAccessToken);
                    mJsonObject.put(STRING.FB_USERNAME, fbUserName);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }

                LogR(mJsonObject.toString());
                //ToastL(fbFirstName + "\n" + fbLastName + "\n" + fbID + "\n" + fbUserName + "\n" + fbEmail + "\n" + fbAccessToken);
                LogW(fbFirstName + "\n" + fbLastName + "\n" + fbID + "\n" + fbUserName + "\n" + fbEmail + "\n" + fbAccessToken);

                break;

            case TYPE.FB_LOGIN:
                try {
                    mJsonObject.put(STRING.EMAIL, fbEmail);
                    mJsonObject.put(STRING.FB_USERNAME, fbUserName);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
                LogR(mJsonObject.toString());
                break;

        }

        return mJsonObject.toString();
    }

    /**
     * <b>LogOut/Flush</b> the "session" stored in the APP.<br>
     * <b>REASON 1 :</b> User wants to logout.<br>
     * <b>REASON 2 :</b> Prev session is stored in the APP & is Invalid for current login.<br>
     * <b>REASON 3 :</b> User removed access for our app in his
     * "<a href="https://www.facebook.com/settings?tab=applications">App Settings</a>".
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

    /*******************************************************************/
    /**
     * @param RED
     */
    private void LogW(String msg) {
        if (Constants.enableLog)
        {
            Log.w(tag, msg);
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
