/**
 * ***Copyright(c)	:	2014-Present, VenomVendor.***
 * Author			:	VenomVendor
 * Dated			:	17 Feb, 2014 3:28:57 AM
 * Project			:	LenDen-Android
 * Client			:	LenDen
 * Contact			:	info@VenomVendor.com
 * URL				:	https://www.google.co.in/search?q=VenomVendor
 * Copyright(c)		:	2014-Present, VenomVendor.
 * License			:	This work is licensed under Attribution-NonCommercial 3.0 Unported
 *						License info at http://creativecommons.org/licenses/by-nc/3.0/deed.en_US
 *						Read More at http://creativecommons.org/licenses/by-nc/3.0/legalcode
 **/

package vee.HexWhale.LenDen;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import vee.HexWhale.LenDen.Parsers.AccessToken.GetAccessToken;
import vee.HexWhale.LenDen.Parsers.Profile.ForgotPassword;
import vee.HexWhale.LenDen.Storage.GlobalSharedPrefs;
import vee.HexWhale.LenDen.Storage.SettersNGetters;
import vee.HexWhale.LenDen.Utils.Constants.API.STRING;
import vee.HexWhale.LenDen.Utils.Constants.API.TYPE;
import vee.HexWhale.LenDen.Utils.Constants.API.URL;
import vee.HexWhale.LenDen.Utils.Constants.KEY;
import vee.HexWhale.LenDen.Utils.Constants.LOGIN_TYPE;
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
    EditText mEmail, mPassword;
    GetDataFromUrl mDataFromUrl;
    int type = -1;
    GlobalSharedPrefs mPrefs;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    String tempEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        tag = TagGen.getTag(this.getClass());
        super.onCreate(savedInstanceState);
        mPrefs = new GlobalSharedPrefs(this);
        this.setContentView(R.layout.login);
        mDataFromUrl = new GetDataFromUrl(this, mFetcherListener);
        mEmail = (EditText) findViewById(R.id.login_name);
        mPassword = (EditText) findViewById(R.id.login_psw);

        mEmail.setText("na@naa.com");
        mPassword.setText("qwerty");

    }

    public void Signin(View v) {
        validate();
    }

    private void validate() {
        if (!Validator.hasMinChars(mEmail, 4).equals("k")) {
            mEmail.setError(Validator.hasMinChars(mEmail, 4));
            return;
        }

        if (!Validator.hasMinChars(mPassword, 6).equals("k")) {
            mPassword.setError(Validator.hasMinChars(mPassword, 4));
            return;
        }

        SigninI();

    }

    public void FrgotPassword(View v)
    {

        builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Forgot Password ?");
        final EditText input = new EditText(this);
        input.setHint("Enter your E-Mail");
        input.setMinHeight(75);
        input.setMinimumHeight(75);
        input.setPadding(18, 10, 18, 10);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        builder.setView(input);

        input.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null)
                {
                    if (Validator.isvalidEmail(input).equalsIgnoreCase("k"))
                    {
                        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
                    }
                    else {
                        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                    }
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {

                if (input != null)
                {
                    goOutSideWithEmail(input.getText().toString().trim());
                }
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        dialog = builder.create();
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);

    }

    protected void goOutSideWithEmail(String email) {

        tempEmail = email;

        mDataFromUrl.setAccessToken();
        mDataFromUrl.GetString(TYPE.FORGOT_PASSWORD, getBody(TYPE.FORGOT_PASSWORD), GetData.getUrl(URL.FORGOT_PASSWORD));
    }

    public void SignUp(View v)
    {
        startActivity(new Intent(getApplicationContext(), SignUp.class));
        AnimNext();

    }

    private void SigninI() {
        if (!NetworkConnection.isAvail(getApplicationContext())) {
            ToastL("No internet Connection");
            return;
        }
        mDataFromUrl.setAccessToken();
        mDataFromUrl.GetString(TYPE.LOGIN_EMAIL, getBody(TYPE.LOGIN_EMAIL), GetData.getUrl(URL.LOGIN_EMAIL));
    }

    private String getBody(int tokenType) {
        JSONObject mJsonObject = null;
        mJsonObject = new JSONObject();
        switch (tokenType) {
            case TYPE.LOGIN_EMAIL:
                try {
                    if (!mEmail.getText().toString().trim().equalsIgnoreCase(mPrefs.getStringInPref(KEY.MY_E_MAIL)))
                    {
                        mPrefs.setStringInPref(KEY.MY_E_MAIL, "");
                    }
                    mJsonObject.put(STRING.EMAIL, mEmail.getText().toString().trim());
                    mJsonObject.put(STRING.PASSWORD, mPassword.getText().toString().trim());
                    return mJsonObject.toString();
                }
                catch (final JSONException e) {
                    e.printStackTrace();
                }
                break;

            case TYPE.FORGOT_PASSWORD:
                try {
                    mJsonObject.put(STRING.EMAIL, tempEmail);
                    return mJsonObject.toString();
                }
                catch (final JSONException e) {
                    e.printStackTrace();
                }
                break;
        }

        return null;

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

            switch (typ) {
                case TYPE.LOGIN_EMAIL:

                    final GetAccessToken mEmail = SettersNGetters.isLoggedInViaEmail();

                    if (mEmail != null) {
                        if (mEmail.getStatus().equalsIgnoreCase(STRING.SUCCESS)) {

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

                            if (mPrefs.getIntInPref(KEY.LOGIN_TYPE) != LOGIN_TYPE.EMAIL)
                            {
                                mPrefs.setStringInPref(KEY.MY_F_NAME, "");
                                mPrefs.setStringInPref(KEY.MY_L_NAME, "");
                                mPrefs.setStringInPref(KEY.MY_E_MAIL, "");
                            }
                            mPrefs.setIntInPref(KEY.LOGIN_TYPE, LOGIN_TYPE.EMAIL);
                            startActivity(intent);
                            finish();
                            AnimNext();
                        }
                        else {
                            ToastL(STRING.ERROR + mEmail.getError_message());
                        }
                    }
                    else {
                        ToastL("{Unknown ERROR }");
                    }

                    break;

                case TYPE.FORGOT_PASSWORD:
                    final ForgotPassword mForgotPassword = SettersNGetters.getForgotPassword();

                    if (mForgotPassword == null)
                    {
                        ToastL("{ Unknown Error }");
                        return;
                    }
                    if (mForgotPassword.getStatus().equalsIgnoreCase(STRING.SUCCESS))
                    {
                        ToastL("New password sent to\n" + "{ " + tempEmail + " }");
                        return;
                    }
                    else {
                        ToastL(STRING.ERROR + mForgotPassword.getError_message());
                    }
                    break;
            }
        }

        @Override
        public void tokenError(String tokenError) {
            ToastL(tokenError);
        }
    };

    @Override
    public void onBackPressed() {
        this.startActivity(new Intent(getApplicationContext(), WalkThrough.class));
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
