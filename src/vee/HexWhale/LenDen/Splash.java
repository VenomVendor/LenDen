/**
 * ***Copyright(c) : 2014-Present, VenomVendor.***
 * Author : VenomVendor
 * Dated : 17 Feb, 2014 3:29:01 AM
 * Project : LenDen-Android
 * Client : LenDen
 * Contact : info@VenomVendor.com
 * URL : https://www.google.co.in/search?q=VenomVendor
 * Copyright(c) : 2014-Present, VenomVendor.
 * License : This work is licensed under Attribution-NonCommercial 3.0 Unported (CC BY-NC 3.0).
 * License info at http://creativecommons.org/licenses/by-nc/3.0/deed.en_US
 * Read More at http://creativecommons.org/licenses/by-nc/3.0/legalcode
 **/

package vee.HexWhale.LenDen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends Activity {

    final long time = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Handler mHandler = new Handler();

        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), WalkThrough.class));
                finish();
                // overridePendingTransition(R.anim.enter, R.anim.exit);
                overridePendingTransition(R.anim.enter, R.anim.exit);

                // overridePendingTransition(R.anim.android_slide_in_right,
                // R.anim.android_slide_out_left);
            }
        }, time);

    }

}
