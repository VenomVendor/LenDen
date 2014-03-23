/**
 * ***Copyright(c) : 2014-Present, VenomVendor.***
 * Author : VenomVendor
 * Dated : 2 Mar, 2014 4:11:32 PM
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

package vee.HexWhale.LenDen.bg.Threads;

import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Class Validator.
 */
public class Validator {

    /** The Email pattern. */
    private final static String EmailPattern = "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])$";

    public static String isvalidEmail(final EditText editText) {
        if (editText.length() < 1) {
            return "Please provide an input";
        }

        if (editText.getText().toString().contains(" ")) {
            return "Input has space";
        }

        final String emailAddress = editText.getText().toString();
        final Pattern regexPattern = Pattern.compile(Validator.EmailPattern);
        final Matcher regMatcher = regexPattern.matcher(emailAddress);
        if (regMatcher.matches()) {
            return "k";
        }
        return "Invalid Email";
    }

    public static final String hasMinChars(final EditText editText, final int minChars) {
        if (editText.length() < 1) {
            return "Please provide an input";
        }

        if (editText.getText().toString().contains(" ")) {
            return "Input has space";
        }

        if (editText.getText().toString().trim().length() >= minChars) {
            return "k";
        }
        return "Minimum " + minChars + " chars";
    }

    public static final boolean doesPasswordMatch(final EditText psw1, final EditText psw2) {

        return psw1.getText().toString().equals(psw2.getText().toString());

    }

}
