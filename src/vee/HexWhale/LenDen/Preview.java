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
        this.setContentView(R.layout.preview);
        this.mListView = (ListView) this.findViewById(android.R.id.list);
        final PreviewAdapter adapter = new PreviewAdapter(this);

        // SwingBottomInAnimationAdapter mScaleInAnimationAdapter = new
        // SwingBottomInAnimationAdapter(adapter, 110, 400);
        // ScaleInAnimationAdapter mScaleInAnimationAdapter = new
        // ScaleInAnimationAdapter(adapter, 0.5f, 110, 400);

        final SwingRightInAnimationAdapter mScaleInAnimationAdapter = new SwingRightInAnimationAdapter(adapter, 40, 400);
        mScaleInAnimationAdapter.setAbsListView(this.mListView);
        this.mListView.setAdapter(mScaleInAnimationAdapter);

        this.mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final Intent mIntent = new Intent(Preview.this.getApplicationContext(), Detailed.class);
                Preview.this.startActivity(mIntent);
                Preview.this.AnimNext();
            }

        });

    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.mImgLeft = (ImageView) this.findViewById(R.id.menu_left);
        this.mTextCenter = (TextView) this.findViewById(R.id.menu_center);

        this.mImgLeft.setVisibility(View.INVISIBLE);
        this.mTextCenter.setText(("Home").toUpperCase(Locale.UK));

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

}
