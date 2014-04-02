/**
 * ***Copyright(c)	:	2014-Present, VenomVendor.***
 * Author			:	VenomVendor
 * Dated			:	6 Mar, 2014 1:09:18 AM
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

import vee.HexWhale.LenDen.Utils.Constants.API;

public class GetData {

    /**
     * Gets the url.
     * 
     * @param tokenType the token type
     * @return the url
     * @refer {@link vee.HexWhale.LenDen.Utils.Constants.API.URL}
     */
    public static String getUrl(String tokenType) {
        return API.BASE_URL + tokenType;
    }

    public static String getImageUrl(String imgAddr) {
        return API.BASE_URL + imgAddr + "/";
    }
}
