/**
 * ***Copyright(c)	:	2014-Present, VenomVendor.***
 * Author		:	VenomVendor
 * Dated		:	1 Apr, 2014 12:40:27 AM
 * Project		:	LenDen-Android
 * Client		:	LenDen
 * Contact		:	info@VenomVendor.com
 * URL			:	https://www.google.co.in/search?q=VenomVendor
 * Copyright(c)	:	2014-Present, VenomVendor.
 * License		:	This work is licensed under Attribution-NonCommercial 3.0 Unported (CC BY-NC 3.0).
 *					License info at http://creativecommons.org/licenses/by-nc/3.0/deed.en_US
 *					Read More at http://creativecommons.org/licenses/by-nc/3.0/legalcode
 **/

package vee.HexWhale.LenDen.Parsers.MessagesFull;

public class Messages_list {
    private String date_time;
    private String message;
    private String user_first_name;
    private String user_id;
    private String user_last_name;

    public String getDate_time() {
        return this.date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser_first_name() {
        return this.user_first_name;
    }

    public void setUser_first_name(String user_first_name) {
        this.user_first_name = user_first_name;
    }

    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_last_name() {
        return this.user_last_name;
    }

    public void setUser_last_name(String user_last_name) {
        this.user_last_name = user_last_name;
    }
}
