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
import android.widget.Toast;

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
        this.tag = TagGen.getTag(this.getClass());
        GetDataFromUrl.mQueue = Volley.newRequestQueue(activity);
        this.mPrefs = new GlobalSharedPrefs(activity);
        this.mFetcherListener = mFetcherListener;
    }

    /**
     * @param type
     * @param body
     * @param url
     */
    public void GetString(int type, String body, String url) {
        this.type = type;
        GetDataFromUrl.mQueue.add(this.getJSONRequest(url, body));
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
        GetDataFromUrl.mQueue.add(this.getJSONRequest(url, body));
    }

    /**
     * @param mURL
     * @param body
     * @return JsonRequest
     */
    private Request<?> getJSONRequest(String mURL, String body) {

        this.LogO("``" + mURL + "``");
        this.LogB("`*`" + body + "`*`");

        if (this.cancelOldRequests) {
            GetDataFromUrl.mQueue.cancelAll(this.activity);
            GetDataFromUrl.mQueue.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    GetDataFromUrl.this.LogB("Cancelled all");
                    return true;
                }
            });
        }

        /**
         * JsonRequest
         */
        final JsonRequest<String> mJsonRequest = new JsonRequest<String>(Method.POST, mURL, body, this.successListener, this.errorListener) {

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

                if (GetDataFromUrl.this.accessToken) {
                    GetDataFromUrl.this.accessToken = false;
                    params.put(HEADERS.ACCESS_TOKEN, GetDataFromUrl.this.mPrefs.getStringInPref(KEY.ACCESS_TOKEN));
                }
                params.put(HEADERS.CONTENT_TYPE, HEADERS.JSON);

                GetDataFromUrl.this.LogO(params.toString());
                return params;
            }

        };
        return mJsonRequest;
    }

    private final ErrorListener errorListener = new ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            GetDataFromUrl.this.mFetcherListener.errorFetching(GetDataFromUrl.this.type, error);
        }
    };

    private final Listener<String> successListener = new Listener<String>() {

        @Override
        public void onResponse(String response) {
            GetDataFromUrl.this.mFetcherListener.finishedFetching(GetDataFromUrl.this.type, response);
            new StartBackgroundParsing(GetDataFromUrl.this.activity, GetDataFromUrl.this.type, GetDataFromUrl.this.mFetcherListener).execute(response);
        }
    };

    /**
     * @param Blue
     */
    private void LogB(String msg) {
        Log.d(this.tag, msg);
    }

    /**
     * @param Orange
     */
    public void LogO(String msg) {
        Log.v(this.tag, msg);
    }

    public void setAccessToken() {
        this.accessToken = true;
    }

    /**
     * @param text
     */
    private void ToastL(String text) {
        Toast.makeText(this.activity.getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    /*******************************************************************/
}
