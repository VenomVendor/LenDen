/**
 * ***Copyright(c)	:	2014-Present, VenomVendor.***
 * Author			:	VenomVendor
 * Dated			:	6 Mar, 2014 1:09:32 AM
 * Project			:	LenDen-Android
 * Client			:	LenDen
 * Contact			:	info@VenomVendor.com
 * URL				:	https://www.google.co.in/search?q=VenomVendor
 * Copyright(c)		:	2014-Present, VenomVendor.
 * License			:	This work is licensed under Attribution-NonCommercial 3.0 Unported
 *						License info at http://creativecommons.org/licenses/by-nc/3.0/deed.en_US
 *						Read More at http://creativecommons.org/licenses/by-nc/3.0/legalcode
 *


 **/

package vee.HexWhale.LenDen.bg.Threads;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import vee.HexWhale.LenDen.Parsers.AccessToken.GetAccessToken;
import vee.HexWhale.LenDen.Parsers.AddItems.GetAddItems;
import vee.HexWhale.LenDen.Parsers.AuthCode.GetAuthCode;
import vee.HexWhale.LenDen.Parsers.Categories.GetCategory;
import vee.HexWhale.LenDen.Parsers.DetailedCategory.GetDetailedCategory;
import vee.HexWhale.LenDen.Parsers.FavCategory.GetFavCategory;
import vee.HexWhale.LenDen.Parsers.ItemCategory.GetItemCategory;
import vee.HexWhale.LenDen.Parsers.ItemStats.GetItemStats;
import vee.HexWhale.LenDen.Parsers.Login.FBRegLogin;
import vee.HexWhale.LenDen.Parsers.Messages.CreateMessage;
import vee.HexWhale.LenDen.Parsers.Messages.GetMessages;
import vee.HexWhale.LenDen.Parsers.MessagesFull.GetMessagesFull;
import vee.HexWhale.LenDen.Parsers.Profile.ChangePassword;
import vee.HexWhale.LenDen.Parsers.Profile.ForgotPassword;
import vee.HexWhale.LenDen.Parsers.Profile.GetEditProfile;
import vee.HexWhale.LenDen.Parsers.Profile.GetProfile;
import vee.HexWhale.LenDen.Parsers.ProfileItems.GetProfileItems;
import vee.HexWhale.LenDen.Parsers.SearchCategory.GetSearchCategory;
import vee.HexWhale.LenDen.Parsers.SubCategories.GetSubCategory;
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

    private void parseString(String resultJsonString) {
        mFetcherListener.startedParsing(type);
        try {
            switch (type) {
                case TYPE.AUTHORIZE:
                    SettersNGetters.setAuthCode(StartBackgroundParsing.objectMapper.readValue(resultJsonString, GetAuthCode.class));
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
                    SettersNGetters.setAccessToken(StartBackgroundParsing.objectMapper.readValue(resultJsonString, GetAccessToken.class));
                    if (!SettersNGetters.getAccessToken().getStatus().equalsIgnoreCase(STRING.SUCCESS))
                    {
                        SettersNGetters.setAccessToken(null);
                        mFetcherListener.tokenError("Error in Server");
                    }

                    break;
                case TYPE.REFRESH:
                    // SAME AS GETACCESSTOKEN < GET REFRESH TOKEN FROM
                    // AccessToken
                    SettersNGetters.setAccessToken(StartBackgroundParsing.objectMapper.readValue(resultJsonString, GetAccessToken.class));
                    if (!SettersNGetters.getAccessToken().getStatus().equalsIgnoreCase(STRING.SUCCESS))
                    {
                        SettersNGetters.setAccessToken(null);
                        mFetcherListener.tokenError("Error in Server");
                    }
                    break;
                case TYPE.LOGIN_EMAIL:
                    // SAME AS GETACCESSTOKEN RESULT < GET LOGIN PARAMS FROM
                    // AccessToken
                    SettersNGetters.setLoggedInViaEmail(StartBackgroundParsing.objectMapper.readValue(resultJsonString, GetAccessToken.class));

                    validateToken(SettersNGetters.isLoggedInViaEmail().getError_code());
                    break;
                case TYPE.REGISTER_EMAIL:
                    // SAME AS GETACCESSTOKEN RESULT < GET REGISTER PARAMS FROM
                    // AccessToken
                    SettersNGetters.setRegistered(StartBackgroundParsing.objectMapper.readValue(resultJsonString, GetAccessToken.class));

                    validateToken(SettersNGetters.isRegistered().getError_code());

                    break;

                case TYPE.CATEGORIES:
                    SettersNGetters.setCategory(StartBackgroundParsing.objectMapper.readValue(resultJsonString, GetCategory.class));

                    validateToken(SettersNGetters.getCategory().getError_code());

                    break;

                case TYPE.SUB_CATEGORIES:
                    SettersNGetters.setSubCategory(StartBackgroundParsing.objectMapper.readValue(resultJsonString, GetSubCategory.class));

                    validateToken(SettersNGetters.getSubCategory().getError_code());

                    break;

                case TYPE.ITEM_CATEGORIES:
                    SettersNGetters.setItemCategory(StartBackgroundParsing.objectMapper.readValue(resultJsonString, GetItemCategory.class));

                    validateToken(SettersNGetters.getItemCategory().getError_code());

                    break;

                case TYPE.ITEM_DETAILS:
                    SettersNGetters.setDetailedCategory(StartBackgroundParsing.objectMapper.readValue(resultJsonString, GetDetailedCategory.class));

                    validateToken(SettersNGetters.getDetailedCategory().getError_code());

                    break;
                case TYPE.ITEMS:
                    SettersNGetters.setSearchCategory(StartBackgroundParsing.objectMapper.readValue(resultJsonString, GetSearchCategory.class));

                    validateToken(SettersNGetters.getSearchCategory().getError_code());

                    break;

                case TYPE.FAVORITE:
                    SettersNGetters.setFavCategory(StartBackgroundParsing.objectMapper.readValue(resultJsonString, GetFavCategory.class));

                    validateToken(SettersNGetters.getFavCategory().getError_code());

                    break;

                case TYPE.MESSSAGES:
                    SettersNGetters.setMessages(StartBackgroundParsing.objectMapper.readValue(resultJsonString, GetMessages.class));

                    validateToken(SettersNGetters.getMessages().getError_code());

                    break;

                case TYPE.MESSSAGES_USER:
                    SettersNGetters.setMessagesFull(StartBackgroundParsing.objectMapper.readValue(resultJsonString, GetMessagesFull.class));

                    validateToken(SettersNGetters.getMessagesFull().getError_code());

                    break;

                case TYPE.PROFILE_ME:
                    SettersNGetters.setProfile(StartBackgroundParsing.objectMapper.readValue(resultJsonString, GetProfile.class));
                    validateToken(SettersNGetters.getProfile().getError_code());

                    break;

                case TYPE.MESSSAGES_CREATE:
                    SettersNGetters.setCreateMessage(StartBackgroundParsing.objectMapper.readValue(resultJsonString, CreateMessage.class));

                    validateToken(SettersNGetters.getCreateMessage().getError_code());

                    break;

                case TYPE.PROFILE_ITEMS:
                    SettersNGetters.setProfileItems(StartBackgroundParsing.objectMapper.readValue(resultJsonString, GetProfileItems.class));

                    validateToken(SettersNGetters.getProfileItems().getError_code());

                    break;

                case TYPE.PROFILE_ITEMS_STATS:
                    SettersNGetters.setItemStats(StartBackgroundParsing.objectMapper.readValue(resultJsonString, GetItemStats.class));

                    validateToken(SettersNGetters.getItemStats().getError_code());

                    break;

                case TYPE.PROFILE_EDIT:
                    SettersNGetters.setEditProfile(StartBackgroundParsing.objectMapper.readValue(resultJsonString, GetEditProfile.class));

                    validateToken(SettersNGetters.getEditProfile().getError_code());

                    break;

                case TYPE.CHANGE_PASSWORD:
                    SettersNGetters.setChangePassword(StartBackgroundParsing.objectMapper.readValue(resultJsonString, ChangePassword.class));

                    validateToken(SettersNGetters.getChangePassword().getError_code());

                    break;

                case TYPE.FORGOT_PASSWORD:
                    SettersNGetters.setForgotPassword(StartBackgroundParsing.objectMapper.readValue(resultJsonString, ForgotPassword.class));

                    validateToken(SettersNGetters.getForgotPassword().getError_code());

                    break;

                case TYPE.FB_REGISTER:
                    SettersNGetters.setFbRegLogin(StartBackgroundParsing.objectMapper.readValue(resultJsonString, FBRegLogin.class));

                    validateToken(SettersNGetters.getFbRegLogin().getError_code());

                    break;

                case TYPE.FB_LOGIN:
                    SettersNGetters.setFbRegLogin(StartBackgroundParsing.objectMapper.readValue(resultJsonString, FBRegLogin.class));

                    validateToken(SettersNGetters.getFbRegLogin().getError_code());

                    break;

                case TYPE.ITEMS_CREATE:
                    SettersNGetters.setAddItems(StartBackgroundParsing.objectMapper.readValue(resultJsonString, GetAddItems.class));

                    validateToken(SettersNGetters.getAddItems().getError_code());

                    break;

            }
        }
        catch (final Exception e) {
            mFetcherListener.ParsingException(e);
            System.out.println("Exception");
            e.printStackTrace();

            switch (type) {
                case TYPE.AUTHORIZE:
                    SettersNGetters.setAuthCode(null);
                    break;
                case TYPE.ACCESSTOKEN:

                    SettersNGetters.setAccessToken(null);
                    break;
                case TYPE.REFRESH:
                    // TODO
                    break;
                case TYPE.LOGIN_EMAIL:

                    SettersNGetters.setLoggedInViaEmail(null);
                    break;
                case TYPE.REGISTER_EMAIL:

                    SettersNGetters.setRegistered(null);
                    break;

                case TYPE.CATEGORIES:

                    SettersNGetters.setCategory(null);
                    break;

                case TYPE.ITEM_CATEGORIES:

                    SettersNGetters.setItemCategory(null);
                    break;

                case TYPE.ITEM_DETAILS:
                    SettersNGetters.setDetailedCategory(null);

                    break;
                case TYPE.ITEMS:
                    SettersNGetters.setSearchCategory(null);

                    break;

                case TYPE.FAVORITE:
                    SettersNGetters.setFavCategory(null);

                    break;

                case TYPE.MESSSAGES:

                    SettersNGetters.setMessages(null);
                    break;

                case TYPE.MESSSAGES_USER:

                    SettersNGetters.setMessagesFull(null);
                    break;

                case TYPE.MESSSAGES_CREATE:

                    SettersNGetters.setCreateMessage(null);
                    break;

                case TYPE.PROFILE_ME:

                    SettersNGetters.setProfile(null);
                    break;

                case TYPE.PROFILE_ITEMS:

                    SettersNGetters.setProfileItems(null);
                    break;

                case TYPE.PROFILE_ITEMS_STATS:

                    SettersNGetters.setItemStats(null);
                    break;
                case TYPE.PROFILE_EDIT:

                    SettersNGetters.setEditProfile(null);
                    break;
                case TYPE.CHANGE_PASSWORD:

                    SettersNGetters.setChangePassword(null);
                    break;
                case TYPE.FORGOT_PASSWORD:

                    SettersNGetters.setForgotPassword(null);
                    break;

                case TYPE.ITEMS_CREATE:

                    SettersNGetters.setAddItems(null);
                    break;

            }

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
