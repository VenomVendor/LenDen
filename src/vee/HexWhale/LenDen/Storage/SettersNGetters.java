/**
 * ***Copyright(c) : 2014-Present, VenomVendor.***
 * Author : VenomVendor
 * Dated : 6 Mar, 2014 1:10:48 AM
 * Project : LenDen-Android
 * Client : LenDen
 * Contact : info@VenomVendor.com
 * URL : https://www.google.co.in/search?q=VenomVendor
 * Copyright(c) : 2014-Present, VenomVendor.
 * License : This work is licensed under Attribution-NonCommercial 3.0 Unported (CC BY-NC 3.0).
 * License info at http://creativecommons.org/licenses/by-nc/3.0/deed.en_US
 * Read More at http://creativecommons.org/licenses/by-nc/3.0/legalcode
 **/

package vee.HexWhale.LenDen.Storage;

import vee.HexWhale.LenDen.Parsers.AccessToken.GetAccessToken;
import vee.HexWhale.LenDen.Parsers.AuthCode.GetAuthCode;

public class SettersNGetters {

    private static GetAuthCode authCode;
    private static GetAccessToken accessToken;
    private static GetAccessToken isLoggedInEmail;
    private static GetAccessToken isRegistered;

    public static GetAuthCode getAuthCode() {
        return SettersNGetters.authCode;
    }

    public static void setAuthCode(GetAuthCode authCode) {
        SettersNGetters.authCode = authCode;
    }

    public static GetAccessToken getAccessToken() {
        return SettersNGetters.accessToken;
    }

    public static void setAccessToken(GetAccessToken accessToken) {
        SettersNGetters.accessToken = accessToken;
    }

    public static GetAccessToken isLoggedInViaEmail() {
        return isLoggedInEmail;
    }

    public static void setLoggedInViaEmail(GetAccessToken email) {
        SettersNGetters.isLoggedInEmail = email;
    }

    public static GetAccessToken isRegistered() {
        return isRegistered;
    }

    public static void setRegistered(GetAccessToken isRegistered) {
        SettersNGetters.isRegistered = isRegistered;
    }

}
