/**
 * ***Copyright(c)	:	2014-Present, VenomVendor.***
 * Author			:	VenomVendor
 * Dated			:	6 Mar, 2014 1:09:00 AM
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

import com.android.volley.VolleyError;

public interface FetcherListener {

    /**
     * Error Fetching
     */
    void errorFetching(int type, VolleyError error);

    /**
     * Finished Fetching
     */
    void finishedFetching(int type, String response);

    /**
     * Before Parsing, Safe to Update UI
     */
    void beforeParsing(int type);

    /**
     * Started Parsing DO NOT UPDATE UI HERE
     */
    void startedParsing(int type);

    /**
     * Error Parsing
     */
    void ParsingException(Exception e);

    /**
     * Finished Parsing
     */
    void finishedParsing(int typ);

    void tokenError(String tokenError);
}
