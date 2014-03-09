/**
 * ***Copyright(c) : 2014-Present, VenomVendor.***
 * Author : VenomVendor
 * Dated : 17 Feb, 2014 3:28:57 AM
 * Project : LenDen-Android
 * Client : LenDen
 * Contact : info@VenomVendor.com
 * URL : https://www.google.co.in/search?q=VenomVendor
 * Copyright(c) : 2014-Present, VenomVendor.
 * License : This work is licensed under Attribution-NonCommercial 3.0 Unported (CC BY-NC 3.0).
 * License info at http://creativecommons.org/licenses/by-nc/3.0/deed.en_US
 * Read More at http://creativecommons.org/licenses/by-nc/3.0/legalcode
 **/

package vee.HexWhale.LenDen;

import java.util.Locale;

import vee.HexWhale.LenDen.aUI.Adapters.SearchListAdapter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.haarman.listviewanimations.swinginadapters.prepared.SwingRightInAnimationAdapter;

public class Search extends FragmentActivity {
    GoogleMap map = null;
    double latitude = 12.971689;
    double longitude = 77.594504;

    LatLng latlon = null;
    public static boolean googlePlayOn = false;

    ListView mListView;

    View mView;
    ImageView mImageView;
    LinearLayout search;
    FrameLayout mapFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        ((TextView) findViewById(R.id.menu_center)).setText(("search").toUpperCase(Locale.UK));
        ((ImageView) findViewById(R.id.menu_right)).setImageResource(R.drawable.filter);

        latlon = new LatLng(latitude, longitude);
        setUpMapIfNeeded(); // Required to check the availability of Maps
        mListView = (ListView) findViewById(R.id.search_list);
        mView = findViewById(R.id.dummy_id);
        mImageView = (ImageView) findViewById(R.id.search_id);
        search = (LinearLayout) findViewById(R.id.search_ll);
        mapFrame = (FrameLayout) findViewById(R.id.mapFrame);

        SearchListAdapter adapter = new SearchListAdapter(this);
        SwingRightInAnimationAdapter mScaleInAnimationAdapter = new SwingRightInAnimationAdapter(adapter, 40, 400);
        mScaleInAnimationAdapter.setAbsListView(mListView);
        mListView.setAdapter(mScaleInAnimationAdapter);
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
        this.finish();
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
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (status == ConnectionResult.SUCCESS) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 10).show();
        }
        return false;

    }

    private void setUpMapIfNeeded() {
        if (map == null && isGooglePlayServicesAvail()) {
            /*
             * I avoid Crashing, if Google_Play_Services is not Updated or
             * Unavailable
             */
            map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            /*
             * To make sure map is loaded
             */
            if (map != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        /*
         * Add a Marker Adding marker at 12.971689,77.594504;
         */
        // map.getUiSettings().setZoomControlsEnabled(false);
        map.addMarker(new MarkerOptions().position(latlon)

        /*
         * Add Title when clicked on marker
         */
        .title("Title")
        /*
         * Add Snippet when clicked on marker
         */
        .snippet("I am a looooooooooooooong Snippet"));

        /*
         * NormalMapView
         */
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL); // Normal MapView

        /*
         * Move Camera to Snippet Location
         */
        float zoom = 11;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlon, zoom)); // toPosition,
        // ZoomLevel

        map.setOnMapClickListener(new OnMapClickListener() {

            @Override
            public void onMapClick(LatLng touchedLatLon) {

                System.out.println("LatLon : " + touchedLatLon);

                if (mView.getVisibility() == View.VISIBLE) {
                    mView.setVisibility(View.GONE);
                    mImageView.setVisibility(View.GONE);
                    search.setVisibility(View.GONE);
                    LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, 0, 5f);
                    mapFrame.setLayoutParams(mParams);
                } else {
                    mView.setVisibility(View.VISIBLE);
                    mImageView.setVisibility(View.VISIBLE);
                    search.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, 0, 2f);
                    mapFrame.setLayoutParams(mParams);
                }
            }
        });
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
