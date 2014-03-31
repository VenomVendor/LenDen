/**
 * ***Copyright(c) : 2014-Present, VenomVendor.***
 * Author : VenomVendor
 * Dated : 21 Feb, 2014 4:41:51 PM
 * Project : LenDen-Android
 * Client : LenDen
 * Contact : info@VenomVendor.com
 * URL : https://www.google.co.in/search?q=VenomVendor
 * Copyright(c) : 2014-Present, VenomVendor.
 * License : This work is licensed under Attribution-NonCommercial 3.0 Unported
 * (CC BY-NC 3.0).
 * License info at http://creativecommons.org/licenses/by-nc/3.0/deed.en_US
 * Read More at http://creativecommons.org/licenses/by-nc/3.0/legalcode
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
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import vee.HexWhale.LenDen.R;
import vee.HexWhale.LenDen.Parsers.Messages.GetMessages;
import vee.HexWhale.LenDen.Parsers.Messages.Messages;
import vee.HexWhale.LenDen.Utils.Constants.API.IMAGEURL;
import vee.HexWhale.LenDen.Utils.Constants.API.STRING;
import vee.HexWhale.LenDen.bg.Threads.GetData;

import java.io.File;
import java.util.List;

public class MessagesAdapter extends BaseAdapter {

    Activity sActivity;
    GetMessages sMessages;
    List<Messages> bulkMessages;
    DisplayImageOptions optionsDp;
    File cacheDir = new File(Environment.getExternalStorageDirectory(), STRING.CACHE_LOC);
    ImageLoader imageLoader;

    public MessagesAdapter(Activity activity) {
        sActivity = activity;
        initilizeImageCache();
    }

    public MessagesAdapter(Activity activity, GetMessages mMessages) {
        sActivity = activity;
        sMessages = mMessages;
        if (sMessages == null)
        {
            bulkMessages = null;

        }
        else {
            bulkMessages = sMessages.getResponse().getMessages();
        }
        initilizeImageCache();

    }

    @Override
    public int getCount() {
        if (bulkMessages == null)
        {
            return 0;
        }
        return bulkMessages.size();
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
            final LayoutInflater mInflater = (LayoutInflater) sActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.messages_list, null);
            holder.sName = (TextView) convertView.findViewById(R.id.msg_list_name);
            holder.sTitle = (TextView) convertView.findViewById(R.id.msg_list_title);
            holder.sMessage = (TextView) convertView.findViewById(R.id.msg_list_msg);
            holder.sTime = (TextView) convertView.findViewById(R.id.msg_list_time);
            holder.sUnRead = (TextView) convertView.findViewById(R.id.msg_list_unread);
            holder.sDP = (ImageView) convertView.findViewById(R.id.msg_list_dp);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Messages individualMessage = bulkMessages.get(position);

        holder.sName.setText("" + individualMessage.getPartner_first_name());
        holder.sTitle.setText("" + individualMessage.getItem_title());
        holder.sMessage.setText("" + individualMessage.getLast_message());
        holder.sTime.setText("" + individualMessage.getLast_message_date_time());
        holder.sUnRead.setText("" + individualMessage.getUnread_messages());

        if(individualMessage.getUnread_messages()==0)
        {
            holder.sUnRead.setVisibility(View.INVISIBLE);
        }

        // /users/photo/<user_id>/
        imageLoader.displayImage("" + GetData.getUrl(IMAGEURL.DP + individualMessage.getPartner_id()), holder.sDP, optionsDp);
        return convertView;
    }

    public class ViewHolder {
        private ImageView sDP;
        private TextView sUnRead, sName, sMessage, sTitle, sTime;
    }

    private void initilizeImageCache() {
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
