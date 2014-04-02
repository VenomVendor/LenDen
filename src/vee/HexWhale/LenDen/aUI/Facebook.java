/**
 * ***Copyright(c)  :   2014-Present, VenomVendor.***
 * Author       :   VenomVendor
 * Dated        :   2 Apr, 2014 8:17:25 PM
 * Project      :   LenDen-Android
 * Client       :   LenDen
 * Contact      :   info@VenomVendor.com
 * URL          :   https://www.google.co.in/search?q=VenomVendor
 * Copyright(c) :   2014-Present, VenomVendor.
 * License      :   This work is licensed under Attribution-NonCommercial 3.0 Unported
 *                  License info at http://creativecommons.org/licenses/by-nc/3.0/deed.en_US
 *                  Read More at http://creativecommons.org/licenses/by-nc/3.0/legalcode
 **/

package vee.HexWhale.LenDen.aUI;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.facebook.LoggingBehavior;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;

import vee.HexWhale.LenDen.Utils.Constants;
import vee.HexWhale.LenDen.Utils.Constants.API.STRING;
import vee.HexWhale.LenDen.bg.Threads.TagGen;

import java.util.Arrays;
import java.util.List;

/**
 * Used for Login/Signingup user via Facebook.
 * 
 * @author VenomVendor<br>
 *         <b>SDK Used</b> <a
 *         href="https://developers.facebook.com/docs/android/downloads/">facebook-android-sdk</a><br>
 *         <b>Direct Download</b> <a
 *         href="https://developers.facebook.com/resources/facebook-android-sdk-3.8.zip"
 *         >facebook-sdk-3.8.zip</a>&emsp;
 */
public class Facebook {

    /** The tag. */
    private final String tag = TagGen.getTag(this.getClass());

    /** The facebook listener. */
    FacebookListener mFacebookListener;

    /** The Context. */
    Activity mActivity;

    /** The status callback. */
    private final Session.StatusCallback statusCallback = new SessionStatusCallback();

    /** The Constant PERMISSIONS. */
    private static final List<String> PERMISSIONS = Arrays.asList(STRING.FB_EMAIL);

    /**
     * Instantiates a new facebook.
     * 
     * @param mActivity the m activity
     * @param mListener the m listener
     */
    public Facebook(Activity mActivity, FacebookListener mListener) {
        this.mActivity = mActivity;
        mFacebookListener = mListener;
    }

    /**
     * Log me in.
     */
    public void LogMeIn() {

        LogR("1");
        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

        Session session = Session.getActiveSession();

        if (session == null) {
            LogR("2");
            session = Session.openActiveSessionFromCache(mActivity);
            if (session == null) {
                LogR("3");
                session = new Session(mActivity);
            }

            Session.setActiveSession(session);

            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
                LogR("4");
                openLoginForm(session);
            }
        }

        Session.setActiveSession(session);

        if (!session.isOpened() && !session.isClosed()) {
            LogR("5");
            openLoginForm(session);
        }
        else {
            LogR("6");
            Session.openActiveSession(mActivity, true, statusCallback);
            mFacebookListener.AlreadyLoggedIn();
            return;
        }
    }

    /**
     * Open login form.
     * 
     * @param session the session
     */
    private void openLoginForm(Session session) {
        final Session.OpenRequest request = new Session.OpenRequest(mActivity);
        request.setPermissions(Facebook.PERMISSIONS);
        request.setCallback(statusCallback);
        session.openForRead(request);
    }

    /**
     * The Class SessionStatusCallback.
     */
    public class SessionStatusCallback implements Session.StatusCallback {

        /**
         * @see com.facebook.Session.StatusCallback#call(com.facebook.Session,
         *      com.facebook.SessionState, java.lang.Exception)
         */
        @Override
        public void call(final Session session, SessionState state, Exception exception) {
            mFacebookListener.call(session, state, exception, mActivity.getApplicationContext());
        }
    }

    /**
     * Log r.
     * 
     * @param msg the msg
     */
    private void LogR(String msg) {
        if (Constants.enableLog)
        {
            Log.wtf(tag, msg);
        }
    }

    /**
     * The listener interface for receiving facebook events. The class that is interested in
     * processing a facebook event implements this interface, and the object created with that class
     * is registered with a component using the component's
     * <code>addFacebookListener<code> method. When
     * the facebook event occurs, that object's appropriate
     * method is invoked.
     * 
     * @see FacebookEvent
     */
    public interface FacebookListener {

        /**
         * Already logged in.
         */
        public void AlreadyLoggedIn();

        /**
         * Call.
         * 
         * @param session the session
         * @param state the state
         * @param exception the exception
         * @param context the context
         */
        public void call(final Session session, SessionState state, Exception exception, Context context);
    }

}
