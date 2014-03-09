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

package vee.HexWhale.LenDen.Utils;

import vee.HexWhale.LenDen.AddItems;
import vee.HexWhale.LenDen.Favorites;
import vee.HexWhale.LenDen.Messages;
import vee.HexWhale.LenDen.Profile;
import vee.HexWhale.LenDen.R;
import vee.HexWhale.LenDen.Search;
import vee.HexWhale.LenDen.Settings;

public final class Constants {

    public final static class API {

        public final static String BASE_URL = "http://lddev.lendenapp.com/";
        public final static String KEY = "592a34a4abadaa52b3c0abb055b689";
        public final static String SECRET = "266aec96e90af9450eb4ad450309b3";

        public final static class STRING {
            public final static String KEY = "client_key";
            public final static String SECRET = "client_secret";
            public final static String AUTH = "auth_code";
            public final static String REFRESH = "refresh_token";
            public final static String EMAIL = "email";
            public final static String PASSWORD = "password";
            public final static String FIRSTNAME = "first_name";
            public final static String LASTNAME = "last_name";

        }

        public final static class HEADERS {
            public final static String CONTENT_TYPE = "Content-Type";
            public final static String JSON = "application/json";
            public final static String ACCESS_TOKEN = "access_token";
            public final static String MULTIPART = "multipart/form-data parameters";
            public final static String PICTURE = "picture";
        }

        public final static class URL {
            public final static String AUTHORIZE = "tokens/authorize/";
            public final static String ACCESSTOKEN = "tokens/issue/";
            public final static String REFRESH = "tokens/refresh/";
            public final static String REGISTER_EMAIL = "users/register/email/";
            public final static String REGISTER_FB = "users/register/facebook/";
            public final static String LOGIN_EMAIL = "users/login/email/";
            public final static String LOGIN_FB = "users/login/facebook/";
            public final static String LOGOUT = "users/logout/";
        }

        public final static class TYPE {
            public final static int AUTHORIZE = 1;
            public final static int ACCESSTOKEN = 2;
            public final static int REFRESH = 3;
            public final static int LOGIN_EMAIL = 4;
            public final static int REGISTER_EMAIL = 5;
        }

    }

    public class KEY {

        public final static String AUTH_CODE = "AUTHCODE";
        public final static String ACCESS_TOKEN = "ACCESSTOKEN";
        public final static String REFRESH_TOKEN = "REFRESHTOKEN";
        public final static String LAST_JSON = "LASTJSON";
        public final static String OPENED_TIMES_COUNT = "OPENEDTIMESCOUNT";

    }

    public final static int[] gridImages = {
            R.drawable.bp5_crop1, R.drawable.bp5_crop2, R.drawable.bp5_crop3, R.drawable.bp5_crop4, R.drawable.bp5_crop5, R.drawable.bp5_crop6
    };

    public final static int[] menuImages = {
            R.drawable.add_items, R.drawable.search, R.drawable.favorites, R.drawable.messages, R.drawable.profile, R.drawable.settings
    };
    public final static String[] menuText = {
            "Add items", "Search", "Favorites", "Messages", "Profile", "Settings"
    };

    public final static Class<?>[] menuclass = {
            AddItems.class, Search.class, Favorites.class, Messages.class, Profile.class, Settings.class
    };

}
