/**
 * ***Copyright(c) : 2014-Present, VenomVendor.***
 * Author : VenomVendor
 * Dated : 17 Feb, 2014 3:28:58 AM
 * Project : LenDen-Android
 * Client : LenDen
 * Contact : info@VenomVendor.com
 * URL : https://www.google.co.in/search?q=VenomVendor
 * Copyright(c) : 2014-Present, VenomVendor.
 * License : This work is licensed under Attribution-NonCommercial 3.0 Unported (CC BY-NC 3.0).
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
        this.tag = TagGen.getTag(this.getClass());
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.signup);
        this.mName = (EditText) this.findViewById(R.id.signup_name);
        this.mLName = (EditText) this.findViewById(R.id.signup_uname);
        this.mMail = (EditText) this.findViewById(R.id.signup_email);
        this.mPsw = (EditText) this.findViewById(R.id.signup_psw);
        this.mRePsw = (EditText) this.findViewById(R.id.signup_repsw);
        this.mBar = (ProgressBar) this.findViewById(R.id.signup_progressbar);

        this.mName.setText("Android");
        this.mLName.setText("Test");
        this.mMail.setText("naa@naa.com");
        this.mPsw.setText("qwerty");
        this.mRePsw.setText("qwerty");

        this.mPrefs = new GlobalSharedPrefs(this);
        this.mDataFromUrl = new GetDataFromUrl(this, this.mFetcherListener);
        if (this.mPrefs.getStringInPref(KEY.ACCESS_TOKEN) == null) {
            System.out.println("FetchToken");
            this.mDataFromUrl.GetString(TYPE.ACCESSTOKEN, this.getBody(TYPE.ACCESSTOKEN), GetData.getUrl(URL.ACCESSTOKEN));

        }

    }

    private String getBody(final int mType) {
        JSONObject mJsonObject = null;
        mJsonObject = new JSONObject();
        try {
            switch (mType) {

                case TYPE.ACCESSTOKEN:
                    System.out.println("ISSUE " + TYPE.ACCESSTOKEN);
                    mJsonObject.put(STRING.AUTH, this.mPrefs.getStringInPref(KEY.AUTH_CODE));
                    break;

                case TYPE.REGISTER_EMAIL:
                    System.out.println("REGISTER_EMAIL " + TYPE.REGISTER_EMAIL);
                    this.mDataFromUrl.setAccessToken();
                    mJsonObject.put(STRING.FIRSTNAME, this.mName.getText().toString());
                    mJsonObject.put(STRING.LASTNAME, this.mLName.getText().toString());
                    mJsonObject.put(STRING.EMAIL, this.mMail.getText().toString());
                    mJsonObject.put(STRING.PASSWORD, this.mRePsw.getText().toString());
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
                    SignUp.this.mAccessToken = SettersNGetters.getAccessToken();

                    if (SignUp.this.mAccessToken != null) {
                        if (SignUp.this.mAccessToken.getStatus().equalsIgnoreCase("success")) {

                            SignUp.this.mPrefs.setStringInPref(KEY.ACCESS_TOKEN, SignUp.this.mAccessToken.getResponse().getAccess_token());
                            SignUp.this.mPrefs.setStringInPref(KEY.REFRESH_TOKEN, SignUp.this.mAccessToken.getResponse().getRefresh_token());
                            SignUp.this.LogBlk("AccessToken " + SignUp.this.mAccessToken.getResponse().getAccess_token());
                            SignUp.this.LogBlk("AccessToken's RefreshToken  " + SignUp.this.mAccessToken.getResponse().getRefresh_token());

                        } else {
                            SignUp.this.ToastL("Error : " + SignUp.this.mAccessToken.getError_message());
                        }
                    } else {
                        SignUp.this.ToastL("{ ERROR }");
                    }
                    break;

                case TYPE.REGISTER_EMAIL:
                    SignUp.this.isRegistered = SettersNGetters.isRegistered();

                    if (SignUp.this.isRegistered != null) {
                        if (SignUp.this.isRegistered.getStatus().equalsIgnoreCase("success")) {

                            SignUp.this.ToastL("Registered Successfully");
                            SignUp.this.finish();
                            SignUp.this.AnimNext();

                        } else {
                            SignUp.this.ToastL("Error : " + SignUp.this.isRegistered.getError_message());
                        }
                    } else {
                        SignUp.this.ToastL("{ SERVER ERROR }");
                    }
                    break;

                default:
                    break;
            }

        }

        @Override
        public void finishedFetching(int type, String response) {
            SignUp.this.buttonSignup.setEnabled(true);
            SignUp.this.buttonSignup.setImageResource(R.drawable.signup_proceed);
            SignUp.this.mBar.setVisibility(View.GONE);
            SignUp.this.LogR("+++" + response.toString());
        }

        @Override
        public void errorFetching(int type, VolleyError error) {
            SignUp.this.buttonSignup.setEnabled(true);
            SignUp.this.buttonSignup.setImageResource(R.drawable.signup_proceed);
            SignUp.this.mBar.setVisibility(View.GONE);
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
        this.finish();
        this.AnimPrev();
    }

    public void Validate(View v) {

        if (!Validator.hasMinChars(this.mName, 4).equals("k")) {
            this.mName.setError(Validator.hasMinChars(this.mName, 4));
            return;
        }
        if (!Validator.hasMinChars(this.mLName, 1).equals("k")) {
            this.mLName.setError(Validator.hasMinChars(this.mLName, 1));
            return;
        }
        if (!Validator.isvalidEmail(this.mMail).equals("k")) {
            this.mMail.setError(Validator.isvalidEmail(this.mMail));
            return;
        }
        if (!Validator.hasMinChars(this.mPsw, 6).equals("k")) {
            this.mPsw.setError(Validator.hasMinChars(this.mPsw, 6));
            return;
        }

        if (!Validator.hasMinChars(this.mRePsw, 1).equals("k")) {
            this.mRePsw.setError("Password doesn't match");
            return;
        }

        if (!Validator.doesPasswordMatch(this.mPsw, this.mRePsw)) {
            this.mRePsw.setError("Password doesn't match");
            return;
        }

        this.buttonSignup = (ImageView) v;

        this.buttonSignup.setImageResource(0);

        this.buttonSignup.setEnabled(false);

        this.mBar.setVisibility(View.VISIBLE);

        this.mDataFromUrl.GetString(TYPE.REGISTER_EMAIL, this.getBody(TYPE.REGISTER_EMAIL), GetData.getUrl(URL.REGISTER_EMAIL));
    }

    @Override
    public void onBackPressed() {
        this.finish();
        this.AnimPrev();
    }

    private void AnimPrev() {
        this.overridePendingTransition(R.anim.android_slide_in_left, R.anim.android_slide_out_right);
        return;
    }

    private void AnimNext() {
        this.overridePendingTransition(R.anim.enter, R.anim.exit);
        return;
    }

    /*******************************************************************/

    /**
     * @param RED
     */
    public void LogR(String msg) {
        Log.wtf(this.tag, msg);
    }

    /**
     * @param Black
     */
    public void LogBlk(String msg) {
        Log.v(this.tag, msg);
    }

    /**
     * @param text
     */
    private void ToastL(String text) {
        Toast.makeText(this.getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    /**
     * @param text
     */
    private void ToastS(String text) {
        Toast.makeText(this.getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    /*******************************************************************/
}
