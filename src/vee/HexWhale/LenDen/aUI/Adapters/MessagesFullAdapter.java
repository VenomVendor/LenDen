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

package vee.HexWhale.LenDen.aUI.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import vee.HexWhale.LenDen.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MessagesFullAdapter extends BaseAdapter {

    Activity sActivity;

    public MessagesFullAdapter(Activity activity) {
        this.sActivity = activity;
    }

    @Override
    public int getCount() {
        return 100;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            final LayoutInflater mInflater = (LayoutInflater) this.sActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.messages_full_list, null);

            holder.mRecDP = (ImageView) convertView.findViewById(R.id.rec_dp);
            holder.mMeDP = (ImageView) convertView.findViewById(R.id.me_dp);

            holder.mMeTime = (TextView) convertView.findViewById(R.id.me_timestamp);
            holder.mRecTime = (TextView) convertView.findViewById(R.id.rec_timestamp);

            holder.mMeName = (TextView) convertView.findViewById(R.id.me_name);
            holder.mRecName = (TextView) convertView.findViewById(R.id.rec_name);

            holder.mMeMsg = (TextView) convertView.findViewById(R.id.me_msg);
            holder.mRecMsg = (TextView) convertView.findViewById(R.id.rec_msg);

            holder.mMeLyt = (RelativeLayout) convertView.findViewById(R.id.msg_me_lyt);
            holder.mRecLyt = (RelativeLayout) convertView.findViewById(R.id.msg_rec_lyt);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position % 2 == 0) {
            holder.mMeMsg
            .setText("C was created by Dennis Ritchie. Brian Kernighan wrote the first C tutorial. The authors came together to write the book in conjunction with the language");
            holder.mRecMsg.setText("Ok, Can I learn it from you?");
        } else {
            holder.mRecMsg
            .setText("Dennis RitchieHe created the C programming language and, with long-time colleague Ken ... His father was Alistair E. Ritchie, a longtime Bell Labs scientist");
            holder.mMeMsg.setText("C is one of the most widely used programming languages of all time");

        }

        holder.mMeTime.setText(this.getCustomTime());
        holder.mRecTime.setText(this.getCustomTime());

        // if (position % 5 == 0) {
        // holder.mRecLyt.setVisibility(View.GONE);
        // }
        //
        // if (position % 8 == 0) {
        // holder.mRecLyt.setVisibility(View.GONE);
        // }

        return convertView;
    }

    public String getCustomTime() {

        final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yy hh:mm:ss a", Locale.UK);

        final Date resultdate = new Date(System.currentTimeMillis());

        final String date = dateFormat.format(resultdate);

        return date;

        // return new SimpleDateFormat("MMM dd, yy hh:mm:ss a",
        // Locale.UK).format(new Date(System.currentTimeMillis()));
    }

    public class ViewHolder {
        TextView mRecName, mMeName, mRecMsg, mMeMsg, mRecTime, mMeTime;
        ImageView mRecDP, mMeDP;
        RelativeLayout mRecLyt, mMeLyt;
    }

}
