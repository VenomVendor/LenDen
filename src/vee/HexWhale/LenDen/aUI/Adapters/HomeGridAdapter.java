/**
 * ***Copyright(c) : 2014-Present, VenomVendor.***
 * Author : VenomVendor
 * Dated : 17 Feb, 2014 3:28:58 AM
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
import vee.HexWhale.LenDen.Parsers.Categories.Response;
import vee.HexWhale.LenDen.Utils.Constants.API.IMAGEURL;
import vee.HexWhale.LenDen.Utils.Constants.API.URL;
import vee.HexWhale.LenDen.bg.Threads.GetData;

import java.io.File;
import java.util.List;

public class HomeGridAdapter extends BaseAdapter {
    Activity sActivity;
    DisplayImageOptions options;
    File cacheDir = new File(Environment.getExternalStorageDirectory(), "data/.vee.HexWhale.LenDen/.imgCache");
    ImageLoader imageLoader;
    List<Response> response;

    public HomeGridAdapter(Activity activity) {
        sActivity = activity;
        initilizeImageCache();
    }

    public HomeGridAdapter(Activity activity, List<Response> response) {
        sActivity = activity;
        this.response = response;
        initilizeImageCache();
    }

    @Override
    public int getCount() {
        if (response != null)
        {
            return response.size();
        }
        return 0;

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
        // View sView = convertView;
        ViewHolder holder;
        if (convertView == null) {
            final LayoutInflater layoutInflater = (LayoutInflater) sActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.home_grid, null);
            holder = new ViewHolder();
            holder.imView = (ImageView) convertView.findViewById(R.id.home_frnt_grid_img);
            holder.txtView = (TextView) convertView.findViewById(R.id.home_frnt_grid_txt);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        // holder.imView.setImageResource(Constants.gridImages[position %
        // Constants.gridImages.length]);

        final String r = GetData.getUrl(URL.CATEGORIES + response.get(position).getCategory_id() + IMAGEURL.PICTURE);

        imageLoader.displayImage(r, holder.imView, options);
        holder.txtView.setText(response.get(position).getCategory_name());

        return convertView;
    }

    private void initilizeImageCache() {
        options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.ic_launcher).showImageOnFail(R.drawable.ic_launcher)
                .resetViewBeforeLoading(true).cacheInMemory(true).cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).displayer(new RoundedBitmapDisplayer(10)).displayer(new FadeInBitmapDisplayer(0)).build();

        final ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(sActivity.getApplicationContext())
        .defaultDisplayImageOptions(options)
        .threadPriority(Thread.NORM_PRIORITY).threadPoolSize(3).denyCacheImageMultipleSizesInMemory()
        .discCache(new UnlimitedDiscCache(cacheDir))
        // .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
        .tasksProcessingOrder(QueueProcessingType.FIFO).writeDebugLogs() // TODO
        // Remove
        // for
        // release
        // app
        .build();
        ImageLoader.getInstance().init(config); // Do it on Application start
        imageLoader = ImageLoader.getInstance();
    }

    private static class ViewHolder {
        ImageView imView;
        TextView txtView;
    }
}
