/**
 * ***Copyright(c)	:	2014-Present, VenomVendor.***
 * Author		:	VenomVendor
 * Dated		:	1 Apr, 2014 12:38:45 AM
 * Project		:	LenDen-Android
 * Client		:	LenDen
 * Contact		:	info@VenomVendor.com
 * URL			:	https://www.google.co.in/search?q=VenomVendor
 * Copyright(c)	:	2014-Present, VenomVendor.
 * License		:	This work is licensed under Attribution-NonCommercial 3.0 Unported (CC BY-NC 3.0).
 *					License info at http://creativecommons.org/licenses/by-nc/3.0/deed.en_US
 *					Read More at http://creativecommons.org/licenses/by-nc/3.0/legalcode
 **/

package vee.HexWhale.LenDen.bg.Threads;

import android.app.AlarmManager;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import java.util.List;

public class LocationFinder implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener
{
    private LocationClient mLocationClient;
    private Location location = null;

    protected LocationManager locationManager;
    Context mContext;

    private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(1000 * 60 * 5) // 5 seconds
            .setFastestInterval(16) // 16ms = 60fps
            .setSmallestDisplacement(50)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    public LocationFinder(Context mContext) {
        this.mContext = mContext;
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        setUpLocationClientIfNeeded();
        mLocationClient.connect();
    }

    private void setUpLocationClientIfNeeded() {
        if (mLocationClient == null) {
            mLocationClient = new LocationClient(mContext,
                    this, // ConnectionCallbacks
                    this); // OnConnectionFailedListener
        }
    }

    public Location getLastBestLocation(int minDistance, long minTime)
    {

        if (!mLocationClient.isConnected())
        {
            return location;
        }
        if (location != null)
        {
            if ((location.getTime() - System.currentTimeMillis()) < (1000 * 60 * 5)) {
                return location;
            }
        }

        Location bestResult = mLocationClient.getLastLocation();
        float bestAccuracy = Float.MAX_VALUE;
        long bestTime = Long.MIN_VALUE;

        // Iterate through all the providers on the system, keeping
        // note of the most accurate result within the acceptable time limit.
        // If no result is found within maxTime, return the newest Location.
        List<String> matchingProviders = locationManager.getAllProviders();
        for (String provider : matchingProviders) {
            location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                float accuracy = location.getAccuracy();
                long time = location.getTime();

                if ((time > minTime && accuracy < bestAccuracy)) {
                    bestResult = location;
                    bestAccuracy = accuracy;
                    bestTime = time;
                }
                else
                    if (time < minTime &&
                            bestAccuracy == Float.MAX_VALUE && time > bestTime) {
                        bestResult = location;
                        bestTime = time;
                    }
            }
        }

        // If the best result is beyond the allowed time limit, or the accuracy
        // of the
        // best result is wider than the acceptable maximum distance, request a
        // single update.
        // This check simply implements the same conditions we set when
        // requesting regular
        // location updates every [minTime] and [minDistance].
        if ((bestTime < minTime || bestAccuracy > minDistance)) {

            for (String provider : matchingProviders) {
                locationManager.requestLocationUpdates(provider, 0, 0, (android.location.LocationListener) mListener);
            }
        }
        return bestResult;
    }

    LocationListener mListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            setLocation(location);
        }
    };

    @Override
    public void onLocationChanged(Location location) {
        setLocation(location);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnected(Bundle connectionHint) {
        mLocationClient.requestLocationUpdates(REQUEST, this); // LocationListener
    }

    @Override
    public void onDisconnected() {
        stopUpdates();
    }

    public void stopUpdates()
    {
        if (mLocationClient != null)
        {
            mLocationClient.removeLocationUpdates(mListener);
            mLocationClient.removeLocationUpdates(this);
        }
    }


    /**
     *  <pre>
     *
        //Add this method
        &nbsp;@Override
        protected void onDestroy() {
            myLocation.stopUpdates();
            super.onDestroy();
        }</pre>
     */
    public synchronized Location getLocation() {

        if (location == null)
        {
            setLocation(this.getLastBestLocation(500, System.currentTimeMillis() - (AlarmManager.INTERVAL_FIFTEEN_MINUTES / 4)));
        }
        return location;
    }

    public synchronized void setLocation(Location location) {
        this.location = location;
    }

}
