/**
 * ***Copyright(c)  :   2014-Present, VenomVendor.***
 * Author           :   VenomVendor
 * Dated            :   17 Feb, 2014 3:28:58 AM
 * Project          :   LenDen-Android
 * Client           :   LenDen
 * Contact          :   info@VenomVendor.com
 * URL              :   https://www.google.co.in/search?q=VenomVendor
 * Copyright(c)     :   2014-Present, VenomVendor.
 * License          :   This work is licensed under Attribution-NonCommercial 3.0 Unported (CC BY-NC 3.0).
 *                      License info at http://creativecommons.org/licenses/by-nc/3.0/deed.en_US
 *                      Read More at http://creativecommons.org/licenses/by-nc/3.0/legalcode
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

import vee.HexWhale.LenDen.Parsers.ItemCategory.GetItemCategory;
import vee.HexWhale.LenDen.Storage.SettersNGetters;
import vee.HexWhale.LenDen.Utils.Constants.API.STRING;
import vee.HexWhale.LenDen.Utils.Constants.API.TYPE;
import vee.HexWhale.LenDen.Utils.Constants.API.URL;
import vee.HexWhale.LenDen.aUI.MenuBar;
import vee.HexWhale.LenDen.aUI.Adapters.PreviewAdapter;
import vee.HexWhale.LenDen.bg.Threads.FetcherListener;
import vee.HexWhale.LenDen.bg.Threads.GetData;
import vee.HexWhale.LenDen.bg.Threads.GetDataFromUrl;
import vee.HexWhale.LenDen.bg.Threads.TagGen;

public class Preview extends MenuBar {

    ListView mListView;
    private String tag = "UNKNOWN";
    GetDataFromUrl mDataFromUrl;
    GetItemCategory mItemCategory;
    static int page = 1;
    String cate_id = null;
    TextView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tag = TagGen.getTag(getClass());
        setContentView(R.layout.preview);
        mView = (TextView) findViewById(R.id.no_item);
        mDataFromUrl = new GetDataFromUrl(this, mFetcherListener);
        mListView = (ListView) findViewById(android.R.id.list);

        final Intent mIntent = getIntent();
        if (mIntent != null)
        {
            cate_id = mIntent.getStringExtra("cate_id");
        }

        mDataFromUrl.setAccessToken();
        mDataFromUrl.GetString(TYPE.ITEM_CATEGORIES, getBody(TYPE.ITEM_CATEGORIES), GetData.getUrl(URL.ITEM_CATEGORIES));

    }

    private String getBody(final int mType) {
        JSONObject mJsonObject = null;
        mJsonObject = new JSONObject();
        try {
            switch (mType) {
                case TYPE.ITEM_CATEGORIES:
                    System.out.println("ISSUE " + TYPE.ACCESSTOKEN);
                    mJsonObject.put(STRING.PAGE, "" + Preview.page);
                    mJsonObject.put(STRING.OFFSET, "" + 10);
                    mJsonObject.put(STRING.CATEGORY_ID, cate_id);
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
                case TYPE.ITEM_CATEGORIES:

                    mItemCategory = SettersNGetters.getItemCategory();

                    if (mItemCategory == null) {
                        mView.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.GONE);
                        ToastL("{ Unknown Error }");
                        return;
                    }

                    if (mItemCategory.getResponse().getItems().size() == 0) {
                        mView.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.GONE);
                        ToastL("{ No Items }");
                        return;
                    }

                    final PreviewAdapter adapter = new PreviewAdapter(Preview.this, mItemCategory);

                    // SwingBottomInAnimationAdapter mScaleInAnimationAdapter =
                    // new
                    // SwingBottomInAnimationAdapter(adapter, 110, 400);
                    // ScaleInAnimationAdapter mScaleInAnimationAdapter = new
                    // ScaleInAnimationAdapter(adapter, 0.5f, 110, 400);

                    final SwingRightInAnimationAdapter mScaleInAnimationAdapter = new SwingRightInAnimationAdapter(adapter, 40, 400);
                    mScaleInAnimationAdapter.setAbsListView(mListView);
                    mListView.setAdapter(mScaleInAnimationAdapter);

                    mListView.setOnItemClickListener(new OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            final Intent mIntent = new Intent(getApplicationContext(), Detailed.class);

                            mIntent.putExtra(STRING.POSITION, position);

                            startActivity(mIntent);
                            AnimNext();
                        }

                    });

                    break;

                default:
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

        mImgLeft.setImageResource(R.drawable.filter);
        mTextCenter.setText(STRING.PREVIEW);

    }

    protected void LogR(String msg) {
        Log.wtf(tag, msg);
    }

    public void FlipView(View v)
    {
        ToastL("Open Spinner for filters");
    }

    /**
     * @param text
     */
    private void ToastL(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
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

}
