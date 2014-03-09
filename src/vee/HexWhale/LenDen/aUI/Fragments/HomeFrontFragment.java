/**
 * ***Copyright(c) : 2014-Present, VenomVendor.***
 * Author : VenomVendor
 * Dated : 17 Feb, 2014 3:29:01 AM
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

package vee.HexWhale.LenDen.aUI.Fragments;

import android.app.Activity;
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
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.haarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

import vee.HexWhale.LenDen.Home;
import vee.HexWhale.LenDen.R;
import vee.HexWhale.LenDen.Storage.SettersNGetters;
import vee.HexWhale.LenDen.Utils.Constants.API.STRING;
import vee.HexWhale.LenDen.Utils.Constants.API.TYPE;
import vee.HexWhale.LenDen.Utils.Constants.API.URL;
import vee.HexWhale.LenDen.aUI.Adapters.HomeGridAdapter;
import vee.HexWhale.LenDen.bg.Threads.FetcherListener;
import vee.HexWhale.LenDen.bg.Threads.GetData;
import vee.HexWhale.LenDen.bg.Threads.GetDataFromUrl;
import vee.HexWhale.LenDen.bg.Threads.TagGen;

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

    GetDataFromUrl mDataFromUrl;
    private String tag = "UNKNOWN";

    public HomeFrontFragment() {
        this.setRetainInstance(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.sActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.tag = TagGen.getTag(this.getClass());

        if (container == null) {
            return null;
        }

        if (HomeFrontFragment.view != null) {
            final ViewGroup parent = (ViewGroup) HomeFrontFragment.view.getParent();
            if (parent != null) {
                parent.removeView(HomeFrontFragment.view);
            }
        }
        try {
            HomeFrontFragment.view = inflater.inflate(R.layout.home_front, container, false);
        }
        catch (final InflateException e) {
            Log.wtf("S*****", e.getMessage());
        }
        this.mGridView = (GridView) HomeFrontFragment.view.findViewById(R.id.home_grid);
        this.mTextView = (TextView) HomeFrontFragment.view.findViewById(R.id.home_dummy_text);
        return HomeFrontFragment.view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.mapFrame = (FrameLayout) this.sActivity.findViewById(R.id.mapFrame);
        this.latlon = new LatLng(this.latitude, this.longitude);

        this.mDataFromUrl = new GetDataFromUrl(this.sActivity, this.mFetcherListener);
        sendRequest();

        if (this.isGooglePlay()) {
            this.setUpMapIfNeeded();
        }
    }

    private void sendRequest() {
        this.mDataFromUrl.setAccessToken();
        this.mDataFromUrl.GetString(TYPE.CATEGORIES, getBody(TYPE.CATEGORIES), GetData.getUrl(URL.CATEGORIES));
    }

    private String getBody(int tokenType) {
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

            /*************** COMMON ***************/
            switch (typ) {
                case TYPE.AUTHORIZE:
                    LogR("DO NOTHING");
                    return;
                case TYPE.ACCESSTOKEN:
                    LogR("LOGOUT HERE");
                    return;
                case TYPE.REFRESH:
                    LogR("RESEND REQUEST HERE");
                    sendRequest();
                    return;
            }
            /*************** COMMON ***************/

            if (SettersNGetters.getCategory().getStatus().equalsIgnoreCase(STRING.SUCCESS))
            {

                final HomeGridAdapter adapter = new HomeGridAdapter(sActivity, SettersNGetters.getCategory().getResponse());
                final SwingBottomInAnimationAdapter mScaleInAnimationAdapter = new SwingBottomInAnimationAdapter(adapter, 110, 400);
                mScaleInAnimationAdapter.setAbsListView(mGridView);
                mGridView.setAdapter(mScaleInAnimationAdapter);
                mGridView.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapter, View parent, int position, long id) {

                        ToastL("" + position);
                        // final Intent i = new Intent(getActivity(),
                        // Preview.class);
                        // startActivity(i);
                        // AnimNext();

                    }

                });
            }
            else{
                ToastL("{ Error }");
            }
        }

        @Override
        public void finishedFetching(int type, String response) {
            LogR("+++ " + response);
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
            ToastL("{ Unknown Error }");
        }
    };

    protected void AnimNext() {
        this.getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
        return;
    }

    private boolean isGooglePlay() {
        final int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.getActivity());
        if (status == ConnectionResult.SUCCESS) {
            return true;
        }
        else {
            GooglePlayServicesUtil.getErrorDialog(status, this.getActivity(), 10).show();
        }
        return false;

    }

    /*******************************************************************/
    /**
     * @param RED
     */
    private void LogR(String msg) {
        Log.wtf(this.tag, msg);
    }

    /**
     * @param text
     */
    private void ToastL(String text) {
        Toast.makeText(this.sActivity, text, Toast.LENGTH_LONG).show();
    }

    /*******************************************************************/

    private void setUpMapIfNeeded() {
        if (this.map == null) {
            this.map = ((SupportMapFragment) this.getActivity().getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            if (this.map != null) {
                // do things to the map
                this.map.addMarker(new MarkerOptions().position(this.latlon).title("xxx").snippet("I am a looooooooooooooong Snippet"));
                this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(this.latlon, 15));
                // map.getUiSettings().setZoomControlsEnabled(false);

                this.map.setOnMapClickListener(new OnMapClickListener() {

                    @Override
                    public void onMapClick(LatLng touchedLatLon) {

                        System.out.println("LatLon : " + touchedLatLon);

                        if (HomeFrontFragment.this.mTextView.getVisibility() == View.VISIBLE) {
                            HomeFrontFragment.this.mTextView.setVisibility(View.GONE);
                            final LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                                    0, 5f);
                            HomeFrontFragment.this.mapFrame.setLayoutParams(mParams);
                            ((Home) HomeFrontFragment.this.sActivity).HideTop();
                        }
                        else {
                            HomeFrontFragment.this.mTextView.setVisibility(View.VISIBLE);
                            final LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                                    0, 2f);
                            HomeFrontFragment.this.mapFrame.setLayoutParams(mParams);
                            ((Home) HomeFrontFragment.this.sActivity).ShowTop();
                        }

                    }
                });
            }
        }
    }
}
