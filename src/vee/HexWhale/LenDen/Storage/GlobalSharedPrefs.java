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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import vee.HexWhale.LenDen.Utils.Constants.KEY;

public class GlobalSharedPrefs {

    private SharedPreferences mSharedPreferences;
    private Editor mEditor;
    private int mLocalRefOpened;
    private final Context mContext;
    protected static SettersNGetters mSettersNGetters = new SettersNGetters();

    public GlobalSharedPrefs(Context context) {
        this.mContext = context;
    }

    public boolean setAppOpenedTimes() {

        System.out.println("INITIAL TIMES COUNT " + this.getAppOpenedTimes()); // DO
        // NOT
        // REMOVE
        // ME
        this.edit();
        this.mEditor.putInt(KEY.OPENED_TIMES_COUNT, (this.mLocalRefOpened + 1));
        return this.commit();
    }

    public int getAppOpenedTimes() {
        this.initiateSharedPrefrences();
        this.mLocalRefOpened = this.mSharedPreferences.getInt(KEY.OPENED_TIMES_COUNT, 0);
        return this.mLocalRefOpened;
    }

    /**
     * Initiate's SharedPrefrences.
     */

    private void initiateSharedPrefrences() {
        this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
    }

    /**
     * SharedPrefrence Editor<br>
     * Make sure {@link #commit()} is called <b>AFTER</b> this.
     */
    @SuppressLint("CommitPrefEdits")
    private void edit() {
        this.mEditor = this.mSharedPreferences.edit();
    }

    /**
     * SharedPrefrence Editor<br>
     * Make sure {@link #edit()} is called <b>BEFORE</b> this.
     */
    private boolean commit() {
        return this.mEditor.commit();
    }

    /**
     * @param xKey
     *            {@link KEY}
     * @param value
     *            String
     */
    public boolean setStringInPref(String xKey, String value) {
        this.initiateSharedPrefrences();
        this.edit();
        this.mEditor.putString(xKey, null);
        this.mEditor.putString(xKey, value);
        return this.commit();
    }

    /**
     * @param xKey
     *            {@link KEY}
     * @param value
     *            int
     */

    public boolean setIntInPref(String xKey, int value) {
        this.initiateSharedPrefrences();
        this.edit();
        this.mEditor.putInt(xKey, -1);
        this.mEditor.putInt(xKey, value);
        return this.commit();
    }

    /**
     * @param xKey
     *            {@link KEY}
     * @return <b>VALUE</b> String
     */
    public String getStringInPref(String xKey) {
        this.initiateSharedPrefrences();
        return this.mSharedPreferences.getString(xKey, null);
    }

    /**
     * @param xKey
     *            {@link KEY}
     * @return <b>VALUE</b> int
     */
    public int getIntInPref(String xKey) {
        this.initiateSharedPrefrences();
        return this.mSharedPreferences.getInt(xKey, -1);
    }

}
