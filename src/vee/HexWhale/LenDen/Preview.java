/**
 * ***Copyright(c) : 2014-Present, VenomVendor.***
 * Author : VenomVendor
 * Dated : 17 Feb, 2014 3:28:58 AM
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

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.haarman.listviewanimations.swinginadapters.prepared.SwingRightInAnimationAdapter;

import vee.HexWhale.LenDen.aUI.MenuBar;
import vee.HexWhale.LenDen.aUI.Adapters.PreviewAdapter;

import java.util.Locale;

public class Preview extends MenuBar {

    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview);
        mListView = (ListView) findViewById(android.R.id.list);
        PreviewAdapter adapter = new PreviewAdapter(this);

        // SwingBottomInAnimationAdapter mScaleInAnimationAdapter = new
        // SwingBottomInAnimationAdapter(adapter, 110, 400);
        // ScaleInAnimationAdapter mScaleInAnimationAdapter = new
        // ScaleInAnimationAdapter(adapter, 0.5f, 110, 400);

        SwingRightInAnimationAdapter mScaleInAnimationAdapter = new SwingRightInAnimationAdapter(adapter, 40, 400);
        mScaleInAnimationAdapter.setAbsListView(mListView);
        mListView.setAdapter(mScaleInAnimationAdapter);

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent mIntent = new Intent(getApplicationContext(), Detailed.class);
                startActivity(mIntent);
                AnimNext();
            }

        });

    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mImgLeft = (ImageView) findViewById(R.id.menu_left);
        mTextCenter = (TextView) findViewById(R.id.menu_center);

        mImgLeft.setVisibility(View.INVISIBLE);
        mTextCenter.setText(("Home").toUpperCase(Locale.UK));

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
