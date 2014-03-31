/**
 * ***Copyright(c) : 2014-Present, VenomVendor.***
 * Author : VenomVendor
 * Dated : 1 Apr, 2014 12:40:27 AM
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

package vee.HexWhale.LenDen.Parsers.MessagesFull;

import java.util.List;

public class Response {
    private List<Messages_list> messages_list;
    private boolean show_complete;
    private boolean show_lock;
    private boolean show_unlock;

    public List<Messages_list> getMessages_list() {
        return messages_list;
    }

    public void setMessages_list(List<Messages_list> messages_list) {
        this.messages_list = messages_list;
    }

    public boolean isShow_complete() {
        return show_complete;
    }

    public void setShow_complete(boolean show_complete) {
        this.show_complete = show_complete;
    }

    public boolean isShow_lock() {
        return show_lock;
    }

    public void setShow_lock(boolean show_lock) {
        this.show_lock = show_lock;
    }

    public boolean isShow_unlock() {
        return show_unlock;
    }

    public void setShow_unlock(boolean show_unlock) {
        this.show_unlock = show_unlock;
    }

}
