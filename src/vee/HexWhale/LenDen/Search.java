/**
 * ***Copyright(c)	:	2014-Present, VenomVendor.***
 * Author			:	VenomVendor
 * Dated			:	17 Feb, 2014 3:28:57 AM
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

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.haarman.listviewanimations.swinginadapters.prepared.SwingRightInAnimationAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import vee.HexWhale.LenDen.Parsers.Common.Items;
import vee.HexWhale.LenDen.Parsers.SearchCategory.GetSearchCategory;
import vee.HexWhale.LenDen.Storage.SettersNGetters;
import vee.HexWhale.LenDen.Utils.Constants.API.STRING;
import vee.HexWhale.LenDen.Utils.Constants.API.TYPE;
import vee.HexWhale.LenDen.Utils.Constants.API.URL;
import vee.HexWhale.LenDen.aUI.Adapters.SearchListAdapter;
import vee.HexWhale.LenDen.aUI.Fragments.NiceSupportMapFragment;
import vee.HexWhale.LenDen.bg.Threads.FetcherListener;
import vee.HexWhale.LenDen.bg.Threads.GetData;
import vee.HexWhale.LenDen.bg.Threads.GetDataFromUrl;
import vee.HexWhale.LenDen.bg.Threads.LocationFinder;
import vee.HexWhale.LenDen.bg.Threads.LocationFinder.LocListner;
import vee.HexWhale.LenDen.bg.Threads.TagGen;

import java.util.List;
import java.util.Locale;

public class Search extends FragmentActivity {
    GoogleMap map = null;
    double latitude = 12.971689;
    double longitude = 77.594504;
    Marker markerCurrentLocation;
    LatLng latlon = null;
    public static boolean googlePlayOn = false;
    public static boolean isSearchDone = false;

    ListView mListView;

    View mView;
    EditText mSearchBtn;
    LinearLayout searchFrame;
    FrameLayout mapFrame;

    GetDataFromUrl mDataFromUrl;
    GetSearchCategory searchCategory;
    List<Items> mItems;
    int type = -1;
    private String tag = "UNKNOWN";
    private LocationFinder myLocation;
    private Location location;
    final float zoom = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        tag = TagGen.getTag(this.getClass());
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.search);
        myLocation = new LocationFinder(getApplicationContext(), mLocListner);
        mDataFromUrl = new GetDataFromUrl(this, mFetcherListener);
        ((TextView) findViewById(R.id.menu_center)).setText(("search").toUpperCase(Locale.UK));
        // ((ImageView)
        // this.findViewById(R.id.menu_right)).setImageResource(R.drawable.filter);
        ((ImageView) findViewById(R.id.menu_right)).setVisibility(View.INVISIBLE);

        latlon = new LatLng(latitude, longitude);
        setUpMapIfNeeded(); // Required to check the availability of Maps
        mListView = (ListView) findViewById(R.id.search_list);
        mView = findViewById(R.id.dummy_id);
        mSearchBtn = (EditText) findViewById(R.id.search_id);
        searchFrame = (LinearLayout) findViewById(R.id.search_ll);
        mapFrame = (FrameLayout) findViewById(R.id.mapFrame);

        mSearchBtn.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (v.getText().length() < 3)
                {
                    mSearchBtn.setError("Min 3 Chars");
                    return false;
                }

                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE) {
                    fetchDataforSearch(v.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });

        searchFrame.setVisibility(View.GONE);
        final LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, 0, 5f);
        mapFrame.setLayoutParams(mParams);

    }

    private LocListner mLocListner = new LocListner() {

        @Override
        public void gotLocation(Location loc) {

            if (loc == null)
            {
                return;
            }
            location = loc;
            LogR("===================== " + location.getLatitude());
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            latlon = new LatLng(latitude, longitude);
            setUpMapIfNeeded();
        }
    };

    @Override
    protected void onDestroy() {
        myLocation.stopUpdates();
        super.onDestroy();
    }

    private final FetcherListener mFetcherListener = new FetcherListener() {

        @Override
        public void errorFetching(int type, VolleyError error) {
            // TODO Auto-generated method stub

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
            ToastL("{ Unknown Error }");
        }

        @Override
        public void finishedParsing(int typ) {
            switch (typ) {
                case TYPE.ITEMS:
                    searchCategory = SettersNGetters.getSearchCategory();

                    if (searchCategory == null) {
                        ToastL("{ Unknown Error }");
                        return;
                    }

                    if (searchCategory.getResponse().getTotal_item_count() > 0)
                    {
                        setSearchList();
                        enableClickListiner();
                    }
                    else {
                        ToastL("{ No related posts }");
                    }
                    break;

                default:
                    break;
            }

        }

        @Override
        public void tokenError(String tokenError) {
            ToastL(tokenError);
        }

    };

    protected void ToastL(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    protected void setSearchList() {
        Search.isSearchDone = true;
        mItems = searchCategory.getResponse().getItems();
        final SearchListAdapter adapter = new SearchListAdapter(this, mItems);
        final SwingRightInAnimationAdapter mScaleInAnimationAdapter = new SwingRightInAnimationAdapter(adapter, 40, 400);
        mScaleInAnimationAdapter.setAbsListView(mListView);
        mListView.setAdapter(mScaleInAnimationAdapter);
        enableList();
        // mItems
    }

    protected void LogR(String msg) {
        Log.wtf(tag, msg);
    }

    protected void fetchDataforSearch(String searchText) {

        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        ToastL(searchText);
        mDataFromUrl.setAccessToken();
        mDataFromUrl.GetString(TYPE.ITEMS, getBody(TYPE.ITEMS, searchText), GetData.getUrl(URL.ITEMS));
    }


    private String getBody(int mType, String searchText) {
        JSONObject mJsonObject = null;
        mJsonObject = new JSONObject();
        location = (location == null) ? myLocation.getLocation() : location;

        try {
            switch (mType) {
                case TYPE.ITEMS:

                    ToastL((location == null) ? "Unknown Latitude, Longitude" : "Latitude" + location.getLatitude() + "\nLongitude" + location.getLongitude());

                    mJsonObject.put(STRING.LATITUDE, (location == null) ? "00" : "" + location.getLatitude());
                    mJsonObject.put(STRING.LONGITUDE, (location == null) ? "00" : "" + location.getLongitude());
                    mJsonObject.put(STRING.RANGE, "" + 10000);
                    mJsonObject.put(STRING.SEARCH, searchText);
                    break;
            }
            System.out.println(mJsonObject.toString());
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

    private void setCurrentLocation() {
        if (markerCurrentLocation != null)
        {
            markerCurrentLocation.remove();
        }
        markerCurrentLocation = map.addMarker(new MarkerOptions().position(latlon).title("My Location"));
    }


    @SuppressWarnings("unused")
    private String getBody(final int mType) {
        JSONObject mJsonObject = null;
        mJsonObject = new JSONObject();
        try {
            switch (mType) {
                case TYPE.ITEMS:
                    System.out.println("ITEMS " + mType);
                    mJsonObject.put(STRING.LATITUDE, "" + "10.00");
                    mJsonObject.put(STRING.LONGITUDE, "" + "11.00");
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

    public void Finish(View v) {
        finish();
        AnimPrev();
    }

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
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private boolean isGooglePlayServicesAvail() {
        final int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (status == ConnectionResult.SUCCESS) {
            return true;
        }
        else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 10).show();
        }
        return false;

    }

    private void setUpMapIfNeeded() {
        if (map == null && isGooglePlayServicesAvail()) {
            /*
             * I avoid Crashing, if Google_Play_Services is not Updated or Unavailable
             */
            map = ((NiceSupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            /*
             * To make sure map is loaded
             */
            if (map != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        setCurrentLocation();


        /*
         * NormalMapView
         */
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL); // Normal MapView

        /*
         * Move Camera to Snippet Location
         */
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlon, zoom)); // toPosition,
        // ZoomLevel

        if (Search.isSearchDone)
        {
            enableClickListiner();
        }
        else {
            disableClickListiner();
        }

    }

    private void disableClickListiner() {
        map.setOnMapClickListener(null);
    }

    private void enableClickListiner() {

        map.setOnMapClickListener(new OnMapClickListener() {

            @Override
            public void onMapClick(LatLng touchedLatLon) {

                System.out.println("LatLon : " + touchedLatLon);

                if (mView.getVisibility() == View.VISIBLE) {
                    disableList();
                }
                else {
                    enableList();
                }
            }
        });

    }

    protected void enableList() {
        mView.setVisibility(View.VISIBLE);
        mSearchBtn.setVisibility(View.VISIBLE);
        searchFrame.setVisibility(View.VISIBLE);
        final LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, 0, 2f);
        mapFrame.setLayoutParams(mParams);

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Intent mIntent = new Intent(getApplicationContext(), Detailed.class);
                ToastL(""+position);

                mIntent.putExtra(STRING.POSITION, position);
                mIntent.putExtra(STRING.FROM, STRING.SEARCH);
                startActivity(mIntent);
                AnimNext();
            }
        });
    }

    protected void disableList() {
        mView.setVisibility(View.GONE);
        mSearchBtn.setVisibility(View.GONE);
        searchFrame.setVisibility(View.GONE);
        final LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, 0, 5f);
        mapFrame.setLayoutParams(mParams);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void HideTop() {
        mView.setVisibility(View.GONE);
    }

    public void ShowTop() {
        mView.setVisibility(View.VISIBLE);
    }
}
