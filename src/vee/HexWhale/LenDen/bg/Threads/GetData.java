
package vee.HexWhale.LenDen.bg.Threads;

import vee.HexWhale.LenDen.Utils.Constants.API;

public class GetData {

    public static String getUrl(String tokenType) {
        return API.BASE_URL + tokenType;
    }
}