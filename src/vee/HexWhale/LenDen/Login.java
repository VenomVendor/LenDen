/**
 * ***Copyright(c) : 2014-Present, VenomVendor.***
 * Author : VenomVendor
 * Dated : 17 Feb, 2014 3:28:57 AM
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

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import vee.HexWhale.LenDen.Parsers.AccessToken.GetAccessToken;
import vee.HexWhale.LenDen.Storage.SettersNGetters;
import vee.HexWhale.LenDen.Utils.Constants.API.STRING;
import vee.HexWhale.LenDen.Utils.Constants.API.TYPE;
import vee.HexWhale.LenDen.Utils.Constants.API.URL;
import vee.HexWhale.LenDen.bg.Threads.FetcherListener;
import vee.HexWhale.LenDen.bg.Threads.GetData;
import vee.HexWhale.LenDen.bg.Threads.GetDataFromUrl;
/***************************************************************/
import vee.HexWhale.LenDen.bg.Threads.NetworkConnection;
import vee.HexWhale.LenDen.bg.Threads.TagGen;
import vee.HexWhale.LenDen.bg.Threads.Validator;

/***************************************************************/

public class Login extends FragmentActivity {
    private String tag = "UNKNOWN";
    EditText mUserName, mPassword;
    GetDataFromUrl mDataFromUrl;
    int type = -1;

    @Override
    protected void onCreate(Bundle arg0) {
        this.tag = TagGen.getTag(this.getClass());
        super.onCreate(arg0);
        this.setContentView(R.layout.login);
        this.mDataFromUrl = new GetDataFromUrl(this, this.mFetcherListener);
        this.mUserName = (EditText) this.findViewById(R.id.login_name);
        this.mPassword = (EditText) this.findViewById(R.id.login_psw);

        this.mUserName.setText("naa@naa.com");
        this.mPassword.setText("qwerty");

    }

    public void Signin(View v) {
        this.validate();
    }

    private void validate() {
        if (!Validator.hasMinChars(this.mUserName, 4).equals("k")) {
            this.mUserName.setError(Validator.hasMinChars(this.mUserName, 4));
            return;
        }

        if (!Validator.hasMinChars(this.mPassword, 6).equals("k")) {
            this.mPassword.setError(Validator.hasMinChars(this.mPassword, 4));
            return;
        }

        this.SigninI();

    }

    private void SigninI() {
        if (!NetworkConnection.isAvail(this.getApplicationContext())) {
            this.ToastL("No internet Connection");
            return;
        }
        this.mDataFromUrl.setAccessToken();
        this.mDataFromUrl.GetString(TYPE.LOGIN_EMAIL, this.getBody(TYPE.LOGIN_EMAIL), GetData.getUrl(URL.LOGIN_EMAIL));
    }

    private String getBody(int tokenType) {
        JSONObject mJsonObject = null;
        mJsonObject = new JSONObject();
        try {
            mJsonObject.put(STRING.PASSWORD, this.mPassword.getText().toString().trim());
            mJsonObject.put(STRING.EMAIL, this.mUserName.getText().toString().trim());
            return mJsonObject.toString();
        }
        catch (final JSONException e) {
            e.printStackTrace();
        }

        return null;

    }

    private final FetcherListener mFetcherListener = new FetcherListener() {

        @Override
        public void finishedFetching(int type, String response) {

            Login.this.LogR("+++ " + response);

        }

        @Override
        public void errorFetching(int type, VolleyError error) {
            try {
                error.printStackTrace();
                Login.this.LogR("---" + "Error");
            }
            finally {
                Login.this.ToastL("Error");
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
            if (typ == TYPE.LOGIN_EMAIL) {
                final GetAccessToken mEmail = SettersNGetters.isLoggedInViaEmail();

                if (mEmail != null) {
                    if (mEmail.getStatus().equalsIgnoreCase("success")) {

                        final Intent intent = new Intent(Login.this.getApplicationContext(), Home.class);

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

                        Login.this.startActivity(intent);
                        Login.this.finish();
                        Login.this.AnimNext();
                    }
                    else {
                        Login.this.ToastL("Error : " + mEmail.getError_message());
                    }
                }
                else {
                    Login.this.ToastL("{ ERROR }");
                }

            }
            else {
                Login.this.ToastL("{ ERROR }");
            }
        }

        @Override
        public void tokenError(String tokenError) {
            ToastL(tokenError);
        }
    };

    @Override
    public void onBackPressed() {
        this.startActivity(new Intent(this.getApplicationContext(), WalkThrough.class));
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
    private void LogR(String msg) {
        Log.wtf(this.tag, msg);
    }

    /**
     * @param text
     */
    private void ToastL(String text) {
        Toast.makeText(this.getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    /*******************************************************************/
}
