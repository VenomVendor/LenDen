/**
 * ***Copyright(c) : 2014-Present, VenomVendor.***
 * Author : VenomVendor
 * Dated : 1 Apr, 2014 12:40:13 AM
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

package vee.HexWhale.LenDen.Parsers.Messages;

import java.util.List;

public class Response {

    private int current_page;
    private List<Messages> messages;
    private int total_message_count;

    public synchronized int getCurrent_page() {
        return current_page;
    }

    public synchronized void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public synchronized List<Messages> getMessages() {
        return messages;
    }

    public synchronized void setMessages(List<Messages> messages) {
        this.messages = messages;
    }

    public synchronized int getTotal_message_count() {
        return total_message_count;
    }

    public synchronized void setTotal_message_count(int total_message_count) {
        this.total_message_count = total_message_count;
    }

}
