/**
 * ***Copyright(c) : 2014-Present, VenomVendor.***
 * Author : VenomVendor
 * Dated : 17 Feb, 2014 3:28:57 AM
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

import static vee.HexWhale.LenDen.Utils.Constants.API.BASE_URL;

import org.json.JSONException;
import org.json.JSONObject;

import vee.HexWhale.LenDen.Parsers.AccessToken.GetAccessToken;
import vee.HexWhale.LenDen.Storage.SettersNGetters;
import vee.HexWhale.LenDen.Utils.Constants.API.STRING;
import vee.HexWhale.LenDen.Utils.Constants.API.TYPE;
import vee.HexWhale.LenDen.Utils.Constants.API.URL;
import vee.HexWhale.LenDen.bg.Threads.FetcherListener;
import vee.HexWhale.LenDen.bg.Threads.GetDataFromUrl;
/***************************************************************/
import vee.HexWhale.LenDen.bg.Threads.NetworkConnection;
import vee.HexWhale.LenDen.bg.Threads.TagGen;
import vee.HexWhale.LenDen.bg.Threads.Validator;
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

/***************************************************************/

public class Login extends FragmentActivity {
    private String tag = "UNKNOWN";
    EditText mUserName, mPassword;
    GetDataFromUrl mDataFromUrl;
    int type = -1;

    @Override
    protected void onCreate(Bundle arg0) {
        tag = TagGen.getTag(this.getClass());
        super.onCreate(arg0);
        setContentView(R.layout.login);
        mDataFromUrl = new GetDataFromUrl(this, mFetcherListener);
        mUserName = (EditText) findViewById(R.id.login_name);
        mPassword = (EditText) findViewById(R.id.login_psw);

        mUserName.setText("naa@naa.com");
        mPassword.setText("qwerty");

    }

    public void Signin(View v) {
        validate();
    }

    private void validate() {
        if (!Validator.hasMinChars(mUserName, 4).equals("k")) {
            mUserName.setError(Validator.hasMinChars(mUserName, 4));
            return;
        }

        if (!Validator.hasMinChars(mPassword, 6).equals("k")) {
            mPassword.setError(Validator.hasMinChars(mPassword, 4));
            return;
        }

        SigninI();

    }

    private void SigninI() {
        if (!NetworkConnection.isAvail(getApplicationContext())) {
            ToastL("No internet Connection");
            return;
        }
        mDataFromUrl.setAccessToken();
        mDataFromUrl.GetString(TYPE.LOGIN_EMAIL, getBody(TYPE.LOGIN_EMAIL), getUrl(URL.LOGIN_EMAIL));
    }

    private String getUrl(String loginEmail) {
        return BASE_URL + loginEmail;
    }

    private String getBody(int tokenType) {
        JSONObject mJsonObject = null;
        mJsonObject = new JSONObject();
        try {
            mJsonObject.put(STRING.PASSWORD, mPassword.getText().toString().trim());
            mJsonObject.put(STRING.EMAIL, mUserName.getText().toString().trim());
            return mJsonObject.toString();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }

    private FetcherListener mFetcherListener = new FetcherListener() {

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
            if (typ == TYPE.LOGIN_EMAIL) {
                GetAccessToken mEmail = SettersNGetters.isLoggedInViaEmail();

                if (mEmail != null) {
                    if (mEmail.getStatus().equalsIgnoreCase("success")) {

                        Intent intent = new Intent(getApplicationContext(), Home.class);

                        if (Build.VERSION.SDK_INT >= 11) {
                            System.out.println("FLAG_ACTIVITY_CLEAR_TASK");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        } else {
                            System.out.println("FLAG_ACTIVITY_CLEAR_TOP");
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        }

                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent);
                        finish();
                        AnimNext();
                    } else {
                        ToastL("Error : " + mEmail.getError_message());
                    }
                } else {
                    ToastL("{ ERROR }");
                }

            } else {
                ToastL("{ ERROR }");
            }
        }

        @Override
        public void tokenError(String tokenError) {
            // TODO Auto-generated method stub

        }
    };

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), WalkThrough.class));
        this.finish();
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
    private void LogR(String msg) {
        Log.wtf(tag, msg);
    }

    /**
     * @param text
     */
    private void ToastL(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    /*******************************************************************/
}
