
package vee.HexWhale.LenDen;

import vee.HexWhale.LenDen.Utils.Settings;

public class Constants {

    public static int[] gridImages = {
            R.drawable.bp5_crop1, R.drawable.bp5_crop2, R.drawable.bp5_crop3, R.drawable.bp5_crop4, R.drawable.bp5_crop5, R.drawable.bp5_crop6
    };

    public static int[] menuImages = {
            R.drawable.add_items, R.drawable.search, R.drawable.favorites, R.drawable.settings
    };
    public static String[] menuText = {
            "Add items", "Search", "Favorites", "Settings"
    };

    public static Class<?>[] menuclass = {
            AddItems.class, Search.class, Favorites.class, Settings.class
    };

}
