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

package vee.HexWhale.LenDen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Settings extends FragmentActivity {

    TextView mTitle;
    ImageView mOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.settings);
        this.mTitle = (TextView) this.findViewById(R.id.menu_center);
        this.mTitle.setText("SETTINGS");
        this.mOk = (ImageView) this.findViewById(R.id.menu_right);
        this.mOk.setVisibility(View.INVISIBLE);

    }

    public void Finish(View v) {
        this.finish();
        this.AnimPrev();
    }

    public void Submit(View v) {
        this.finish();
        this.AnimNext();
    }

    @Override
    public void onBackPressed() {
        this.finish();
        this.AnimPrev();
    }

    private void AnimPrev() {
        this.overridePendingTransition(R.anim.android_slide_in_left, R.anim.android_slide_out_right);
        return;
    }

    private void AnimNext() {
        this.overridePendingTransition(R.anim.enter, R.anim.exit);
        return;
    }

    public void EditProfile(View v) {
        this.startActivity(new Intent(this.getApplicationContext(), EditProfile.class));
        this.AnimNext();
    }

    public void Profile(View v) {
        this.startActivity(new Intent(this.getApplicationContext(), Profile.class));
        this.AnimNext();
    }

    public void Notifications(View v) {
        this.startActivity(new Intent(this.getApplicationContext(), Notifications.class));
        this.AnimNext();
    }

}
