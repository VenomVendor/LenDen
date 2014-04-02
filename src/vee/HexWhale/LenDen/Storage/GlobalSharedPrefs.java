/**
 * ***Copyright(c)	:	2014-Present, VenomVendor.***
 * Author			:	VenomVendor
 * Dated			:	6 Mar, 2014 1:10:48 AM
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
        mContext = context;
    }

    public boolean setAppOpenedTimes() {

        System.out.println("INITIAL TIMES COUNT " + getAppOpenedTimes()); // DO
        // NOT
        // REMOVE
        // ME
        edit();
        mEditor.putInt(KEY.OPENED_TIMES_COUNT, (mLocalRefOpened + 1));
        return commit();
    }

    public int getAppOpenedTimes() {
        initiateSharedPrefrences();
        mLocalRefOpened = mSharedPreferences.getInt(KEY.OPENED_TIMES_COUNT, 0);
        return mLocalRefOpened;
    }

    /**
     * Initiate's SharedPrefrences.
     */

    private void initiateSharedPrefrences() {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    /**
     * SharedPrefrence Editor<br>
     * Make sure {@link #commit()} is called <b>AFTER</b> this.
     */
    @SuppressLint("CommitPrefEdits")
    private void edit() {
        mEditor = mSharedPreferences.edit();
    }

    /**
     * SharedPrefrence Editor<br>
     * Make sure {@link #edit()} is called <b>BEFORE</b> this.
     */
    private boolean commit() {
        return mEditor.commit();
    }

    /**
     * @param xKey {@link KEY}
     * @param value String
     */
    public boolean setStringInPref(String xKey, String value) {
        initiateSharedPrefrences();
        edit();
        mEditor.putString(xKey, null);
        mEditor.putString(xKey, value);
        return commit();
    }

    /**
     * @param xKey {@link KEY}
     * @param value int
     */

    public boolean setIntInPref(String xKey, int value) {
        initiateSharedPrefrences();
        edit();
        mEditor.putInt(xKey, -1);
        mEditor.putInt(xKey, value);
        return commit();
    }

    /**
     * @param xKey {@link KEY}
     * @return <b>VALUE</b> String
     */
    public String getStringInPref(String xKey) {
        initiateSharedPrefrences();
        return mSharedPreferences.getString(xKey, "");
    }

    /**
     * @param xKey {@link KEY}
     * @return <b>VALUE</b> int
     */
    public int getIntInPref(String xKey) {
        initiateSharedPrefrences();
        return mSharedPreferences.getInt(xKey, -1);
    }

}
