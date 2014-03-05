/**
 * ***Copyright(c) : 2014-Present, VenomVendor.***
 * Author : VenomVendor
 * Dated : 17 Feb, 2014 3:29:01 AM
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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import vee.HexWhale.LenDen.Parsers.AccessToken.GetAccessToken;
import vee.HexWhale.LenDen.Parsers.AuthCode.GetAuthCode;
import vee.HexWhale.LenDen.Storage.GlobalSharedPrefs;
import vee.HexWhale.LenDen.Storage.SettersNGetters;
import vee.HexWhale.LenDen.Utils.Constants.API;
import vee.HexWhale.LenDen.Utils.Constants.API.STRING;
import vee.HexWhale.LenDen.Utils.Constants.API.TYPE;
import vee.HexWhale.LenDen.Utils.Constants.API.URL;
import vee.HexWhale.LenDen.Utils.Constants.KEY;
import vee.HexWhale.LenDen.bg.Threads.FetcherListener;
import vee.HexWhale.LenDen.bg.Threads.GetDataFromUrl;
import vee.HexWhale.LenDen.bg.Threads.ParserListener;
import vee.HexWhale.LenDen.bg.Threads.StartBackgroundParsing;
import vee.HexWhale.LenDen.bg.Threads.TagGen;

public class Splash extends Activity {

    private String tag = "UNKNOWN";
    int type = -1;
    GetAuthCode mAuthCode;
    GetAccessToken mAccessToken;
    GlobalSharedPrefs mPrefs;
    GetDataFromUrl mDataFromUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        tag = TagGen.getTag(this.getClass());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        mPrefs = new GlobalSharedPrefs(this);
        mDataFromUrl = new GetDataFromUrl(this, mFetcherListener);

        if (mPrefs.getStringInPref(KEY.AUTH_CODE) == null) {

            System.out.println("GetAuth");

            mDataFromUrl.GetString(TYPE.AUTHORIZE, getBody(TYPE.AUTHORIZE), getUrl(URL.AUTHORIZE));

        } else

        if (mPrefs.getStringInPref(KEY.ACCESS_TOKEN) == null) {

            System.out.println("FetchToken");

            mDataFromUrl.GetString(TYPE.ISSUE, getBody(TYPE.ISSUE), getUrl(URL.ISSUE));

        } else {

            System.out.println("RefreshToken");

            mDataFromUrl.GetString(TYPE.REFRESH, getBody(TYPE.REFRESH), getUrl(URL.REFRESH));
        }

    }

    private FetcherListener mFetcherListener = new FetcherListener() {

        @Override
        public void finishedFetching(int type, String response) {
            setType(type);
            // ToastL("response " + response);
            LogR("+++" + response.toString());
            StartParsing(response);
        }

        @Override
        public void errorFetching(int type, VolleyError error) {
            Splash.this.type = type;
            try {
                error.printStackTrace();
                LogR("---" + "Error");
            }
            finally {
                ToastL("Error");
            }

        }
    };

    private String getBody(int mType) {

        JSONObject mJsonObject = null;
        mJsonObject = new JSONObject();

        try {
            switch (type) {
                case TYPE.AUTHORIZE:
                    mJsonObject.put(STRING.SECRET, API.SECRET);
                    mJsonObject.put(STRING.KEY, API.KEY);
                    break;
                case TYPE.ISSUE:
                    mJsonObject.put(STRING.AUTH, mPrefs.getStringInPref(KEY.AUTH_CODE));
                    break;
                case TYPE.REFRESH:
                    mJsonObject.put(STRING.AUTH, mPrefs.getStringInPref(KEY.AUTH_CODE));
                    mJsonObject.put(STRING.REFRESH, mPrefs.getStringInPref(KEY.REFRESH_TOKEN));
                    break;
            }

            System.out.println(mJsonObject.toString());
            return mJsonObject.toString();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    protected void setType(int type) {
        this.type = type;
    }

    protected void StartParsing(String response) {
        new StartBackgroundParsing(this, this.type, mParserListener).execute(response);
    }

    /*******************************************************************/

    private ParserListener mParserListener = new ParserListener() {

        @Override
        public void finishedParsing(int typ) {

            switch (typ) {
                case TYPE.AUTHORIZE:
                    mAuthCode = SettersNGetters.getAuthCode();
                    if (mAuthCode != null) {
                        if (mAuthCode.getStatus().equalsIgnoreCase("success")) {
                            mPrefs.setStringInPref(KEY.AUTH_CODE, mAuthCode.getResponse().getAuth_code());
                            ToastL("mAuthCode " + mPrefs.getStringInPref(KEY.AUTH_CODE));
                            mDataFromUrl.GetString(TYPE.ISSUE, getBody(TYPE.ISSUE), getUrl(URL.ISSUE));
                        } else {
                            ToastL("Error : " + mAuthCode.getError_message());
                        }
                    } else {
                        ToastL("{ ERROR }");
                    }
                    break;

                case TYPE.ISSUE:
                    mAccessToken = SettersNGetters.getAccessToken();

                    if (mAccessToken != null) {
                        if (mAccessToken.getStatus().equalsIgnoreCase("success")) {

                            mPrefs.setStringInPref(KEY.ACCESS_TOKEN, mAccessToken.getResponse().getAccess_token());
                            mPrefs.setStringInPref(KEY.REFRESH_TOKEN, mAccessToken.getResponse().getRefresh_token());

                            ToastL("AccessToken " + mPrefs.getStringInPref(KEY.ACCESS_TOKEN));
                            ToastL("AccessToken's RefreshToken " + mPrefs.getStringInPref(KEY.REFRESH_TOKEN));

                            LogBlk("AccessToken " + mAccessToken.getResponse().getAccess_token());
                            LogBlk("AccessToken " + mAccessToken.getResponse().getRefresh_token());
                            mDataFromUrl.GetString(TYPE.REFRESH, getBody(TYPE.REFRESH), getUrl(URL.REFRESH)); // XXX
                                                                                                              // REMOVE-THIS
                            // startNextActivity(); //REMOVE COMMENTED
                        } else {
                            ToastL("Error : " + mAccessToken.getError_message());
                        }
                    } else {
                        ToastL("{ ERROR }");
                    }

                case TYPE.REFRESH:
                    mAccessToken = SettersNGetters.getAccessToken();

                    if (mAccessToken != null) {
                        if (mAccessToken.getStatus().equalsIgnoreCase("success")) {

                            mPrefs.setStringInPref(KEY.ACCESS_TOKEN, mAccessToken.getResponse().getAccess_token());
                            mPrefs.setStringInPref(KEY.REFRESH_TOKEN, mAccessToken.getResponse().getRefresh_token());

                            ToastL("RefreshToken's AccessToken " + mPrefs.getStringInPref(KEY.ACCESS_TOKEN));
                            ToastL("RefreshToken " + mPrefs.getStringInPref(KEY.REFRESH_TOKEN));

                            LogBlk("RefreshToken " + mAccessToken.getResponse().getAccess_token());
                            LogBlk("RefreshToken " + mAccessToken.getResponse().getRefresh_token());

                            startNextActivity();

                        } else {
                            ToastL("Error : " + mAccessToken.getError_message());
                        }
                    } else {
                        ToastL("{ ERROR }");
                    }

                default:
                    break;
            }
        }
    };

    private void AnimNext() {
        overridePendingTransition(R.anim.enter, R.anim.exit);
        return;
    }

    protected void startNextActivity() {
        startActivity(new Intent(getApplicationContext(), WalkThrough.class));
        finish();
        AnimNext();
    }

    private String getUrl(String tokenType) {
        Log.w(tag, BASE_URL + tokenType);
        return (BASE_URL + tokenType);
    }

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

    /*******************************************************************/
}
