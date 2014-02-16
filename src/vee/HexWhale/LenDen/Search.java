
package vee.HexWhale.LenDen;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import vee.HexWhale.LenDen.aUI.SearchListAdapter;

public class Search extends FragmentActivity {
    static GoogleMap map = null;
    double latitude = 12.971689;
    double longitude = 77.594504;

    LatLng latlon = null;
    public static boolean googlePlayOn = false;

    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        latlon = new LatLng(latitude, longitude);
        setUpMapIfNeeded(); // Required to check the availability of Maps
        mListView = (ListView) findViewById(R.id.search_list);
        SearchListAdapter adapter = new SearchListAdapter(this);
        mListView.setAdapter(adapter);
    }

    public void Finish(View v) {
        finish();
        AnimPrev();
    }

    public void Filter(View v) {
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
        map.getUiSettings().setZoomControlsEnabled(false);
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

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}
