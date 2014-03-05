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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import vee.HexWhale.LenDen.Parsers.Login.IsValidUserEmail;
import vee.HexWhale.LenDen.Storage.SettersNGetters;
import vee.HexWhale.LenDen.Utils.Constants.API.STRING;
import vee.HexWhale.LenDen.Utils.Constants.API.TYPE;
import vee.HexWhale.LenDen.Utils.Constants.API.URL;
import vee.HexWhale.LenDen.bg.Threads.FetcherListener;
import vee.HexWhale.LenDen.bg.Threads.GetDataFromUrl;
/***************************************************************/
import vee.HexWhale.LenDen.bg.Threads.NetworkConnection;
import vee.HexWhale.LenDen.bg.Threads.ParserListener;
import vee.HexWhale.LenDen.bg.Threads.StartBackgroundParsing;
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
        tag = TagGen.getTag(this.getClass());
        super.onCreate(arg0);
        setContentView(R.layout.login);
        mDataFromUrl = new GetDataFromUrl(this, mFetcherListener);
        mUserName = (EditText) findViewById(R.id.login_name);
        mPassword = (EditText) findViewById(R.id.login_psw);

    }

    public void Signin(View v) {
        validate();
    }

    protected void setType(int type) {
        this.type = type;
    }

    protected void StartParsing(String response) {
        new StartBackgroundParsing(this, this.type, mParserListener).execute(response);
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
            mJsonObject.put(STRING.EMAIL, mUserName.getText().toString().trim());
            mJsonObject.put(STRING.PASSWORD, mPassword.getText().toString().trim());
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
            setType(type);
            ToastL("response " + response);
            LogR("+++" + response.toString());
            if (type == TYPE.LOGIN_EMAIL) {
                StartParsing(response);
            }
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
    };

    private ParserListener mParserListener = new ParserListener() {

        @Override
        public void finishedParsing(int type) {
            if (type == TYPE.LOGIN_EMAIL) {
                IsValidUserEmail mEmail = SettersNGetters.getValidUserEmail();

                if (mEmail != null) {
                    if (mEmail.getStatus().equalsIgnoreCase("success")) {
                        startActivity(new Intent(getApplicationContext(), Home.class));
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
    };

    @Override
    public void onBackPressed() {
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
