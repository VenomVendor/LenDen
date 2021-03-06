/**
 * ***Copyright(c)	:	2014-Present, VenomVendor.***
 * Author			:	VenomVendor
 * Dated			:	17 Feb, 2014 3:28:57 AM
 * Project			:	LenDen-Android
 * Client			:	LenDen
 * Contact			:	info@VenomVendor.com
 * URL				:	https://www.google.co.in/search?q=VenomVendor
 * Copyright(c)		:	2014-Present, VenomVendor.
 * License			:	This work is licensed under Attribution-NonCommercial 3.0 Unported
 *						License info at http://creativecommons.org/licenses/by-nc/3.0/deed.en_US
 *						Read More at http://creativecommons.org/licenses/by-nc/3.0/legalcode
 *


 **/

package vee.HexWhale.LenDen.aUI.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import vee.HexWhale.LenDen.R;
import vee.HexWhale.LenDen.Utils.Constants;

public class MenuDrawerAdapter extends BaseAdapter {

    Activity sActivity;

    public MenuDrawerAdapter(Activity activity) {
        sActivity = activity;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return Constants.menuImages.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            final LayoutInflater mInflater = (LayoutInflater) sActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.menu_drawer_list, null);
            holder.mTView = (TextView) convertView.findViewById(R.id.menu_drawer_list_txt);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTView.setText(Constants.menuText[position]);
        holder.mTView.setCompoundDrawablesWithIntrinsicBounds(0, Constants.menuImages[position], 0, 0);

        return convertView;
    }

    private static class ViewHolder {
        TextView mTView;
    }

}
