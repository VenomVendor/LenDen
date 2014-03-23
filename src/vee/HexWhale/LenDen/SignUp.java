/**
 * ***Copyright(c) : 2014-Present, VenomVendor.***
 * Author : VenomVendor
 * Dated : 17 Feb, 2014 3:28:58 AM
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

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import vee.HexWhale.LenDen.Parsers.AccessToken.GetAccessToken;
import vee.HexWhale.LenDen.Storage.GlobalSharedPrefs;
import vee.HexWhale.LenDen.Storage.SettersNGetters;
import vee.HexWhale.LenDen.Utils.Constants.API.STRING;
import vee.HexWhale.LenDen.Utils.Constants.API.TYPE;
import vee.HexWhale.LenDen.Utils.Constants.API.URL;
import vee.HexWhale.LenDen.Utils.Constants.KEY;
import vee.HexWhale.LenDen.bg.Threads.FetcherListener;
import vee.HexWhale.LenDen.bg.Threads.GetData;
import vee.HexWhale.LenDen.bg.Threads.GetDataFromUrl;
import vee.HexWhale.LenDen.bg.Threads.TagGen;
import vee.HexWhale.LenDen.bg.Threads.Validator;

public class SignUp extends FragmentActivity {
    private String tag = "UNKNOWN";
    int type = -1;
    EditText mName, mLName, mMail, mPsw, mRePsw;
    GlobalSharedPrefs mPrefs;
    GetDataFromUrl mDataFromUrl;
    GetAccessToken mAccessToken;
    GetAccessToken isRegistered;
    ProgressBar mBar;
    ImageView buttonSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        tag = TagGen.getTag(this.getClass());
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.signup);
        mName = (EditText) findViewById(R.id.signup_name);
        mLName = (EditText) findViewById(R.id.signup_uname);
        mMail = (EditText) findViewById(R.id.signup_email);
        mPsw = (EditText) findViewById(R.id.signup_psw);
        mRePsw = (EditText) findViewById(R.id.signup_repsw);
        mBar = (ProgressBar) findViewById(R.id.signup_progressbar);

        mName.setText("Android");
        mLName.setText("Test");
        mMail.setText("naa@naa.com");
        mPsw.setText("qwerty");
        mRePsw.setText("qwerty");

        mPrefs = new GlobalSharedPrefs(this);
        mDataFromUrl = new GetDataFromUrl(this, mFetcherListener);
        if (mPrefs.getStringInPref(KEY.ACCESS_TOKEN) == null) {
            System.out.println("FetchToken");
            mDataFromUrl.GetString(TYPE.ACCESSTOKEN, getBody(TYPE.ACCESSTOKEN), GetData.getUrl(URL.ACCESSTOKEN));

        }

    }

    private String getBody(final int mType) {
        JSONObject mJsonObject = null;
        mJsonObject = new JSONObject();
        try {
            switch (mType) {

                case TYPE.ACCESSTOKEN:
                    System.out.println("ISSUE " + TYPE.ACCESSTOKEN);
                    mJsonObject.put(STRING.AUTH, mPrefs.getStringInPref(KEY.AUTH_CODE));
                    break;

                case TYPE.REGISTER_EMAIL:
                    System.out.println("REGISTER_EMAIL " + TYPE.REGISTER_EMAIL);
                    mDataFromUrl.setAccessToken();
                    mJsonObject.put(STRING.FIRSTNAME, mName.getText().toString());
                    mJsonObject.put(STRING.LASTNAME, mLName.getText().toString());
                    mJsonObject.put(STRING.EMAIL, mMail.getText().toString());
                    mJsonObject.put(STRING.PASSWORD, mRePsw.getText().toString());
                    break;

            }
            return mJsonObject.toString();
        }
        catch (final JSONException e) {
            e.printStackTrace();
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private final FetcherListener mFetcherListener = new FetcherListener() {

        @Override
        public void startedParsing(int type) {

        }

        @Override
        public void finishedParsing(int typ) {

            switch (typ) {
                case TYPE.ACCESSTOKEN:
                    mAccessToken = SettersNGetters.getAccessToken();

                    if (mAccessToken != null) {
                        if (mAccessToken.getStatus().equalsIgnoreCase("success")) {

                            mPrefs.setStringInPref(KEY.ACCESS_TOKEN, mAccessToken.getResponse().getAccess_token());
                            mPrefs.setStringInPref(KEY.REFRESH_TOKEN, mAccessToken.getResponse().getRefresh_token());
                            SignUp.this.LogBlk("AccessToken " + mAccessToken.getResponse().getAccess_token());
                            SignUp.this.LogBlk("AccessToken's RefreshToken  " + mAccessToken.getResponse().getRefresh_token());

                        }
                        else {
                            SignUp.this.ToastL("Error : " + mAccessToken.getError_message());
                        }
                    }
                    else {
                        SignUp.this.ToastL("{ ERROR }");
                    }
                    break;

                case TYPE.REGISTER_EMAIL:
                    isRegistered = SettersNGetters.isRegistered();

                    if (isRegistered != null) {
                        if (isRegistered.getStatus().equalsIgnoreCase("success")) {

                            SignUp.this.ToastL("Registered Successfully");
                            SignUp.this.finish();
                            SignUp.this.AnimNext();

                        }
                        else {
                            SignUp.this.ToastL("Error : " + isRegistered.getError_message());
                        }
                    }
                    else {
                        SignUp.this.ToastL("{ SERVER ERROR }");
                    }
                    break;

                default:
                    break;
            }

        }

        @Override
        public void finishedFetching(int type, String response) {
            buttonSignup.setEnabled(true);
            buttonSignup.setImageResource(R.drawable.signup_proceed);
            mBar.setVisibility(View.GONE);
            SignUp.this.LogR("+++" + response.toString());
        }

        @Override
        public void errorFetching(int type, VolleyError error) {
            buttonSignup.setEnabled(true);
            buttonSignup.setImageResource(R.drawable.signup_proceed);
            mBar.setVisibility(View.GONE);
            SignUp.this.type = type;
            try {
                error.printStackTrace();
                SignUp.this.LogR("---" + "Error");
            }
            finally {
                SignUp.this.ToastL("{ UNKNOWN ERROR }");
                SignUp.this.ToastL("Check yout internet connectivity.");
            }
        }

        @Override
        public void beforeParsing(int type) {

        }

        @Override
        public void ParsingException(Exception e) {

        }

        @Override
        public void tokenError(String tokenError) {

            ToastL(tokenError);
        }
    };

    public void Finish(View v) {
        finish();
        AnimPrev();
    }

    public void Validate(View v) {

        if (!Validator.hasMinChars(mName, 4).equals("k")) {
            mName.setError(Validator.hasMinChars(mName, 4));
            return;
        }
        if (!Validator.hasMinChars(mLName, 1).equals("k")) {
            mLName.setError(Validator.hasMinChars(mLName, 1));
            return;
        }
        if (!Validator.isvalidEmail(mMail).equals("k")) {
            mMail.setError(Validator.isvalidEmail(mMail));
            return;
        }
        if (!Validator.hasMinChars(mPsw, 6).equals("k")) {
            mPsw.setError(Validator.hasMinChars(mPsw, 6));
            return;
        }

        if (!Validator.hasMinChars(mRePsw, 1).equals("k")) {
            mRePsw.setError("Password doesn't match");
            return;
        }

        if (!Validator.doesPasswordMatch(mPsw, mRePsw)) {
            mRePsw.setError("Password doesn't match");
            return;
        }

        buttonSignup = (ImageView) v;

        buttonSignup.setImageResource(0);

        buttonSignup.setEnabled(false);

        mBar.setVisibility(View.VISIBLE);

        mDataFromUrl.GetString(TYPE.REGISTER_EMAIL, getBody(TYPE.REGISTER_EMAIL), GetData.getUrl(URL.REGISTER_EMAIL));
    }

    @Override
    public void onBackPressed() {
        finish();
        AnimPrev();
    }

    private void AnimPrev() {
        overridePendingTransition(R.anim.android_slide_in_left, R.anim.android_slide_out_right);
        return;
    }

    private void AnimNext() {
        overridePendingTransition(R.anim.enter, R.anim.exit);
        return;
    }

    /*******************************************************************/

    /**
     * @param RED
     */
    public void LogR(String msg) {
        Log.wtf(tag, msg);
    }

    /**
     * @param Black
     */
    public void LogBlk(String msg) {
        Log.v(tag, msg);
    }

    /**
     * @param text
     */
    private void ToastL(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    /**
     * @param text
     */
    private void ToastS(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    /*******************************************************************/
}
