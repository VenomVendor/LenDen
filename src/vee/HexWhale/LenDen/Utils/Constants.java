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
 *


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

    public final static boolean enableLog = true;

    public final static class API {

        public final static String BASE_URL = "http://ld.lendenapp.com/";
        public final static String KEY = "ae2fafc680fbce6f93166f1d9c7eae";
        public final static String SECRET = "50d65976adb25c2110bc69eee1c88a";

        public final static class HEADERS {
            public final static String ACCESS_TOKEN = "access_token";
            public final static String CONTENT_TYPE = "Content-Type";
            public final static String JSON = "application/json";
            public final static String MULTIPART = "multipart/form-data parameters";
            public final static String PICTURE = "picture";
        }

        public final static class STRING {
            public final static String AUTH = "auth_code";
            public final static String CACHE_LOC = "data/.vee.HexWhale.LenDen/.imgCache";
            public final static String CATEGORY_ID = "category_id";
            public final static String CURRENT_PASSWORD = "current_password";
            public final static String DESCRIPTION = "description";
            public final static String EMAIL = "email";
            public final static String ERROR = "Error : ";
            public final static String FAILED = "failed";
            public final static String FROM = "FROM";
            public final static String FAVOURITES = "FAVOURITES";
            public final static String PROFILE = "PROFILE";



            public final static String FB_GENDER = "gender";
            public final static String FB_ID = "fb_id";
            public final static String FB_TOKEN = "fb_token";
            public final static String FB_USERNAME = "fb_user_name";
            public final static String FIRSTNAME = "first_name";
            public final static String ITEM_ID = "item_id";
            public final static String KEY = "client_key";
            public final static String LASTNAME = "last_name";
            public final static String LATITUDE = "latitude";
            public final static String LONGITUDE = "longitude";
            public final static String MESSAGE = "message";
            public final static String NEW_PASSWORD = "new_password";
            public final static String OFFSET = "offset";
            public final static String ORIGINAL_PRICE = "original_price";
            public final static String PAGE = "page";
            public final static String PARTNER_ID = "partner_id";
            public final static String PASSWORD = "password";
            public final static String PICTURE = "picture";
            public final static String PICTURE_1 = "picture1";
            public final static String PICTURE_2 = "picture2";
            public final static String PICTURE_3 = "picture3";
            public final static String PICTURE_4 = "picture4";
            public final static String PICTURE_5 = "picture5";
            public final static String POSITION = "position";
            public final static String PREVIEW = "PREVIEW";
            public final static String PRICE = "price";
            public final static String RANGE = "range";
            public final static String REFRESH = "refresh_token";
            public final static String SEARCH = "search";
            public final static String SECRET = "client_secret";
            public final static String SELLING_PRICE = "selling_price";
            public final static String STATUS = "status";
            public final static String SUBCATEGORY_ID = "subcategory_id";
            public final static String SUCCESS = "success";
            public final static String TITLE = "title";
            public final static String TRADE_MODE = "trade_mode";
            public final static String USER_ALREADY_EXISTS = "User already exists";
            public final static String USER_ALREADY_EXISTS_ERROR = "users-4";

        }

        public final static class IMAGEURL {
            public final static String DP = "users/photo/";
            public final static String ICON = "/icon/";
            public final static String PICTURE = "/picture/";
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
            public final static String CATEGORIES = "categories/";
            public final static String ITEM_CATEGORIES = "items/category/";
            public final static String ITEM_SUBCATEGORY = "items/subcategory/";
            public final static String ITEM_DETAILS = "items/details/";
            public final static String ITEMS = "items/";
            public final static String FAVORITE = "items/me/favorite/";
            public final static String MESSSAGES = "messages/";
            public final static String MESSSAGES_USER = "messages/user/";
            public final static String MESSSAGES_CREATE = "messages/create/";
            public final static String PROFILE_ME = "users/me/";
            public final static String PROFILE_ITEMS = "items/me/";
            public final static String PROFILE_ITEMS_STATS = "items/me/stats/";
            public final static String PROFILE_PIC = "users/me/photo/upload/";
            public final static String PROFILE_EDIT = "users/me/edit/";
            public final static String CHANGE_PASSWORD = "users/me/change/password/";
            public final static String FORGOT_PASSWORD = "users/forgot/password/";
            public final static String FB_REGISTER = "users/register/facebook/";
            public final static String FB_LOGIN = "users/login/facebook/";
            public final static String SUB_CATEGORIES = "categories/subcategories/";
            public final static String ITEMS_CREATE = "items/create/";
            public final static String ITEMS_CREATE_UPLOAD = "items/pictures/upload/";
            public final static String MAP_ITEMS = URL.ITEMS;

        }

        public final static class TYPE {
            public final static int AUTHORIZE = 1;
            public final static int ACCESSTOKEN = 2;
            public final static int REFRESH = 3;
            public final static int REGISTER_EMAIL = 4;
            public final static int REGISTER_FB = 5;
            public final static int LOGIN_EMAIL = 6;
            public final static int LOGIN_FB = 7;
            public final static int LOGOUT = 8;
            public final static int CATEGORIES = 9;
            public final static int ITEM_CATEGORIES = 10;
            public final static int ITEM_SUBCATEGORY = 11;
            public final static int ITEM_DETAILS = 12;
            public final static int ITEMS = 13;
            public final static int FAVORITE = 14;
            public final static int MESSSAGES = 15;
            public final static int MESSSAGES_USER = 16;
            public final static int MESSSAGES_CREATE = 17;
            public final static int PROFILE_ME = 18;
            public final static int PROFILE_ITEMS = 19;
            public final static int PROFILE_ITEMS_STATS = 20;
            public final static int PROFILE_PIC = 21;
            public final static int PROFILE_EDIT = 22;
            public final static int CHANGE_PASSWORD = 23;
            public final static int FORGOT_PASSWORD = 24;
            public final static int FB_REGISTER = 25;
            public final static int FB_LOGIN = 26;
            public final static int SUB_CATEGORIES = 27;
            public final static int ITEMS_CREATE = 28;
            public final static int MAP_ITEMS = 29;
        }

    }

    public class KEY {
        public final static String ACCESS_TOKEN = "ACCESSTOKEN";
        public final static String AUTH_CODE = "AUTHCODE";
        public final static String LAST_JSON = "LASTJSON";
        public final static String LOGIN_TYPE = "LOGINTYPE";
        public final static String MY_E_MAIL = "MYEMAIL";
        public final static String MY_F_NAME = "MYFNAME";
        public final static String MY_I_URL = "MYIURL";
        public final static String MY_L_NAME = "MYLNAME";
        public final static String OPENED_TIMES_COUNT = "OPENEDTIMESCOUNT";
        public final static String REFRESH_TOKEN = "REFRESHTOKEN";
    }

    public class LOGIN_TYPE
    {
        public final static int EMAIL = 2;
        public final static int FACEBOOK = 5;
        public final static int HACKED = 0;
        public final static int REGISTER = 1;
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
