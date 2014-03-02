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

package vee.HexWhale.LenDen.aUI.Pagers;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import vee.HexWhale.LenDen.R;

public class WalkThroughPager extends PagerAdapter {

    Activity sActivity;

    public WalkThroughPager(Activity act) {
        this.sActivity = act;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 5;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        // TODO Auto-generated method stub
        return view == (object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final LayoutInflater inflater = (LayoutInflater) sActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View v = inflater.inflate(R.layout.walkthrough_pager, null);

        final TextView sTitle = (TextView) v.findViewById(R.id.wt_pager_title);
        final TextView sText = (TextView) v.findViewById(R.id.wt_pager_text);

        sTitle.setText("" + (position + 1));
        sText.setText("" + (position + 1));

        ((ViewPager) container).addView(v, 0);

        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        ((ViewPager) container).removeView((View) view);
    }

}
