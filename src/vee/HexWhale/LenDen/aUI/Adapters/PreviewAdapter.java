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
import vee.HexWhale.LenDen.Parsers.ItemCategory.GetItemCategory;
import vee.HexWhale.LenDen.Parsers.ItemCategory.Items;
import vee.HexWhale.LenDen.Utils.Constants.API.IMAGEURL;
import vee.HexWhale.LenDen.bg.Threads.GetData;

import java.io.File;

public class PreviewAdapter extends BaseAdapter {

    Activity sActivity;
    GetItemCategory mItemCategory;
    static int totalCount;
    static int currentPage;
    static int totalItemCount;

    DisplayImageOptions optionsIcon, optionsDp;
    File cacheDir = new File(Environment.getExternalStorageDirectory(), "data/.vee.HexWhale.LenDen/.imgCache");
    ImageLoader imageLoader;

    public PreviewAdapter(Activity activity) {
        sActivity = activity;
        initilizeImageCache();
    }

    public PreviewAdapter(Activity activity, GetItemCategory mItemCategory) {
        sActivity = activity;
        this.mItemCategory = mItemCategory;
        initilizeImageCache();
    }

    @Override
    public int getCount() {

        if (mItemCategory == null)
        {
            PreviewAdapter.totalCount = 0;
        }
        else {
            PreviewAdapter.currentPage = mItemCategory.getResponse().getCurrent_page();
            PreviewAdapter.totalItemCount = mItemCategory.getResponse().getTotal_item_count();
            PreviewAdapter.totalCount = mItemCategory.getResponse().getItems().size();
        }
        return PreviewAdapter.totalCount;
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
            convertView = mInflater.inflate(R.layout.preview_list, null);

            holder.mDP = (ImageView) convertView.findViewById(R.id.preview_dp);
            holder.mName = (TextView) convertView.findViewById(R.id.preview_name);
            holder.mTime = (TextView) convertView.findViewById(R.id.preview_time);
            holder.mType = (ImageView) convertView.findViewById(R.id.preview_type);

            holder.mIcon = (ImageView) convertView.findViewById(R.id.search_list_img);
            holder.mTitle = (TextView) convertView.findViewById(R.id.search_list_title);
            holder.mCategory = (TextView) convertView.findViewById(R.id.search_list_type); // mDescprtion
            holder.mPrice = (TextView) convertView.findViewById(R.id.search_list_price);

            holder.mLikeCnt = (TextView) convertView.findViewById(R.id.search_list_lik);
            holder.mFavCnt = (TextView) convertView.findViewById(R.id.search_list_fav);

            holder.mTypeButton = (TextView) convertView.findViewById(R.id.search_list_type_btn);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Items mItems = mItemCategory.getResponse().getItems().get(position);
        int mTadeMode = mItems.getTrade_mode();
        mTadeMode = 2; // TODO - REMOVE THIS HARDCODED MODE

        holder.mName.setText("" + mItems.getUser_first_name());

        holder.mTitle.setText("" + mItems.getTitle());
        holder.mCategory.setText("" + mItems.getCategory_name());
        holder.mTime.setText("" + mItems.getCreation_date());

        holder.mFavCnt.setText("" + mItems.getFavorite_count());
        holder.mLikeCnt.setText("" + mItems.getLike_count());

        // if (position > (totalCount - 2) && !noMoreTopics)
        // /items/<item_id>/picture/<num:int>/
        imageLoader.displayImage("" + GetData.getUrl("items/" + mItems.getItem_id() + "/picture/" + 1), holder.mIcon, optionsIcon);

        // /users/photo/<user_id>/
        imageLoader.displayImage("" + GetData.getUrl(IMAGEURL.DP + mItems.getUser_id()), holder.mDP, optionsDp);
        holder.mPrice.setText("$" + mItems.getSelling_price());

        if (mTadeMode == 1) {
            holder.mTypeButton.setText("SELL");
            holder.mTypeButton.setBackgroundResource(R.drawable.sales_rnd_bg);
            holder.mPrice.setTextColor(convertView.getResources().getColor(R.color.menu_bg));
            holder.mType.setImageResource(R.drawable.pyth_sale);
        }
        else
            if (mTadeMode == 2) {
                holder.mTypeButton.setText("EXCHANGE");
                holder.mTypeButton.setBackgroundResource(R.drawable.exch_rnd_bg);
                holder.mPrice.setTextColor(convertView.getResources().getColor(R.color.orange));
                holder.mType.setImageResource(R.drawable.pyth_exchange);
            }
            else {
                holder.mTypeButton.setText("BOTH");
                holder.mTypeButton.setBackgroundResource(R.drawable.both_rnd_bg);
                holder.mPrice.setTextColor(convertView.getResources().getColor(R.color.saani));
                holder.mType.setImageResource(R.drawable.pyth_both);
            }

        return convertView;
    }

    private static class ViewHolder {

        TextView mName, mTime, mTitle, mCategory, mPrice, mFavCnt, mLikeCnt, mTypeButton;
        ImageView mDP, mType, mIcon;

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
