/**
 * ***Copyright(c)  :   2014-Present, VenomVendor.***
 * Author           :   VenomVendor
 * Dated            :   17 Feb, 2014 3:28:57 AM
 * Project          :   LenDen-Android
 * Client           :   LenDen
 * Contact          :   info@VenomVendor.com
 * URL              :   https://www.google.co.in/search?q=VenomVendor
 * Copyright(c)     :   2014-Present, VenomVendor.
 * License          :   This work is licensed under Attribution-NonCommercial 3.0 Unported (CC BY-NC 3.0).
 *                      License info at http://creativecommons.org/licenses/by-nc/3.0/deed.en_US
 *                      Read More at http://creativecommons.org/licenses/by-nc/3.0/legalcode
 **/


package vee.HexWhale.LenDen.aUI.Pagers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
import vee.HexWhale.LenDen.Utils.Constants.API.IMAGEURL;
import vee.HexWhale.LenDen.Utils.Constants.API.STRING;
import vee.HexWhale.LenDen.bg.Threads.GetData;

import java.io.File;

public class DetailedPager extends PagerAdapter {

    Activity sActivity;
    String itemId;
    DisplayImageOptions options;
    final File cacheDir = new File(Environment.getExternalStorageDirectory(), STRING.CACHE_LOC);
    ImageLoader imageLoader;

    public DetailedPager(Activity act, String itemId) {
        sActivity = act;
        this.itemId = itemId;
        initilizeImageCache();
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

        final View sView = inflater.inflate(R.layout.detailed_pager, null);

        final ImageView sImg = (ImageView) sView.findViewById(R.id.detailed_pager_img);

        imageLoader.displayImage("" + GetData.getImageUrl("items/" + itemId + IMAGEURL.PICTURE + (position + 1)), sImg, options);

        ((ViewPager) container).addView(sView, 0);

        return sView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        ((ViewPager) container).removeView((View) view);
    }

    private void initilizeImageCache() {
	L.disableLogging();
        options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.noimage).showImageOnFail(R.drawable.noimage)
                .resetViewBeforeLoading(false).cacheInMemory(true).cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY)
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

}
