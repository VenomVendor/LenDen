/**
 * ***Copyright(c) : 2014-Present, VenomVendor.***
 * Author : VenomVendor
 * Dated : 21 Feb, 2014 4:41:51 PM
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

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ListView;

import vee.HexWhale.LenDen.aUI.Adapters.MessagesFullAdapter;

public class MessagesFull extends FragmentActivity {

    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.messages_full);
        mListView = (ListView) findViewById(android.R.id.list);

        MessagesFullAdapter adapter = new MessagesFullAdapter(this);
        mListView.setAdapter(adapter);

    }

    public void Finish(View v) {
        finish();
        AnimPrev();
    }

    @Override
    public void onBackPressed() {
        this.finish();
        AnimPrev();
    }

    private void AnimPrev() {
        overridePendingTransition(R.anim.android_slide_in_left, R.anim.android_slide_out_right);
        return;
    }

}
