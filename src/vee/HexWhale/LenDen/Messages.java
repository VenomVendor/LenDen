/**
 * ***Copyright(c) : 2014-Present, VenomVendor.***
 * Author : VenomVendor
 * Dated : 18 Feb, 2014 4:25:32 PM
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import vee.HexWhale.LenDen.aUI.Adapters.MessagesAdapter;

import java.util.Locale;

public class Messages extends FragmentActivity {

    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages);
        ((TextView) findViewById(R.id.menu_center)).setText(("messages").toUpperCase(Locale.UK));
        ((ImageView) findViewById(R.id.menu_right)).setVisibility(View.INVISIBLE);

        mListView = (ListView) findViewById(android.R.id.list);
        MessagesAdapter adapter = new MessagesAdapter(this);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                startActivity(new Intent(getApplicationContext(), MessagesFull.class));
                AnimNext();
            }

        });

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

    private void AnimNext() {
        overridePendingTransition(R.anim.enter, R.anim.exit);
        return;
    }
}
