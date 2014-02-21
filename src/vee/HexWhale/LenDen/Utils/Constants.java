/**
 * ***Copyright(c) : 2014-Present, VenomVendor.***
 * Author : VenomVendor
 * Dated : 17 Feb, 2014 3:28:57 AM
 * Project : LenDen-Android
 * Client : LenDen
 * Contact : info@VenomVendor.com
 * URL : https://www.google.co.in/search?q=VenomVendor
 * Copyright(c) : 2014-Present, VenomVendor.
 * License : This work is licensed under Attribution-NonCommercial 3.0 Unported (CC BY-NC 3.0).
 * License info at http://creativecommons.org/licenses/by-nc/3.0/deed.en_US
 * Read More at http://creativecommons.org/licenses/by-nc/3.0/legalcode
 **/

package vee.HexWhale.LenDen.Utils;

import vee.HexWhale.LenDen.AddItems;
import vee.HexWhale.LenDen.Favorites;
import vee.HexWhale.LenDen.Messages;
import vee.HexWhale.LenDen.Profile;
import vee.HexWhale.LenDen.R;
import vee.HexWhale.LenDen.Search;
import vee.HexWhale.LenDen.Settings;

public class Constants {

    public static int[] gridImages = {
            R.drawable.bp5_crop1, R.drawable.bp5_crop2, R.drawable.bp5_crop3, R.drawable.bp5_crop4, R.drawable.bp5_crop5, R.drawable.bp5_crop6
    };

    public static int[] menuImages = {
            R.drawable.add_items, R.drawable.search, R.drawable.favorites, R.drawable.messages, R.drawable.profile, R.drawable.settings
    };
    public static String[] menuText = {
            "Add items", "Search", "Favorites", "Messages", "Profile", "Settings"
    };

    public static Class<?>[] menuclass = {
            AddItems.class, Search.class, Favorites.class, Messages.class, Profile.class, Settings.class
    };

    // public static int[] gridImages = {
    // R.drawable.bp5_crop1, R.drawable.bp5_crop2, R.drawable.bp5_crop3,
    // R.drawable.bp5_crop4, R.drawable.bp5_crop5, R.drawable.bp5_crop6
    // };
    //
    // public static int[] menuImages = {
    // R.drawable.add_items, R.drawable.search, R.drawable.favorites,
    // R.drawable.profile, R.drawable.settings
    // };
    // public static String[] menuText = {
    // "Add items", "Search", "Favorites", "Profile", "Settings"
    // };
    //
    // public static Class<?>[] menuclass = {
    // AddItems.class, Search.class, Favorites.class, Profile.class,
    // Settings.class
    // };

}
