/**
 * ***Copyright(c) : 2014-Present, VenomVendor.***
 * Author : VenomVendor
 * Dated : 6 Mar, 2014 1:09:32 AM
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

package vee.HexWhale.LenDen.bg.Threads;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Locale;

import vee.HexWhale.LenDen.Parsers.AccessToken.GetAccessToken;
import vee.HexWhale.LenDen.Parsers.AuthCode.GetAuthCode;
import vee.HexWhale.LenDen.Parsers.Categories.GetCategory;
import vee.HexWhale.LenDen.Storage.SettersNGetters;
import vee.HexWhale.LenDen.Utils.Constants.API.STRING;
import vee.HexWhale.LenDen.Utils.Constants.API.TYPE;

@SuppressLint("DefaultLocale")
public class StartBackgroundParsing extends AsyncTask<String, Integer, String> {
    Activity activity;
    int type;
    private static ObjectMapper objectMapper = new ObjectMapper();
    FetcherListener mFetcherListener;
    GetTokens mTokens;

    /**
     * @param activity
     * @param type
     * @param mParserListener
     */
    public StartBackgroundParsing(Activity activity, int type, FetcherListener mFetcherListener) {
        this.activity = activity;
        this.type = type;
        this.mFetcherListener = mFetcherListener;
        this.mTokens = new GetTokens(activity, mFetcherListener);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.mFetcherListener.beforeParsing(this.type);
        this.initObjectMappers();
    }

    @Override
    protected String doInBackground(final String... params) {
        this.parseString(params[0]);
        return null;
    }

    private void initObjectMappers() {
        StartBackgroundParsing.objectMapper.setSerializationInclusion(Include.NON_NULL);
        StartBackgroundParsing.objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        StartBackgroundParsing.objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        StartBackgroundParsing.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // TODO
        // False
        return;
    }

    private void parseString(String mParams) {
        this.mFetcherListener.startedParsing(this.type);
        try {
            switch (this.type) {
                case TYPE.AUTHORIZE:
                    SettersNGetters.setAuthCode(StartBackgroundParsing.objectMapper.readValue(mParams, GetAuthCode.class));
                    if (SettersNGetters.getAuthCode().getStatus().equalsIgnoreCase(STRING.SUCCESS))
                    {
                        this.mTokens.getAccessToken();
                    }
                    else {
                        SettersNGetters.setAuthCode(null);
                        mFetcherListener.tokenError("Error in Server");
                    }
                    break;
                case TYPE.ACCESSTOKEN:
                    SettersNGetters.setAccessToken(StartBackgroundParsing.objectMapper.readValue(mParams, GetAccessToken.class));
                    if (!SettersNGetters.getAccessToken().getStatus().equalsIgnoreCase(STRING.SUCCESS))
                    {
                        SettersNGetters.setAccessToken(null);
                        mFetcherListener.tokenError("Error in Server");
                    }

                    break;
                case TYPE.REFRESH:
                    // SAME AS GETACCESSTOKEN < GET REFRESH TOKEN FROM
                    // AccessToken
                    SettersNGetters.setAccessToken(StartBackgroundParsing.objectMapper.readValue(mParams, GetAccessToken.class));
                    if (!SettersNGetters.getAccessToken().getStatus().equalsIgnoreCase(STRING.SUCCESS))
                    {
                        SettersNGetters.setAccessToken(null);
                        mFetcherListener.tokenError("Error in Server");
                    }
                    break;
                case TYPE.LOGIN_EMAIL:
                    // SAME AS GETACCESSTOKEN RESULT < GET LOGIN PARAMS FROM
                    // AccessToken
                    SettersNGetters.setLoggedInViaEmail(StartBackgroundParsing.objectMapper.readValue(mParams, GetAccessToken.class));
                    break;
                case TYPE.REGISTER_EMAIL:
                    // SAME AS GETACCESSTOKEN RESULT < GET REGISTER PARAMS FROM
                    // AccessToken
                    SettersNGetters.setRegistered(StartBackgroundParsing.objectMapper.readValue(mParams, GetAccessToken.class));
                    break;

                case TYPE.CATEGORIES:
                    SettersNGetters.setCategory(StartBackgroundParsing.objectMapper.readValue(mParams, GetCategory.class));

                    if (SettersNGetters.getCategory().getError_code() != null)
                    {
                        if (SettersNGetters.getCategory().getError_code().toLowerCase(Locale.ENGLISH).contains("token"))
                        {
                            mTokens.refreshToken();
                        }
                    }

                    break;
            }
        }
        catch (final Exception e) {
            this.mFetcherListener.ParsingException(e);
            System.out.println("Exception");
            e.printStackTrace();

            SettersNGetters.setAuthCode(null);
            SettersNGetters.setAccessToken(null);
            SettersNGetters.setLoggedInViaEmail(null);
            SettersNGetters.setRegistered(null);
            SettersNGetters.setCategory(null);
        }

        return;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        this.mFetcherListener.finishedParsing(this.type);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }
}
