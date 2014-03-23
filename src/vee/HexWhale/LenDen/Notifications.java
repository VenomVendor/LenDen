/**
 * ***Copyright(c) : 2014-Present, VenomVendor.***
 * Author : VenomVendor
 * Dated : 17 Feb, 2014 3:28:57 AM
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

package vee.HexWhale.LenDen;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Notifications extends FragmentActivity {

    TextView mTitle;

    // ImageView mOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.notifications);
        mTitle = (TextView) findViewById(R.id.menu_center);
        mTitle.setText("SETTINGS");
        // mOk = (ImageView) findViewById(R.id.menu_right);
        // mOk.setBackgroundResource(0);
        // mOk.setImageResource(0);
    }

    public void Finish(View v) {
        finish();
        AnimPrev();
    }

    public void Submit(View v) {
        finish();
        AnimNext();
    }

    @Override
    public void onBackPressed() {
        finish();
        AnimPrev();
    }

    private void AnimPrev() {
        overridePendingTransition(R.anim.android_slide_in_left, R.anim.android_slide_out_right);
        return;
    }

    private void AnimNext() {
        overridePendingTransition(R.anim.enter, R.anim.exit);
        return;
    }

    public void Noti(View view) {

        if (toggleImage((ImageView) view)) {
            ToastL("Enable Notification");
        }
        else {
            ToastL("Disable Notification");
        }
    }

    private boolean toggleImage(ImageView imv) {
        if (imv.getTag().toString().trim().equalsIgnoreCase("on")) {
            imv.setImageResource(R.drawable.notification_off);
            imv.setTag("off");
            return false;
        }
        else {
            imv.setImageResource(R.drawable.notification_on);
            imv.setTag("on");
            return true;
        }
    }

    private void ToastL(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

}
