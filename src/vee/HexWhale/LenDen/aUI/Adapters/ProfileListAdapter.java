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

package vee.HexWhale.LenDen.aUI.Adapters;

import vee.HexWhale.LenDen.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileListAdapter extends BaseAdapter {

    Activity sActivity;

    public ProfileListAdapter(Activity activity) {
        this.sActivity = activity;
    }

    @Override
    public int getCount() {

        return 10;
    }

    @Override
    public Object getItem(int position) {

        return position;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) sActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.search_list, null);
            holder = new ViewHolder();
            holder.sImg = (ImageView) convertView.findViewById(R.id.search_list_img);
            holder.sTitle = (TextView) convertView.findViewById(R.id.search_list_title);
            holder.sType = (TextView) convertView.findViewById(R.id.search_list_type);
            holder.sPrice = (TextView) convertView.findViewById(R.id.search_list_price);
            holder.sFav = (TextView) convertView.findViewById(R.id.search_list_fav);
            holder.sCmt = (TextView) convertView.findViewById(R.id.search_list_cmt);
            holder.sStr = (TextView) convertView.findViewById(R.id.search_list_str);
            holder.sBtn = (TextView) convertView.findViewById(R.id.search_list_type_btn);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position % 2 == 1) {
            holder.sTitle.setText("Am Loooooooooooooooooooooooooooooooooong title");
            holder.sBtn.setText("SALE");
            holder.sBtn.setBackgroundResource(R.drawable.exch_rnd_bg);
            holder.sPrice.setText("$45");
            holder.sPrice.setTextColor(convertView.getResources().getColor(R.color.orange));
        } else {
            holder.sTitle.setText("Am small title");
            holder.sBtn.setText("EXCHANGE");
            holder.sBtn.setBackgroundResource(R.drawable.sales_rnd_bg);
            holder.sPrice.setTextColor(convertView.getResources().getColor(R.color.menu_bg));
        }

        holder.sTitle.setSelected(true);

        return convertView;
    }

    private static class ViewHolder {
        public ImageView sImg;
        public TextView sTitle;
        public TextView sType;
        public TextView sPrice;
        public TextView sFav;
        public TextView sCmt;
        public TextView sStr;
        public TextView sBtn;
    }
}
