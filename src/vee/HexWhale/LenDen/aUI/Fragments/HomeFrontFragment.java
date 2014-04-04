/**
 * ***Copyright(c)	:	2014-Present, VenomVendor.***
 * Author			:	VenomVendor
 * Dated			:	17 Feb, 2014 3:29:01 AM
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

package vee.HexWhale.LenDen.aUI.Fragments;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.SparseArrayCompat;
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

import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.haarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import vee.HexWhale.LenDen.Home;
import vee.HexWhale.LenDen.R;
import vee.HexWhale.LenDen.Parsers.Categories.Response;
import vee.HexWhale.LenDen.Parsers.MapItems.GetMapItems;
import vee.HexWhale.LenDen.Parsers.MapItems.Items;
import vee.HexWhale.LenDen.Storage.SettersNGetters;
import vee.HexWhale.LenDen.Utils.Constants;
import vee.HexWhale.LenDen.Utils.Constants.API.STRING;
import vee.HexWhale.LenDen.Utils.Constants.API.TYPE;
import vee.HexWhale.LenDen.Utils.Constants.API.URL;
import vee.HexWhale.LenDen.aUI.Adapters.HomeGridAdapter;
import vee.HexWhale.LenDen.bg.Threads.FetcherListener;
import vee.HexWhale.LenDen.bg.Threads.GetData;
import vee.HexWhale.LenDen.bg.Threads.GetDataFromUrl;
import vee.HexWhale.LenDen.bg.Threads.LocationFinder;
import vee.HexWhale.LenDen.bg.Threads.LocationFinder.LocListner;
import vee.HexWhale.LenDen.bg.Threads.TagGen;

import java.util.List;

/**
 * A fragment representing the back of the card.
 */
public class HomeFrontFragment extends Fragment {
    Activity sActivity;
    GridView mGridView;
    GoogleMap map = null;
    double latitude = 0.0;
    double longitude = 0.0;
    TextView mTextView;
    FrameLayout mapFrame;
    LatLng latlon = null;
    public static boolean googlePlayOn = false;

    private static View view;
    LocationFinder myLocation;
    GetDataFromUrl mDataFromUrl;
    GetMapItems mapItems;
    private Location location;
    private String tag = "UNKNOWN";
    float zoom = 11;
    protected boolean noTouched = true;
    Marker markerCurrentLocation;
    SparseArrayCompat<Marker> tempRack = new SparseArrayCompat<Marker>();
    List<Response> mResponse;
    List<Items> list;

    public HomeFrontFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        sActivity = activity;
    }

    @Override
    public void onDestroy() {
        myLocation.stopUpdates();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (savedInstanceState == null) {
            setRetainInstance(true);
        } else {
            map = ((NiceSupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        }

        tag = TagGen.getTag(this.getClass());
        myLocation = new LocationFinder(sActivity, mLocListner);

        if (myLocation.getLocation() != null)
        {
            latitude = myLocation.getLocation().getLatitude();
            longitude = myLocation.getLocation().getLatitude();
        }

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
        mGridView = (GridView) HomeFrontFragment.view.findViewById(R.id.home_grid);
        mTextView = (TextView) HomeFrontFragment.view.findViewById(R.id.home_dummy_text);
        return HomeFrontFragment.view;

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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapFrame = (FrameLayout) sActivity.findViewById(R.id.mapFrame);
        latlon = new LatLng(latitude, longitude);

        mDataFromUrl = new GetDataFromUrl(sActivity, mFetcherListener);
        sendRequest();

        if (isGooglePlay()) {
            setUpMapIfNeeded();
        }
    }

    private void sendRequest() {
        mDataFromUrl.setAccessToken();
        mDataFromUrl.GetString(TYPE.CATEGORIES, getBody(TYPE.CATEGORIES), GetData.getUrl(URL.CATEGORIES));
    }

    private String getBody(int tokenType) {

        if (tokenType == TYPE.MAP_ITEMS)
        {
            JSONObject mJsonObject = null;
            mJsonObject = new JSONObject();
            location = (location == null) ? myLocation.getLocation() : location;

            try {
                ToastL((location == null) ? "Unknown Latitude, Longitude" : "Latitude" + location.getLatitude() + "\nLongitude"
                        + location.getLongitude());

                mJsonObject.put(STRING.LATITUDE, (location == null) ? "00" : "" + location.getLatitude());
                mJsonObject.put(STRING.LONGITUDE, (location == null) ? "00" : "" + location.getLongitude());

                mJsonObject.put(STRING.RANGE, "" + 10000);
                System.out.println(mJsonObject.toString());
                return mJsonObject.toString();
            }
            catch (final JSONException e) {
                e.printStackTrace();
            }

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

            switch (type) {
                case TYPE.CATEGORIES:
                    if (map == null)
                    {
                        return;
                    }
                    mDataFromUrl.setAccessToken();
                    mDataFromUrl.GetString(TYPE.MAP_ITEMS, getBody(TYPE.MAP_ITEMS), GetData.getUrl(URL.MAP_ITEMS));
                    break;
            }

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

                    /*************** COMMON ***************/

                case TYPE.CATEGORIES:

                    if (SettersNGetters.getCategory().getStatus().equalsIgnoreCase(STRING.SUCCESS))
                    {

                        mResponse = SettersNGetters.getCategory().getResponse();
                        final HomeGridAdapter adapter = new HomeGridAdapter(sActivity, mResponse);
                        final SwingBottomInAnimationAdapter mScaleInAnimationAdapter = new SwingBottomInAnimationAdapter(adapter, 110, 400);
                        mScaleInAnimationAdapter.setAbsListView(mGridView);
                        mGridView.setAdapter(mScaleInAnimationAdapter);
                        mGridView.setOnItemClickListener(new OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> adapter, View parent, int position, long id) {

                                updateMap(mResponse.get(position).getCategory_id());
                                ToastL("" + position);
                                // final Intent i = new Intent(getActivity(),
                                // Preview.class);
                                // startActivity(i);
                                // AnimNext();

                            }

                        });
                    }
                    else {
                        ToastL("{ Error }");
                    }

                case TYPE.MAP_ITEMS:

                    mapItems = SettersNGetters.getMapItems();

                    if (mapItems == null)
                    {
                        ToastL("{ Unknown Error }");
                        return;
                    }

                    if (mapItems.getStatus().equalsIgnoreCase(STRING.SUCCESS))
                    {
                        list = mapItems.getResponse().getItems();
                        addMarker();

                       // ///////////////////////ccccc

                    }
                    else {
                        ToastL("{ Error }");
                    }

                    return;
            }

        }

        @Override
        public void finishedFetching(int type, String response) {
            LogR("+++ " + response);
        }

        @Override
        public void errorFetching(int type, VolleyError error) {

        }

        @Override
        public void beforeParsing(int type) {

        }

        @Override
        public void ParsingException(Exception e) {
            ToastL("{ Unknown Error }");
        }
    };

    protected void AnimNext() {
        getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
        return;
    }

    protected void updateMap(String category_id) {


        if (map == null)
        {
            return;
        }

        for (int i = 0; i < list.size(); i++) {
            final Marker tempMarker = tempRack.get(i);
            if (category_id.equalsIgnoreCase(list.get(i).getCategory_id()))
            {
                tempMarker.setVisible(true);

            } else {
                tempMarker.setVisible(false);
            }
        }
    }

    protected void addMarker() {

        if (map == null)
        {
            return;
        }
        map.clear();
        setCurrentLocation();

        for (int i = 0; i < list.size(); i++) {
            Marker mTemp = map.addMarker(new MarkerOptions()
                    .position(new LatLng(list.get(i).getLocation_latitude(), list.get(i).getLocation_longitude())).title(list.get(i).getTitle())
                    .snippet(list.get(i).getDescription())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    );
            tempRack.put(i, mTemp);

        }

    }

    private boolean isGooglePlay() {
        final int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (status == ConnectionResult.SUCCESS) {
            return true;
        }
        else {
            GooglePlayServicesUtil.getErrorDialog(status, getActivity(), 10).show();
        }
        return false;

    }

    /*******************************************************************/
    /**
     * @param RED
     */
    private void LogR(String msg) {
        if (Constants.enableLog) {
            Log.wtf(tag, msg);
        }
    }

    /**
     * @param text
     */
    private void ToastL(String text) {
        // Toast.makeText(sActivity, text, Toast.LENGTH_LONG).show();
    }

    /*******************************************************************/

    private void setUpMapIfNeeded() {

        if (map == null) {
            map = ((NiceSupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            if (map != null) {
                setUpMap();
            }
        }
        else {
            setUpMap();
        }

    }

    private void setUpMap() {
        setCurrentLocation();
        if (noTouched)
        {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlon, zoom));
        }
        map.setOnMapClickListener(new OnMapClickListener() {

            @Override
            public void onMapClick(LatLng touchedLatLon) {

                noTouched = false;

                System.out.println("LatLon : " + touchedLatLon);

                if (mTextView.getVisibility() == View.VISIBLE) {
                    mTextView.setVisibility(View.GONE);
                    final LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, 0, 5f);
                    mapFrame.setLayoutParams(mParams);
                    ((Home) sActivity).HideTop();
                }
                else {
                    mTextView.setVisibility(View.VISIBLE);
                    final LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                            0, 2f);
                    mapFrame.setLayoutParams(mParams);
                    ((Home) sActivity).ShowTop();
                }

            }
        });

        map.setOnMarkerClickListener(new OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng mLatLng = new LatLng((latitude + .03), longitude);
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, zoom), 300, null);
                return false;
            }
        });

    }

    private void setCurrentLocation() {
        if (markerCurrentLocation != null)
        {
            markerCurrentLocation.remove();
        }
        markerCurrentLocation = map.addMarker(new MarkerOptions().position(latlon).title("My Location"));
    }

}
