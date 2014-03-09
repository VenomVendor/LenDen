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

import vee.HexWhale.LenDen.aUI.Adapters.SearchListAdapter;

import java.util.Locale;

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
        this.setContentView(R.layout.search);

        ((TextView) this.findViewById(R.id.menu_center)).setText(("search").toUpperCase(Locale.UK));
        ((ImageView) this.findViewById(R.id.menu_right)).setImageResource(R.drawable.filter);

        this.latlon = new LatLng(this.latitude, this.longitude);
        this.setUpMapIfNeeded(); // Required to check the availability of Maps
        this.mListView = (ListView) this.findViewById(R.id.search_list);
        this.mView = this.findViewById(R.id.dummy_id);
        this.mImageView = (ImageView) this.findViewById(R.id.search_id);
        this.search = (LinearLayout) this.findViewById(R.id.search_ll);
        this.mapFrame = (FrameLayout) this.findViewById(R.id.mapFrame);

        final SearchListAdapter adapter = new SearchListAdapter(this);
        final SwingRightInAnimationAdapter mScaleInAnimationAdapter = new SwingRightInAnimationAdapter(adapter, 40, 400);
        mScaleInAnimationAdapter.setAbsListView(this.mListView);
        this.mListView.setAdapter(mScaleInAnimationAdapter);
    }

    public void Finish(View v) {
        this.finish();
        this.AnimPrev();
    }

    public void Submit(View v) {
        this.finish();
        this.AnimNext();
    }

    @Override
    public void onBackPressed() {
        this.finish();
        this.AnimPrev();
    }

    private void AnimPrev() {
        this.overridePendingTransition(R.anim.android_slide_in_left, R.anim.android_slide_out_right);
        return;
    }

    private void AnimNext() {
        this.overridePendingTransition(R.anim.enter, R.anim.exit);
        return;
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.setUpMapIfNeeded();
    }

    private boolean isGooglePlayServicesAvail() {
        final int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (status == ConnectionResult.SUCCESS) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 10).show();
        }
        return false;

    }

    private void setUpMapIfNeeded() {
        if (this.map == null && this.isGooglePlayServicesAvail()) {
            /*
             * I avoid Crashing, if Google_Play_Services is not Updated or
             * Unavailable
             */
            this.map = ((SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            /*
             * To make sure map is loaded
             */
            if (this.map != null) {
                this.setUpMap();
            }
        }
    }

    private void setUpMap() {
        /*
         * Add a Marker Adding marker at 12.971689,77.594504;
         */
        // map.getUiSettings().setZoomControlsEnabled(false);
        this.map.addMarker(new MarkerOptions().position(this.latlon)

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
        this.map.setMapType(GoogleMap.MAP_TYPE_NORMAL); // Normal MapView

        /*
         * Move Camera to Snippet Location
         */
        final float zoom = 11;
        this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(this.latlon, zoom)); // toPosition,
        // ZoomLevel

        this.map.setOnMapClickListener(new OnMapClickListener() {

            @Override
            public void onMapClick(LatLng touchedLatLon) {

                System.out.println("LatLon : " + touchedLatLon);

                if (Search.this.mView.getVisibility() == View.VISIBLE) {
                    Search.this.mView.setVisibility(View.GONE);
                    Search.this.mImageView.setVisibility(View.GONE);
                    Search.this.search.setVisibility(View.GONE);
                    final LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, 0, 5f);
                    Search.this.mapFrame.setLayoutParams(mParams);
                } else {
                    Search.this.mView.setVisibility(View.VISIBLE);
                    Search.this.mImageView.setVisibility(View.VISIBLE);
                    Search.this.search.setVisibility(View.VISIBLE);
                    final LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, 0, 2f);
                    Search.this.mapFrame.setLayoutParams(mParams);
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void HideTop() {
        this.mView.setVisibility(View.GONE);
    }

    public void ShowTop() {
        this.mView.setVisibility(View.VISIBLE);
    }
}
