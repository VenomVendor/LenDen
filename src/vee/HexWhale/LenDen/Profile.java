/**
 * ***Copyright(c)	:	2014-Present, VenomVendor.***
 * Author			:	VenomVendor
 * Dated			:	17 Feb, 2014 3:28:58 AM
 * Project			:	LenDen-Android
 * Client			:	LenDen
 * Contact			:	info@VenomVendor.com
 * URL				:	https://www.google.co.in/search?q=VenomVendor
 * Copyright(c)		:	2014-Present, VenomVendor.
 * License			:	This work is licensed under Attribution-NonCommercial 3.0 Unported
 *						License info at http://creativecommons.org/licenses/by-nc/3.0/deed.en_US
 *						Read More at http://creativecommons.org/licenses/by-nc/3.0/legalcode
 *


 **/

package vee.HexWhale.LenDen;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.haarman.listviewanimations.swinginadapters.prepared.SwingRightInAnimationAdapter;
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

import vee.HexWhale.LenDen.Parsers.ItemStats.GetItemStats;
import vee.HexWhale.LenDen.Parsers.Profile.GetProfile;
import vee.HexWhale.LenDen.Parsers.ProfileItems.GetProfileItems;
import vee.HexWhale.LenDen.Storage.GlobalSharedPrefs;
import vee.HexWhale.LenDen.Storage.SettersNGetters;
import vee.HexWhale.LenDen.Utils.Constants.API.IMAGEURL;
import vee.HexWhale.LenDen.Utils.Constants.API.STRING;
import vee.HexWhale.LenDen.Utils.Constants.API.TYPE;
import vee.HexWhale.LenDen.Utils.Constants.API.URL;
import vee.HexWhale.LenDen.Utils.Constants.KEY;
import vee.HexWhale.LenDen.aUI.MenuBar;
import vee.HexWhale.LenDen.aUI.Adapters.ProfileListAdapter;
import vee.HexWhale.LenDen.bg.Threads.FetcherListener;
import vee.HexWhale.LenDen.bg.Threads.GetData;
import vee.HexWhale.LenDen.bg.Threads.GetDataFromUrl;
import vee.HexWhale.LenDen.bg.Threads.TagGen;

import java.io.File;
import java.util.Locale;

public class Profile extends MenuBar {

    private static final int page = 1;
    ListView mListView;
    GetDataFromUrl mDataFromUrl;

    private String tag = "UNKNOWN";
    DisplayImageOptions optionsDp;
    File cacheDir = new File(Environment.getExternalStorageDirectory(), STRING.CACHE_LOC);
    ImageLoader imageLoader;
    ImageView mDp;
    TextView mView, mName, mEmail, mSListings, mSFavs, mSLikes;
    GetProfile profile;
    GetItemStats itemStats;
    GetProfileItems profileItems;
    GlobalSharedPrefs mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.profile);
        tag = TagGen.getTag(getClass());
        mPrefs = new GlobalSharedPrefs(this);
        initilizeImageCache();
        mView = (TextView) findViewById(R.id.no_item);
        mListView = (ListView) findViewById(android.R.id.list);
        mDp = (ImageView) findViewById(R.id.profile_dp);
        mName = (TextView) findViewById(R.id.profile_name);
        mSListings = (TextView) findViewById(R.id.profile_stats_listings);
        mSFavs = (TextView) findViewById(R.id.profile_stats_favs);
        mSLikes = (TextView) findViewById(R.id.profile_stats_likes);

        mEmail = (TextView) findViewById(R.id.profile_loc);

        mDataFromUrl = new GetDataFromUrl(this, mFetcherListener);
        mDataFromUrl.setAccessToken();

        if (mPrefs.getStringInPref(KEY.MY_E_MAIL).equalsIgnoreCase("") || mPrefs.getStringInPref(KEY.MY_E_MAIL) == null)
        {
            mDataFromUrl.GetString(TYPE.PROFILE_ME, getBody(TYPE.PROFILE_ME), GetData.getUrl(URL.PROFILE_ME));
        }
        else {
            mName.setText(mPrefs.getStringInPref(KEY.MY_F_NAME) + " " + mPrefs.getStringInPref(KEY.MY_L_NAME));
            mEmail.setText(mPrefs.getStringInPref(KEY.MY_E_MAIL));
            System.out.println("" + mPrefs.getStringInPref(KEY.MY_I_URL));
            imageLoader.displayImage(mPrefs.getStringInPref(KEY.MY_I_URL), mDp, optionsDp);
            mDataFromUrl.GetString(TYPE.PROFILE_ITEMS_STATS, getBody(TYPE.PROFILE_ITEMS_STATS), GetData.getUrl(URL.PROFILE_ITEMS_STATS));
        }
        return;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private final FetcherListener mFetcherListener = new FetcherListener() {

        @Override
        public void tokenError(String tokenError) {
            ToastL(tokenError);

        }

        @Override
        public void startedParsing(int type) {
            // TODO Auto-generated method stub

        }

        @Override
        public void finishedParsing(int typ) {

            switch (typ) {

                case TYPE.PROFILE_ME:
                    mDataFromUrl.setAccessToken();
                    mDataFromUrl.GetString(TYPE.PROFILE_ITEMS_STATS, getBody(TYPE.PROFILE_ITEMS_STATS), GetData.getUrl(URL.PROFILE_ITEMS_STATS));

                    profile = SettersNGetters.getProfile();

                    if (profile == null) {
                        ToastL("{ Unknown Profile }");
                        return;
                    }
                    mPrefs.setStringInPref(KEY.MY_F_NAME, profile.getResponse().getFirst_name());
                    mPrefs.setStringInPref(KEY.MY_L_NAME, profile.getResponse().getLast_name());
                    mPrefs.setStringInPref(KEY.MY_E_MAIL, profile.getResponse().getEmail());
                    mPrefs.setStringInPref(KEY.MY_I_URL, "" + GetData.getImageUrl(IMAGEURL.DP + profile.getResponse().getId()));

                    mName.setText(profile.getResponse().getFirst_name() + " " + profile.getResponse().getLast_name());
                    mEmail.setText(profile.getResponse().getEmail());
                    imageLoader.displayImage("" + GetData.getImageUrl(IMAGEURL.DP + profile.getResponse().getId()), mDp, optionsDp);

                    break;

                case TYPE.PROFILE_ITEMS_STATS:

                    mDataFromUrl.setAccessToken();
                    mDataFromUrl.GetString(TYPE.PROFILE_ITEMS, getBody(TYPE.PROFILE_ITEMS), GetData.getUrl(URL.PROFILE_ITEMS));

                    itemStats = SettersNGetters.getItemStats();

                    if (itemStats == null)
                    {
                        ToastL("{ Unknown Error }");
                        return;
                    }

                    mSListings.setText("" + itemStats.getResponse().getListed_items_count());
                    mSFavs.setText("" + itemStats.getResponse().getFavorite_items_count());
                    mSLikes.setText("" + itemStats.getResponse().getLiked_items_count());

                    break;

                case TYPE.PROFILE_ITEMS:

                    profileItems = SettersNGetters.getProfileItems();
                    if (profileItems == null) {
                        mView.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.GONE);
                        ToastL("{ Unknown Error }");
                        return;

                    }

                    if (profileItems.getResponse().getItems().size() == 0) {
                        mView.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.GONE);
                        ToastL("{ No Items Listed }");
                        return;
                    }

                    final ProfileListAdapter adapter = new ProfileListAdapter(Profile.this, profileItems);
                    final SwingRightInAnimationAdapter mScaleInAnimationAdapter = new SwingRightInAnimationAdapter(adapter, 40, 400);
                    mScaleInAnimationAdapter.setAbsListView(mListView);
                    mListView.setAdapter(mScaleInAnimationAdapter);
                    break;

            }

        }

        @Override
        public void finishedFetching(int type, String response) {
            LogR(response);

        }

        @Override
        public void errorFetching(int type, VolleyError error) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeParsing(int type) {
            // TODO Auto-generated method stub

        }

        @Override
        public void ParsingException(Exception e) {
            // TODO Auto-generated method stub

        }
    };

    private String getBody(final int mType) {

        if (mType == TYPE.PROFILE_ITEMS)
        {
            JSONObject mJsonObject = null;
            mJsonObject = new JSONObject();
            try {
                mJsonObject.put(STRING.PAGE, "" + Profile.page);
                mJsonObject.put(STRING.OFFSET, "" + 10);
                System.out.println(mJsonObject.toString());
                return mJsonObject.toString();
            }
            catch (final JSONException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mImgLeft = (ImageView) findViewById(R.id.menu_left);
        mTextCenter = (TextView) findViewById(R.id.menu_center);

        mImgLeft.setImageResource(R.drawable.add_items_back);
        mTextCenter.setText(("Profile").toUpperCase(Locale.UK));

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

    public void FlipView(View v) {
        finish();
        AnimPrev();

    }

    @SuppressWarnings("unused")
    private void AnimNext() {
        overridePendingTransition(R.anim.enter, R.anim.exit);
        return;
    }

    protected void LogR(String msg) {
        Log.wtf(tag, msg);
    }

    /**
     * @param text
     */
    private void ToastL(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
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

        final ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
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
