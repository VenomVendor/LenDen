/**
 * ***Copyright(c) : 2014-Present, VenomVendor.***
 * Author : VenomVendor
 * Dated : 6 Mar, 2014 1:09:32 AM
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
import android.os.AsyncTask;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import vee.HexWhale.LenDen.Parsers.AccessToken.GetAccessToken;
import vee.HexWhale.LenDen.Parsers.AuthCode.GetAuthCode;
import vee.HexWhale.LenDen.Parsers.Login.IsValidUserEmail;
import vee.HexWhale.LenDen.Storage.SettersNGetters;
import vee.HexWhale.LenDen.Utils.Constants.API.TYPE;

public class StartBackgroundParsing extends AsyncTask<String, Integer, String> {
    Activity activity;
    int type;
    private static ObjectMapper objectMapper = new ObjectMapper();
    ParserListener mParserListener;

    /**
     * @param activity
     * @param type
     * @param mParserListener
     */
    public StartBackgroundParsing(Activity activity, int type, ParserListener mParserListener) {
        this.activity = activity;
        this.type = type;
        this.mParserListener = mParserListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        initObjectMappers();
    }

    @Override
    protected String doInBackground(final String... params) {
        parseString(params[0]);
        return null;
    }

    private void initObjectMappers() {
        objectMapper.setSerializationInclusion(Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // TODO
        // False
        return;
    }

    private void parseString(String mParams) {

        try {
            switch (type) {
                case TYPE.AUTHORIZE:
                    SettersNGetters.setAuthCode(objectMapper.readValue(mParams, GetAuthCode.class));
                    break;
                case TYPE.ISSUE:
                    SettersNGetters.setAccessToken(objectMapper.readValue(mParams, GetAccessToken.class));
                    break;
                case TYPE.REFRESH:
                    // SAME AS ABOVE RESULT< GET REFRESH TOKEN FROM AccessToken
                    SettersNGetters.setAccessToken(objectMapper.readValue(mParams, GetAccessToken.class));
                    break;
                case TYPE.LOGIN_EMAIL:
                    SettersNGetters.setValidUserEmail(objectMapper.readValue(mParams, IsValidUserEmail.class));
                    break;

            }
        }
        catch (final Exception e) {
            System.out.println("Exception");
            e.printStackTrace();
            SettersNGetters.setAuthCode(null);
            SettersNGetters.setAccessToken(null);
        }

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        mParserListener.finishedParsing(type);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }
}
