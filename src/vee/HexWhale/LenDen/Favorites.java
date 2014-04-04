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
 **/

package vee.HexWhale.LenDen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.haarman.listviewanimations.swinginadapters.prepared.SwingRightInAnimationAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import vee.HexWhale.LenDen.Parsers.FavCategory.GetFavCategory;
import vee.HexWhale.LenDen.Storage.SettersNGetters;
import vee.HexWhale.LenDen.Utils.Constants.API.STRING;
import vee.HexWhale.LenDen.Utils.Constants.API.TYPE;
import vee.HexWhale.LenDen.Utils.Constants.API.URL;
import vee.HexWhale.LenDen.aUI.MenuBar;
import vee.HexWhale.LenDen.aUI.Adapters.FavoritesAdapter;
import vee.HexWhale.LenDen.bg.Threads.FetcherListener;
import vee.HexWhale.LenDen.bg.Threads.GetData;
import vee.HexWhale.LenDen.bg.Threads.GetDataFromUrl;
import vee.HexWhale.LenDen.bg.Threads.TagGen;

import java.util.Locale;

public class Favorites extends MenuBar {

    ListView mListView;
    private String tag = "UNKNOWN";
    GetDataFromUrl mDataFromUrl;
    GetFavCategory mFavCategory;
    static int page = 1;
    String cate_id = null;
    TextView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tag = TagGen.getTag(getClass());
        this.setContentView(R.layout.favorites);

        mView = (TextView) findViewById(R.id.no_item);
        mListView = (ListView) findViewById(android.R.id.list);
        mDataFromUrl = new GetDataFromUrl(this, mFetcherListener);
        mDataFromUrl.setAccessToken();
        mDataFromUrl.GetString(TYPE.FAVORITE, getBody(TYPE.FAVORITE), GetData.getUrl(URL.FAVORITE));

    }

    private String getBody(final int mType) {
        JSONObject mJsonObject = null;
        mJsonObject = new JSONObject();
        try {
            switch (mType) {
                case TYPE.FAVORITE:
                    mJsonObject.put(STRING.PAGE, "" + Favorites.page);
                    mJsonObject.put(STRING.OFFSET, "" + 10);
                    break;
            }
            return mJsonObject.toString();
        }
        catch (final JSONException e) {
            e.printStackTrace();
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
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
                case TYPE.FAVORITE:

                    mFavCategory = SettersNGetters.getFavCategory();
                    if (mFavCategory == null) {
                        mView.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.GONE);
                        ToastL("{ Unknown Error }");
                        return;
                    }

                    if (mFavCategory.getResponse().getItems().size() == 0) {
                        mView.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.GONE);
                        return;
                    }

                    final FavoritesAdapter adapter = new FavoritesAdapter(Favorites.this, mFavCategory);

                    // SwingBottomInAnimationAdapter mScaleInAnimationAdapter =
                    // new
                    // SwingBottomInAnimationAdapter(adapter, 110, 400);
                    // ScaleInAnimationAdapter mScaleInAnimationAdapter = new
                    // ScaleInAnimationAdapter(adapter, 0.5f, 110, 400);
                    mView.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    final SwingRightInAnimationAdapter mScaleInAnimationAdapter = new SwingRightInAnimationAdapter(adapter, 40, 400);
                    mScaleInAnimationAdapter.setAbsListView(mListView);
                    mListView.setAdapter(mScaleInAnimationAdapter);

                    mListView.setOnItemClickListener(new OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            final Intent mIntent = new Intent(getApplicationContext(), Detailed.class);

                            mIntent.putExtra(STRING.POSITION, position);
                            mIntent.putExtra(STRING.FROM, STRING.FAVOURITES);
                            startActivity(mIntent);
                            AnimNext();
                        }

                    });

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

    @SuppressLint("DefaultLocale")
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mImgLeft = (ImageView) findViewById(R.id.menu_left);
        mTextCenter = (TextView) findViewById(R.id.menu_center);

        mImgLeft.setVisibility(View.INVISIBLE);
        mTextCenter.setText(("Favorites").toUpperCase(Locale.UK));

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

    protected void LogR(String msg) {
        Log.wtf(tag, msg);
    }

    /**
     * @param text
     */
    private void ToastL(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}
