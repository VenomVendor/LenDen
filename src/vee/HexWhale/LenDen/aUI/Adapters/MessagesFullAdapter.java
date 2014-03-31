/**
 * ***Copyright(c)  :   2014-Present, VenomVendor.***
 * Author           :   VenomVendor
 * Dated            :   21 Feb, 2014 4:41:51 PM
 * Project          :   LenDen-Android
 * Client           :   LenDen
 * Contact          :   info@VenomVendor.com
 * URL              :   https://www.google.co.in/search?q=VenomVendor
 * Copyright(c)     :   2014-Present, VenomVendor.
 * License          :   This work is licensed under Attribution-NonCommercial 3.0 Unported (CC BY-NC 3.0).
 *                      License info at http://creativecommons.org/licenses/by-nc/3.0/deed.en_US
 *                      Read More at http://creativecommons.org/licenses/by-nc/3.0/legalcode
 **/

package vee.HexWhale.LenDen.aUI.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.utils.L;

import vee.HexWhale.LenDen.R;
import vee.HexWhale.LenDen.Parsers.MessagesFull.GetMessagesFull;
import vee.HexWhale.LenDen.Parsers.MessagesFull.Messages_list;
import vee.HexWhale.LenDen.Utils.Constants.API.IMAGEURL;
import vee.HexWhale.LenDen.Utils.Constants.API.STRING;
import vee.HexWhale.LenDen.bg.Threads.GetData;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessagesFullAdapter extends BaseAdapter {

    Activity sActivity;
    GetMessagesFull sMessagesFull;
    List<Messages_list> mSingleFullMesg;

    DisplayImageOptions optionsDp;
    File cacheDir = new File(Environment.getExternalStorageDirectory(), STRING.CACHE_LOC);
    ImageLoader imageLoader;
    String ZerothMsgId = "";

    public MessagesFullAdapter(Activity activity) {
        sActivity = activity;
        initilizeImageCache();
    }

    public MessagesFullAdapter(Activity activity, GetMessagesFull messagesFull) {
        sActivity = activity;
        sMessagesFull = messagesFull;
        if (sMessagesFull == null)
        {
            mSingleFullMesg = null;

        }
        else {
            mSingleFullMesg = sMessagesFull.getResponse().getMessages_list();
            ZerothMsgId = mSingleFullMesg.get(0).getUser_id();
        }

        initilizeImageCache();
    }

    @Override
    public int getCount() {
        if (mSingleFullMesg == null)
        {
            return 0;
        }
        return mSingleFullMesg.size();
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
        final Messages_list mOneSingleFullMsg = mSingleFullMesg.get(position);
        if (convertView == null) {

            holder = new ViewHolder();
            final LayoutInflater mInflater = (LayoutInflater) sActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (ZerothMsgId.equalsIgnoreCase(mOneSingleFullMsg.getUser_id())) {
            holder.mMeMsg.setText("" + mOneSingleFullMsg.getMessage());
            holder.mMeName.setText("" + mOneSingleFullMsg.getUser_first_name());
            holder.mMeTime.setText("" + mOneSingleFullMsg.getDate_time());
            // TODO-DP
            imageLoader.displayImage("" + GetData.getUrl(IMAGEURL.DP + mOneSingleFullMsg.getUser_id()), holder.mMeDP, optionsDp);

            holder.mRecLyt.setVisibility(View.GONE);

        }
        else {
            holder.mRecMsg.setText("" + mOneSingleFullMsg.getMessage());
            holder.mRecName.setText("" + mOneSingleFullMsg.getUser_first_name());
            holder.mRecTime.setText("" + mOneSingleFullMsg.getDate_time());
            // TODO-mRecDP
            imageLoader.displayImage("" + GetData.getUrl(IMAGEURL.DP + mOneSingleFullMsg.getUser_id()), holder.mRecDP, optionsDp);

            holder.mMeLyt.setVisibility(View.GONE);

        }

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

    private void initilizeImageCache() {
        L.disableLogging();
        optionsDp =
                new DisplayImageOptions.Builder()
                        .showImageForEmptyUri(R.drawable.signup_dp)
                        .showImageOnFail(R.drawable.signup_dp)
                        .resetViewBeforeLoading(false)
                        .cacheInMemory(true)
                        .cacheOnDisc(true)
                        .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        .displayer(new RoundedBitmapDisplayer(10))
                        .displayer(new FadeInBitmapDisplayer(0))
                        .build();

        final ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(sActivity.getApplicationContext())
                .defaultDisplayImageOptions(optionsDp)
                .threadPriority(Thread.NORM_PRIORITY)
                .threadPoolSize(3)
                .denyCacheImageMultipleSizesInMemory()
                .discCache(new UnlimitedDiscCache(cacheDir))
                // .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .build();

        ImageLoader.getInstance().init(config); // Do it on Application start
        imageLoader = ImageLoader.getInstance();

    }

}
