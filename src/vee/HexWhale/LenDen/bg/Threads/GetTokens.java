/**
 * ***Copyright(c)	:	2014-Present, VenomVendor.***
 * Author		:	VenomVendor
 * Dated		:	9 Mar, 2014 1:25:55 PM
 * Project		:	LenDen-Android
 * Client		:	LenDen
 * Contact		:	info@VenomVendor.com
 * URL			:	https://www.google.co.in/search?q=VenomVendor
 * Copyright(c)	:	2014-Present, VenomVendor.
 * License		:	This work is licensed under Attribution-NonCommercial 3.0 Unported (CC BY-NC 3.0).
 *					License info at http://creativecommons.org/licenses/by-nc/3.0/deed.en_US
 *					Read More at http://creativecommons.org/licenses/by-nc/3.0/legalcode
 **/

package vee.HexWhale.LenDen.bg.Threads;

import android.app.Activity;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import vee.HexWhale.LenDen.Parsers.AccessToken.GetAccessToken;
import vee.HexWhale.LenDen.Parsers.AuthCode.GetAuthCode;
import vee.HexWhale.LenDen.Storage.GlobalSharedPrefs;
import vee.HexWhale.LenDen.Storage.SettersNGetters;
import vee.HexWhale.LenDen.Utils.Constants.API;
import vee.HexWhale.LenDen.Utils.Constants.API.HEADERS;
import vee.HexWhale.LenDen.Utils.Constants.API.STRING;
import vee.HexWhale.LenDen.Utils.Constants.API.TYPE;
import vee.HexWhale.LenDen.Utils.Constants.API.URL;
import vee.HexWhale.LenDen.Utils.Constants.KEY;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class GetTokens {
    Activity activity;
    GetAuthCode mAuthCode;
    GetAccessToken mAccessToken;
    GlobalSharedPrefs mPrefs;
    GetDataFromUrl mDataFromUrl;
    static RequestQueue mQueue;
    private String tag = "UNKNOWN";
    private static ObjectMapper objectMapper = new ObjectMapper();
    FetcherListener mFetcherListener;
    int type = -1;

    public GetTokens(Activity activity, FetcherListener mFetcherListener) {
        this.activity = activity;
        this.tag = TagGen.getTag(this.getClass());
        GetTokens.mQueue = Volley.newRequestQueue(activity);
        this.mPrefs = new GlobalSharedPrefs(activity);
        this.mFetcherListener = mFetcherListener;
        this.initObjectMappers();
    }

    public void getAuthToken() {

        this.type = TYPE.AUTHORIZE;
        GetTokens.mQueue.add(this.getJSONRequest(this.getBody(TYPE.AUTHORIZE),GetData.getUrl(URL.AUTHORIZE)));
        this.LogW("" + this.type);
    }

    private void LogW(String msg) {
        Log.wtf(this.tag, msg);
    }

    public void getAccessToken() {
        this.type = TYPE.ACCESSTOKEN;
        GetTokens.mQueue.add(this.getJSONRequest(this.getBody(TYPE.ACCESSTOKEN),GetData.getUrl(URL.ACCESSTOKEN)));
        this.LogW("" + this.type);
    }

    public void refreshToken() {
        this.type = TYPE.REFRESH;
        GetTokens.mQueue.add(this.getJSONRequest(this.getBody(TYPE.REFRESH),GetData.getUrl(URL.REFRESH)));
        this.LogW("" + this.type);
    }

    private void initObjectMappers() {
        GetTokens.objectMapper.setSerializationInclusion(Include.NON_NULL);
        GetTokens.objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        GetTokens.objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        GetTokens.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // TODO
        // False
        return;
    }

    public Listener<String> response = new Listener<String>() {
        @Override
        public void onResponse(String response) {

            GetTokens.this.LogW("+++" + response);
            try {
                switch (GetTokens.this.type) {
                    case TYPE.AUTHORIZE:
                        SettersNGetters.setAuthCode(GetTokens.objectMapper.readValue(response, GetAuthCode.class));
                        GetTokens.this.mAuthCode = SettersNGetters.getAuthCode();
                        if (GetTokens.this.mAuthCode != null) {
                            if (GetTokens.this.mAuthCode.getStatus().equalsIgnoreCase(STRING.SUCCESS)) {
                                GetTokens.this.mPrefs.setStringInPref(KEY.AUTH_CODE, GetTokens.this.mAuthCode.getResponse().getAuth_code());
                                GetTokens.this.mFetcherListener.finishedParsing(GetTokens.this.type);
                                GetTokens.this.getAccessToken(); //XXX
                            } else {
                                GetTokens.this.mFetcherListener.tokenError(GetTokens.this.mAuthCode.getError_message());
                            }
                        } else {
                            GetTokens.this.mFetcherListener.tokenError("Error in Server");
                        }
                        return;
                    case TYPE.ACCESSTOKEN:

                        GetTokens.this.mFetcherListener.beforeParsing(0); // USED AS ALTERNATIVE

                        SettersNGetters.setAccessToken(GetTokens.objectMapper.readValue(response, GetAccessToken.class));
                        GetTokens.this.mAccessToken = SettersNGetters.getAccessToken();
                        if (GetTokens.this.mAccessToken != null) {
                            if (GetTokens.this.mAccessToken.getStatus().equalsIgnoreCase("success")) {
                                GetTokens.this.mPrefs.setStringInPref(KEY.ACCESS_TOKEN, GetTokens.this.mAccessToken.getResponse().getAccess_token());
                                GetTokens.this.mPrefs.setStringInPref(KEY.REFRESH_TOKEN, GetTokens.this.mAccessToken.getResponse().getRefresh_token());
                                GetTokens.this.mFetcherListener.finishedParsing(GetTokens.this.type);
                            } else {
                                GetTokens.this.mFetcherListener.tokenError(GetTokens.this.mAuthCode.getError_message());
                            }
                        } else {
                            GetTokens.this.mFetcherListener.tokenError("Error in Server");
                        }

                        return;
                    case TYPE.REFRESH:
                        // SAME AS GETACCESSTOKEN < GET REFRESH TOKEN FROM AccessToken
                        SettersNGetters.setAccessToken(GetTokens.objectMapper.readValue(response, GetAccessToken.class));
                        GetTokens.this.mAccessToken = SettersNGetters.getAccessToken();

                        if (GetTokens.this.mAccessToken != null) {
                            if (GetTokens.this.mAccessToken.getStatus().equalsIgnoreCase("success")) {
                                GetTokens.this.mPrefs.setStringInPref(KEY.ACCESS_TOKEN, GetTokens.this.mAccessToken.getResponse().getAccess_token());
                                GetTokens.this.mFetcherListener.finishedParsing(GetTokens.this.type);
                            } else {
                                GetTokens.this.getAuthToken(); //XXX
                                GetTokens.this.LogW(GetTokens.this.mAuthCode.getError_message());
                            }
                        } else {
                            GetTokens.this.mFetcherListener.tokenError("Error in Server");
                        }
                        return;

                    default:
                        GetTokens.this.mFetcherListener.tokenError("Invalid Request");
                        return;
                }
            }
            catch (final Exception e) {
                GetTokens.this.mFetcherListener.ParsingException(e);
            }
        }

    };

    public ErrorListener errorListener = new ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            GetTokens.this.mFetcherListener.errorFetching(GetTokens.this.type, error);
        }

    };

    /**
     * @param mURL
     * @param body
     * @return JsonRequest
     */

    private Request<?> getJSONRequest(String body, String mURL) {
        /**
         * JsonRequest
         */
        final JsonRequest<String> mJsonRequest = new JsonRequest<String>(Method.POST, mURL, body, this.response, this.errorListener) {

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String parsed;
                try {
                    parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                }
                catch (final UnsupportedEncodingException e) {
                    parsed = new String(response.data);
                }
                return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final HashMap<String, String> params = new HashMap<String, String>();
                params.put(HEADERS.CONTENT_TYPE, HEADERS.JSON);
                return params;
            }

        };

        return mJsonRequest;
    }

    private String getBody(int mType) {
        JSONObject mJsonObject = null;
        mJsonObject = new JSONObject();
        try {
            switch (mType) {
                case TYPE.AUTHORIZE:
                    System.out.println("AUTHORIZE " + TYPE.AUTHORIZE);
                    mJsonObject.put(STRING.SECRET, API.SECRET);
                    mJsonObject.put(STRING.KEY, API.KEY);
                    break;
                case TYPE.ACCESSTOKEN:
                    System.out.println("ACCESSTOKEN " + TYPE.ACCESSTOKEN);
                    mJsonObject.put(STRING.AUTH, this.mPrefs.getStringInPref(KEY.AUTH_CODE));
                    break;
                case TYPE.REFRESH:
                    System.out.println("REFRESH " + TYPE.REFRESH);
                    mJsonObject.put(STRING.AUTH, this.mPrefs.getStringInPref(KEY.AUTH_CODE));
                    mJsonObject.put(STRING.REFRESH, this.mPrefs.getStringInPref(KEY.REFRESH_TOKEN));
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

}
