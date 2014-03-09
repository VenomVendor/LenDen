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

package vee.HexWhale.LenDen.aUI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.agimind.widget.SlideHolder;

import vee.HexWhale.LenDen.R;
import vee.HexWhale.LenDen.Utils.Constants;
import vee.HexWhale.LenDen.aUI.Adapters.MenuDrawerAdapter;

public class MenuBar extends FragmentActivity {

    public RelativeLayout mRelLayTop;
    ImageView mImgRight;
    protected ImageView mImgLeft;
    protected TextView mTextCenter;
    private SlideHolder mSlideHolder;
    ListView mDrawerList;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.initiateMenu();
    }

    private void initiateMenu() {
        this.mSlideHolder = (SlideHolder) this.findViewById(R.id.slideHolder);
        this.mRelLayTop = (RelativeLayout) this.findViewById(R.id.menu_lyt);
        this.mImgRight = (ImageView) this.findViewById(R.id.menu_right);
        this.mImgLeft = (ImageView) this.findViewById(R.id.menu_left);
        this.mTextCenter = (TextView) this.findViewById(R.id.menu_center);
        this.mDrawerList = (ListView) this.findViewById(R.id.menu_drawer);
        this.mSlideHolder.setDirection(SlideHolder.DIRECTION_RIGHT);
        this.mSlideHolder.setAllowInterceptTouch(false);

        this.mDrawerList.setAdapter(new MenuDrawerAdapter(this));

        this.mDrawerList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MenuBar.this.mSlideHolder.toggle();
                MenuBar.this.startActivity(new Intent(MenuBar.this.getApplicationContext(), Constants.menuclass[position]));
                MenuBar.this.AnimPrev();
            }
        });
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

    @SuppressWarnings("unused")
    private void AnimNext() {
        this.overridePendingTransition(R.anim.enter, R.anim.exit);
        return;
    }

    public void ToggleMenu(View v) {
        this.mSlideHolder.toggle();
    }

}
