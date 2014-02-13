
package vee.HexWhale.LenDen.aUI;

import static vee.HexWhale.LenDen.Constants.gridImages;

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

import java.io.File;

public class HomeGridAdapter extends BaseAdapter {
    Activity sActivity;
    DisplayImageOptions options;
    File cacheDir = new File(Environment.getExternalStorageDirectory(), "data/.vee.HexWhale.LenDen/.imgCache");
    ImageLoader imageLoader;

    public HomeGridAdapter(Activity activity) {
        this.sActivity = activity;
        initilizeImageCache();
    }

    @Override
    public int getCount() {

        return (gridImages.length * 2);
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
        View sView = convertView;
        if (sView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) sActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            sView = layoutInflater.inflate(R.layout.home_grid, null);
        }

        final ImageView imView = (ImageView) sView.findViewById(R.id.home_frnt_grid_img);
        final TextView txtView = (TextView) sView.findViewById(R.id.home_frnt_grid_txt);

        imView.setImageResource(gridImages[position % gridImages.length]);

        // imageLoader.displayImage("drawable://" + (gridImages[position % gridImages.length]),
        // imView, options);
        txtView.setText("text-" + (position + 1));

        return sView;
    }

    private void initilizeImageCache() {
        options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.ic_launcher).showImageOnFail(R.drawable.ic_launcher)
                .resetViewBeforeLoading(true).cacheInMemory(true).cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).displayer(new RoundedBitmapDisplayer(10)).displayer(new FadeInBitmapDisplayer(0)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(sActivity.getApplicationContext()).defaultDisplayImageOptions(options)
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
