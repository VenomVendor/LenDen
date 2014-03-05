/**
 * ***Copyright(c) : 2014-Present, VenomVendor.***
 * Author : VenomVendor
 * Dated : 6 Mar, 2014 1:09:18 AM
 * Project : LenDen-Android
 * Client : LenDen
 * Contact : info@VenomVendor.com
 * URL : https://www.google.co.in/search?q=VenomVendor
 * Copyright(c) : 2014-Present, VenomVendor.
 * License : This work is licensed under Attribution-NonCommercial 3.0 Unported (CC BY-NC 3.0).
 * License info at http://creativecommons.org/licenses/by-nc/3.0/deed.en_US
 * Read More at http://creativecommons.org/licenses/by-nc/3.0/legalcode
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

import vee.HexWhale.LenDen.Storage.GlobalSharedPrefs;
import vee.HexWhale.LenDen.Utils.Constants.API.HEADERS;
import vee.HexWhale.LenDen.Utils.Constants.KEY;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class GetDataFromUrl {
    static RequestQueue mQueue;
    private String tag = "UNKNOWN";
    Activity activity;
    int type = -1;
    boolean cancelOldRequests = true;
    FetcherListener mFetcherListener;
    GlobalSharedPrefs mPrefs;
    boolean accessToken = false;

    /**
     * @param activity
     * @param mFetcherListener
     */
    public GetDataFromUrl(Activity activity, FetcherListener mFetcherListener) {
        this.activity = activity;
        tag = TagGen.getTag(this.getClass());
        mQueue = Volley.newRequestQueue(activity);
        mPrefs = new GlobalSharedPrefs(activity);
        this.mFetcherListener = mFetcherListener;
    }

    /**
     * @param type
     * @param body
     * @param url
     */
    public void GetString(int type, String body, String url) {
        this.type = type;
        mQueue.add(getJSONRequest(url, body));
    }

    /**
     * @param type
     * @param body
     * @param url
     * @param cancelOldRequests
     *            ..?
     */
    public void GetString(int type, String body, String url, boolean cancelOldRequests) {
        this.type = type;
        this.cancelOldRequests = cancelOldRequests;
        mQueue.add(getJSONRequest(url, body));
    }

    /**
     * @param mURL
     * @param body
     * @return JsonRequest
     */
    private Request<?> getJSONRequest(String mURL, String body) {

        if (this.cancelOldRequests) {
            mQueue.cancelAll(this.activity);
            mQueue.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    LogB("Cancelled all");
                    return true;
                }
            });
        }

        /**
         * JsonRequest
         */
        JsonRequest<String> mJsonRequest = new JsonRequest<String>(Method.POST, mURL, body, successListener, errorListener) {

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String parsed;
                try {
                    parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                }
                catch (UnsupportedEncodingException e) {
                    parsed = new String(response.data);
                }
                return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();

                if (accessToken) {
                    GetDataFromUrl.this.accessToken = false;
                    params.put(HEADERS.ACCESS_TOKEN, mPrefs.getStringInPref(KEY.ACCESS_TOKEN));
                }
                params.put(HEADERS.CONTENT_TYPE, HEADERS.JSON);

                return params;
            }

        };
        return mJsonRequest;
    }

    private ErrorListener errorListener = new ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            mFetcherListener.errorFetching(type, error);
        }
    };

    private Listener<String> successListener = new Listener<String>() {

        @Override
        public void onResponse(String response) {
            mFetcherListener.finishedFetching(type, response);
        }
    };

    /**
     * @param Blue
     */
    private void LogB(String msg) {
        Log.d(tag, msg);
    }

    public void setAccessToken() {
        this.accessToken = true;
    }

    /*******************************************************************/
}
