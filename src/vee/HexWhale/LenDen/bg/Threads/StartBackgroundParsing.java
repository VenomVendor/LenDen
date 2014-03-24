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

import vee.HexWhale.LenDen.Parsers.AccessToken.GetAccessToken;
import vee.HexWhale.LenDen.Parsers.AuthCode.GetAuthCode;
import vee.HexWhale.LenDen.Parsers.Categories.GetCategory;
import vee.HexWhale.LenDen.Parsers.DetailedCategory.GetDetailedCategory;
import vee.HexWhale.LenDen.Parsers.ItemCategory.GetItemCategory;
import vee.HexWhale.LenDen.Parsers.SearchCategory.GetSearchCategory;
import vee.HexWhale.LenDen.Storage.SettersNGetters;
import vee.HexWhale.LenDen.Utils.Constants.API.STRING;
import vee.HexWhale.LenDen.Utils.Constants.API.TYPE;

import java.util.Locale;

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
        mTokens = new GetTokens(activity, mFetcherListener);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mFetcherListener.beforeParsing(type);
        initObjectMappers();
    }

    @Override
    protected String doInBackground(final String... params) {
        parseString(params[0]);
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
        mFetcherListener.startedParsing(type);
        try {
            switch (type) {
                case TYPE.AUTHORIZE:
                    SettersNGetters.setAuthCode(StartBackgroundParsing.objectMapper.readValue(mParams, GetAuthCode.class));
                    if (SettersNGetters.getAuthCode().getStatus().equalsIgnoreCase(STRING.SUCCESS))
                    {
                        mTokens.getAccessToken();
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

                    validateToken(SettersNGetters.isLoggedInViaEmail().getError_code());
                    break;
                case TYPE.REGISTER_EMAIL:
                    // SAME AS GETACCESSTOKEN RESULT < GET REGISTER PARAMS FROM
                    // AccessToken
                    SettersNGetters.setRegistered(StartBackgroundParsing.objectMapper.readValue(mParams, GetAccessToken.class));

                    validateToken(SettersNGetters.isRegistered().getError_code());

                    break;

                case TYPE.CATEGORIES:
                    SettersNGetters.setCategory(StartBackgroundParsing.objectMapper.readValue(mParams, GetCategory.class));

                    validateToken(SettersNGetters.getCategory().getError_code());

                    break;

                case TYPE.ITEM_CATEGORIES:
                    SettersNGetters.setItemCategory(StartBackgroundParsing.objectMapper.readValue(mParams, GetItemCategory.class));

                    validateToken(SettersNGetters.getItemCategory().getError_code());

                    break;

                case TYPE.ITEM_DETAILS:
                    SettersNGetters.setDetailedCategory(StartBackgroundParsing.objectMapper.readValue(mParams, GetDetailedCategory.class));

                    validateToken(SettersNGetters.getDetailedCategory().getError_code());

                    break;

                case TYPE.ITEMS:
                    SettersNGetters.setSearchCategory(StartBackgroundParsing.objectMapper.readValue(mParams, GetSearchCategory.class));

                    validateToken(SettersNGetters.getSearchCategory().getError_code());

                    break;
            }
        }
        catch (final Exception e) {
            mFetcherListener.ParsingException(e);
            System.out.println("Exception");
            e.printStackTrace();

            SettersNGetters.setAuthCode(null);
            SettersNGetters.setAccessToken(null);
            SettersNGetters.setLoggedInViaEmail(null);
            SettersNGetters.setRegistered(null);
            SettersNGetters.setCategory(null);
            SettersNGetters.setItemCategory(null);
            SettersNGetters.setDetailedCategory(null);
            SettersNGetters.setSearchCategory(null);
        }

        return;
    }

    private void validateToken(String error_code) {
        if (error_code != null)
        {
            if (error_code.toLowerCase(Locale.ENGLISH).contains("token"))
            {
                mTokens.refreshToken();
            }
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        mFetcherListener.finishedParsing(type);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }
}
