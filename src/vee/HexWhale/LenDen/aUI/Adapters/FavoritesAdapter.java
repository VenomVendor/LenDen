/**
 * ***Copyright(c) : 2014-Present, VenomVendor.***
 * Author : VenomVendor
 * Dated : 17 Feb, 2014 3:28:57 AM
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
import vee.HexWhale.LenDen.Parsers.FavCategory.GetFavCategory;
import vee.HexWhale.LenDen.Parsers.FavCategory.Items;
import vee.HexWhale.LenDen.Utils.Constants.API.IMAGEURL;
import vee.HexWhale.LenDen.Utils.Constants.API.STRING;
import vee.HexWhale.LenDen.bg.Threads.GetData;

import java.io.File;
import java.util.List;

public class FavoritesAdapter extends BaseAdapter {

    Activity sActivity;
    GetFavCategory sFavCategory;
    List<Items> slist;

    DisplayImageOptions optionsIcon, optionsDp;
    File cacheDir = new File(Environment.getExternalStorageDirectory(), STRING.CACHE_LOC);
    ImageLoader imageLoader;

    public FavoritesAdapter(Activity activity) {
        sActivity = activity;
        initilizeImageCache();
    }

    public FavoritesAdapter(Activity activity, GetFavCategory mFavCategory) {
        sActivity = activity;
        sFavCategory = mFavCategory;
        if (sFavCategory == null)
        {
            slist = null;
        }
        else {
            slist = sFavCategory.getResponse().getItems();
        }
        initilizeImageCache();
    }

    @Override
    public int getCount() {
        if (slist == null)
        {
            return 0;
        }

        return slist.size();
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

            holder = new ViewHolder();
            final LayoutInflater mInflater = (LayoutInflater) sActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.favorites_list, null);

            holder.sDP = (ImageView) convertView.findViewById(R.id.preview_dp);
            holder.sName = (TextView) convertView.findViewById(R.id.preview_name);
            holder.sTime = (TextView) convertView.findViewById(R.id.preview_time);
            holder.sType = (ImageView) convertView.findViewById(R.id.preview_type);

            holder.sIcon = (ImageView) convertView.findViewById(R.id.search_list_img);
            holder.sTitle = (TextView) convertView.findViewById(R.id.search_list_title);
            holder.sCategory = (TextView) convertView.findViewById(R.id.search_list_type); // mDescprtion
            holder.sPrice = (TextView) convertView.findViewById(R.id.search_list_price);

            holder.sLikeCnt = (TextView) convertView.findViewById(R.id.search_list_lik);
            holder.sFavCnt = (TextView) convertView.findViewById(R.id.search_list_fav);

            holder.sTypeButton = (TextView) convertView.findViewById(R.id.search_list_type_btn);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Items mItems = sFavCategory.getResponse().getItems().get(position);
        int mTadeMode = mItems.getTrade_mode();
        mTadeMode = 2; // TODO - REMOVE THIS HARDCODED MODE

        holder.sName.setText("" + mItems.getUser_first_name());

        holder.sTitle.setText("" + mItems.getTitle());
        holder.sCategory.setText("" + mItems.getCategory_name());
        holder.sTime.setText("" + mItems.getCreation_date());

        holder.sFavCnt.setText("" + mItems.getFavorite_count());
        holder.sLikeCnt.setText("" + mItems.getLike_count());

        // if (position > (totalCount - 2) && !noMoreTopics)
        // /items/<item_id>/picture/<num:int>/
        imageLoader.displayImage("" + GetData.getUrl("items/" + mItems.getItem_id() + "/picture/" + 1), holder.sIcon, optionsIcon);

        // /users/photo/<user_id>/
        imageLoader.displayImage("" + GetData.getUrl(IMAGEURL.DP + mItems.getUser_id()), holder.sDP, optionsDp);
        holder.sPrice.setText("$" + mItems.getSelling_price());

        if (mTadeMode == 1) {
            holder.sTypeButton.setText("SELL");
            holder.sTypeButton.setBackgroundResource(R.drawable.sales_rnd_bg);
            holder.sPrice.setTextColor(convertView.getResources().getColor(R.color.menu_bg));
            holder.sType.setImageResource(R.drawable.pyth_sale);
        }
        else
            if (mTadeMode == 2) {
                holder.sTypeButton.setText("EXCHANGE");
                holder.sTypeButton.setBackgroundResource(R.drawable.exch_rnd_bg);
                holder.sPrice.setTextColor(convertView.getResources().getColor(R.color.orange));
                holder.sType.setImageResource(R.drawable.pyth_exchange);
            }
            else {
                holder.sTypeButton.setText("BOTH");
                holder.sTypeButton.setBackgroundResource(R.drawable.both_rnd_bg);
                holder.sPrice.setTextColor(convertView.getResources().getColor(R.color.saani));
                holder.sType.setImageResource(R.drawable.pyth_both);
            }
        holder.sTitle.setSelected(true);
        return convertView;
    }

    private static class ViewHolder {

        TextView sName, sTime, sTitle, sCategory, sPrice, sFavCnt, sLikeCnt, sTypeButton;
        ImageView sDP, sType, sIcon;

    }

    private void initilizeImageCache() {
        optionsIcon =
                new DisplayImageOptions.Builder()
                        .showImageForEmptyUri(R.drawable.noimage)
                        .showImageOnFail(R.drawable.noimage)
                        .resetViewBeforeLoading(false)
                        .cacheInMemory(true)
                        .cacheOnDisc(true)
                        .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        .displayer(new RoundedBitmapDisplayer(10))
                        .displayer(new FadeInBitmapDisplayer(0))
                        .build();

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
                .defaultDisplayImageOptions(optionsIcon)
                .threadPriority(Thread.NORM_PRIORITY)
                .threadPoolSize(3)
                .denyCacheImageMultipleSizesInMemory()
                .discCache(new UnlimitedDiscCache(cacheDir))
                // .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .build();

        final ImageLoaderConfiguration configDP = new ImageLoaderConfiguration.Builder(sActivity.getApplicationContext())
                .defaultDisplayImageOptions(optionsDp)
                .threadPriority(Thread.NORM_PRIORITY)
                .threadPoolSize(3)
                .denyCacheImageMultipleSizesInMemory()
                .discCache(new UnlimitedDiscCache(cacheDir))
                // .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .build();

        ImageLoader.getInstance().init(config); // Do it on Application start
        ImageLoader.getInstance().init(configDP); // Do it on Application start
        imageLoader = ImageLoader.getInstance();
    }
}
