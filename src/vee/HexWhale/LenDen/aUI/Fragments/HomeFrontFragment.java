/**
 * ***Copyright(c) : 2014-Present, VenomVendor.***
 * Author : VenomVendor
 * Dated : 17 Feb, 2014 3:29:01 AM
 * Project : LenDen-Android
 * Client : LenDen
 * Contact : info@VenomVendor.com
 * URL : https://www.google.co.in/search?q=VenomVendor
 * Copyright(c) : 2014-Present, VenomVendor.
 * License : This work is licensed under Attribution-NonCommercial 3.0 Unported (CC BY-NC 3.0).
 * License info at http://creativecommons.org/licenses/by-nc/3.0/deed.en_US
 * Read More at http://creativecommons.org/licenses/by-nc/3.0/legalcode
 **/

package vee.HexWhale.LenDen.aUI.Fragments;

import vee.HexWhale.LenDen.Home;
import vee.HexWhale.LenDen.Preview;
import vee.HexWhale.LenDen.R;
import vee.HexWhale.LenDen.aUI.Adapters.HomeGridAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.haarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

/**
 * A fragment representing the back of the card.
 */
public class HomeFrontFragment extends Fragment {
    Activity sActivity;
    GridView mGridView;
    GoogleMap map = null;
    double latitude = 12.971689;
    double longitude = 77.594504;
    TextView mTextView;
    FrameLayout mapFrame;
    LatLng latlon = null;
    public static boolean googlePlayOn = false;

    private static View view;

    public HomeFrontFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.sActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (container == null) {
            return null;
        }

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.home_front, container, false);
        }
        catch (InflateException e) {
            Log.wtf("S*****", e.getMessage());
        }
        mGridView = (GridView) view.findViewById(R.id.home_grid);
        mTextView = (TextView) view.findViewById(R.id.home_dummy_text);
        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        HomeGridAdapter adapter = new HomeGridAdapter(sActivity);
        mapFrame = (FrameLayout) sActivity.findViewById(R.id.mapFrame);
        latlon = new LatLng(latitude, longitude);
        SwingBottomInAnimationAdapter mScaleInAnimationAdapter = new SwingBottomInAnimationAdapter(adapter, 110, 400);
        mScaleInAnimationAdapter.setAbsListView(mGridView);
        mGridView.setAdapter(mScaleInAnimationAdapter);
        mGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View parent, int position, long id) {

                Intent i = new Intent(getActivity(), Preview.class);
                startActivity(i);
                AnimNext();

            }

        });

        if (isGooglePlay()) {
            setUpMapIfNeeded();
        }
    }

    protected void AnimNext() {
        getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
        return;
    }

    private boolean isGooglePlay() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (status == ConnectionResult.SUCCESS) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, getActivity(), 10).show();
        }
        return false;

    }

    private void setUpMapIfNeeded() {
        if (map == null) {
            map = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            if (map != null) {
                // do things to the map
                map.addMarker(new MarkerOptions().position(latlon).title("xxx").snippet("I am a looooooooooooooong Snippet"));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlon, 15));
                // map.getUiSettings().setZoomControlsEnabled(false);

                map.setOnMapClickListener(new OnMapClickListener() {

                    @Override
                    public void onMapClick(LatLng touchedLatLon) {

                        System.out.println("LatLon : " + touchedLatLon);

                        if (mTextView.getVisibility() == View.VISIBLE) {
                            mTextView.setVisibility(View.GONE);
                            LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, 0, 5f);
                            mapFrame.setLayoutParams(mParams);
                            ((Home) sActivity).HideTop();
                        } else {
                            mTextView.setVisibility(View.VISIBLE);
                            LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, 0, 2f);
                            mapFrame.setLayoutParams(mParams);
                            ((Home) sActivity).ShowTop();
                        }

                    }
                });
            }
        }
    }
}