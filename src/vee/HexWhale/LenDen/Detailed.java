/**
 * ***Copyright(c) : 2014-Present, VenomVendor.***
 * Author : VenomVendor
 * Dated : 18 Feb, 2014 4:25:32 PM
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

package vee.HexWhale.LenDen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.utils.L;

import org.json.JSONException;
import org.json.JSONObject;

import vee.HexWhale.LenDen.Parsers.DetailedCategory.GetDetailedCategory;
import vee.HexWhale.LenDen.Parsers.ItemCategory.GetItemCategory;
import vee.HexWhale.LenDen.Parsers.ItemCategory.Items;
import vee.HexWhale.LenDen.Storage.SettersNGetters;
import vee.HexWhale.LenDen.Utils.Constants.API.IMAGEURL;
import vee.HexWhale.LenDen.Utils.Constants.API.STRING;
import vee.HexWhale.LenDen.Utils.Constants.API.TYPE;
import vee.HexWhale.LenDen.Utils.Constants.API.URL;
import vee.HexWhale.LenDen.aUI.NoScrollWebView;
import vee.HexWhale.LenDen.aUI.NoScrollWebView.WebViewSizeChanged;
import vee.HexWhale.LenDen.aUI.Pagers.DetailedPager;
import vee.HexWhale.LenDen.bg.Threads.FetcherListener;
import vee.HexWhale.LenDen.bg.Threads.GetData;
import vee.HexWhale.LenDen.bg.Threads.GetDataFromUrl;
import vee.HexWhale.LenDen.bg.Threads.NetworkConnection;
import vee.HexWhale.LenDen.bg.Threads.TagGen;
import vee.HexWhale.LenDen.viewpagerindicator.CirclePageIndicator;

import java.io.File;
import java.util.Locale;

public class Detailed extends FragmentActivity implements WebViewSizeChanged {
    NoScrollWebView mWebView;
    ViewPager mPager;
    int mPosi = 0;
    private String sItemId;
    private TextView mFavCnt, mLikeCnt, mUserName, mTitle, mCategory, mPrice;
    GetDataFromUrl mDataFromUrl;
    private ImageView mDp;
    GetItemCategory mItemCategory;
    GetDetailedCategory mDetailedCategory;
    DisplayImageOptions optionsDp;
    File cacheDir = new File(Environment.getExternalStorageDirectory(), STRING.CACHE_LOC);
    ImageLoader imageLoader;
    Items mItems;
    vee.HexWhale.LenDen.Parsers.DetailedCategory.Items mDetailedItems;
    private String tag = "UNKNOWN";

    @SuppressLint("SetJavaScriptEnabled")
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        tag = TagGen.getTag(getClass());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed);

        ((ImageView) findViewById(R.id.menu_right)).setImageResource(R.drawable.detailed_up);
        initilizeImageCache();
        mWebView = (NoScrollWebView) findViewById(R.id.webiew);

        mFavCnt = (TextView) findViewById(R.id.detailed_list_fav);
        mLikeCnt = (TextView) findViewById(R.id.detailed_list_like);
        mUserName = (TextView) findViewById(R.id.detailed_uname);
        mTitle = (TextView) findViewById(R.id.detailed_list_title);
        mCategory = (TextView) findViewById(R.id.detailed_list_categ);
        mPrice = (TextView) findViewById(R.id.detailed_price);
        mPager = (ViewPager) findViewById(R.id.pager);

        mDp = (ImageView) findViewById(R.id.detailed_dp);

        if (!NetworkConnection.isAvail(getApplicationContext())) {
            ToastL("No internet Connection");
            return;
        }

        final Intent mIntent = getIntent();

        if (mIntent != null)
        {
            mPosi = mIntent.getIntExtra(STRING.POSITION, 0);
        }

        mItemCategory = SettersNGetters.getItemCategory();

        if (mItemCategory == null)
        {
            ToastL("Unknown Error");
            finish();
            return;

        }

        mItems = mItemCategory.getResponse().getItems().get(mPosi);

        sItemId = mItems.getItem_id();
        final CirclePageIndicator mCirclePageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mPager.setAdapter(new DetailedPager(this, sItemId));
        mCirclePageIndicator.setViewPager(mPager);

        mFavCnt.setText("" + mItems.getFavorite_count());
        mLikeCnt.setText("" + mItems.getLike_count());
        mUserName.setText("" + mItems.getUser_first_name());
        mTitle.setText("" + mItems.getTitle());
        mCategory.setText("" + mItems.getCategory_name());
        mPrice.setText("$" + mItems.getSelling_price());
        imageLoader.displayImage("" + GetData.getImageUrl(IMAGEURL.DP + mItems.getUser_id()), mDp, optionsDp);

        setWebViewUI();
        mDataFromUrl = new GetDataFromUrl(this, mFetcherListener);

        ((TextView) findViewById(R.id.menu_center)).setText(("" + mItems.getTitle()).toUpperCase(Locale.UK));

        mDataFromUrl.setAccessToken();
        mDataFromUrl.GetString(TYPE.ITEM_DETAILS, getBody(TYPE.ITEM_DETAILS), GetData.getUrl(URL.ITEM_DETAILS));

        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(webViewClient);
        mWebView.setVerticalScrollBarEnabled(true);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.scrollTo(0, 0);
        mWebView.setFocusable(true);
        final WebSettings settings = mWebView.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setNeedInitialFocus(true);
        settings.setJavaScriptEnabled(true);
        settings.setUserAgentString("Android");
        settings.setDomStorageEnabled(true);
        settings.setAppCachePath(getApplicationContext().getFilesDir().getPath() + "/" + getPackageName() + "/cache");

        System.out.println("++++++++++++++++++++++++++++++++");
        System.out.println(getApplicationContext().getFilesDir().getPath() + "/" + getPackageName() + "/cache");
        System.out.println("++++++++++++++++++++++++++++++++");

        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setRenderPriority(RenderPriority.HIGH);
        settings.setPluginState(PluginState.ON);
        settings.setSavePassword(false);
        settings.setAppCacheMaxSize(1024 * 1024 * 8);
        mWebView.setWebViewSizeChanged(this);
    }

    private String getBody(int tokenType) {
        JSONObject mJsonObject = null;
        mJsonObject = new JSONObject();
        try {
            mJsonObject.put(STRING.ITEM_ID, sItemId);
            return mJsonObject.toString();
        }
        catch (final JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    private final FetcherListener mFetcherListener = new FetcherListener()
    {

        @Override
        public void errorFetching(int type, VolleyError error) {
            try {
                error.printStackTrace();
                LogR("---" + "Error");
            }
            finally {
                ToastL("Error");
            }

        }

        @Override
        public void finishedFetching(int type, String response) {
            LogR("+++ " + response);
        }

        @Override
        public void beforeParsing(int type) {
            // TODO Auto-generated method stub

        }

        @Override
        public void startedParsing(int type) {
            // TODO Auto-generated method stub

        }

        @Override
        public void ParsingException(Exception e) {
            e.printStackTrace();
        }

        @Override
        public void finishedParsing(int typ) {
            // if (typ == TYPE.) {

            // XXX TODO STart here
            mDetailedCategory = SettersNGetters.getDetailedCategory();
            if (mDetailedCategory == null)
            {
                ToastL("Unknown Error");
                finish();
                return;

            }

            mDetailedItems = mDetailedCategory.getResponse().getItems().get(0);

            setWebViewUI();
        }

        @Override
        public void tokenError(String tokenError) {
            ToastL(tokenError);
        }

    };
    private final WebViewClient webViewClient = new WebViewClient() {

        @Override
        public void onPageStarted(final WebView view, final String url, final Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(final WebView view, final String url) {
            Log.wtf("", url);
        }

        @Override
        public void onReceivedError(final WebView view, final int errorCode, final String description, final String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

            System.out.println("errorCode " + errorCode);
            System.out.println("description " + description);
            System.out.println("failingUrl " + failingUrl);

            Toast.makeText(getApplicationContext(), description, Toast.LENGTH_SHORT).show();
            // webView.loadUrl(url);
        }

    };

    public void Finish(View v) {
        finish();
        AnimPrev();
    }

    protected void setWebViewUI() {
        final String Starting = "<!DOCTYPE html><html><head><style type=\"text/css\">div#veecontent {text-align:justify;padding:1px 10px 20px;}</style></head><body><div id=\"veecontent\">";

        final String contentToLoad = "\t" + mItems.getDescription() + " \r\n";

        final String Ending = "</div></body></html>";

        mWebView.loadDataWithBaseURL("file:///android_asset/", Starting + contentToLoad + Ending, "text/html", "UTF-8", null);

    }

    /*******************************************************************/
    /**
     * @param RED
     */
    private void LogR(String msg) {
        Log.wtf(tag, msg);
    }

    /**
     * @param text
     */
    private void ToastL(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    /*******************************************************************/

    public void Submit(View v) {
        finish();
        AnimNext();
    }

    @Override
    public void onBackPressed() {
        finish();
        AnimPrev();
    }

    private void AnimPrev() {
        overridePendingTransition(R.anim.android_slide_in_left, R.anim.android_slide_out_right);
        return;
    }

    private void AnimNext() {
        overridePendingTransition(R.anim.enter, R.anim.exit);
        return;
    }

    @Override
    public void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        System.out.println("XTXwidth " + width);
        System.out.println("XTXheight " + height);
        System.out.println("XTXoldWidth " + oldWidth);
        System.out.println("XTXoldHeight " + oldHeight);
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

        final ImageLoaderConfiguration configDP = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(optionsDp)
                .threadPriority(Thread.NORM_PRIORITY)
                .threadPoolSize(3)
                .denyCacheImageMultipleSizesInMemory()
                .discCache(new UnlimitedDiscCache(cacheDir))
                // .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .build();

        ImageLoader.getInstance().init(configDP); // Do it on Application start
        imageLoader = ImageLoader.getInstance();
    }
}
