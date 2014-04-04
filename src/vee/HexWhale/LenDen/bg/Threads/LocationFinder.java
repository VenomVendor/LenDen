/**
 * ***Copyright(c)	:	2014-Present, VenomVendor.***
 * Author			:	VenomVendor
 * Dated			:	1 Apr, 2014 12:38:45 AM
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

package vee.HexWhale.LenDen.bg.Threads;

import android.app.AlarmManager;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;


public class LocationFinder implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener
{
    private LocationClient mLocationClient;
    private Location location = null;

    protected LocationManager locationManager;
    Context mContext;
    Criteria criteria;
    private LocListner mLocListner;

    private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(1000 * 60 * 5) // 5 seconds
            .setFastestInterval(16) // 16ms = 60fps
            .setSmallestDisplacement(50)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    public LocationFinder(Context mContext , LocListner locListner) {
        this.mContext = mContext;
        this.mLocListner = locListner;
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
       // final List<String> matchingProviders = locationManager.getAllProviders();

        location = locationManager.getLastKnownLocation(getCriteria());
        if (location != null) {
            final float accuracy = location.getAccuracy();
            final long time = location.getTime();

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


        // If the best result is beyond the allowed time limit, or the accuracy
        // of the
        // best result is wider than the acceptable maximum distance, request a
        // single update.
        // This check simply implements the same conditions we set when
        // requesting regular
        // location updates every [minTime] and [minDistance].
        if ((bestTime < minTime || bestAccuracy > minDistance)) {

            locationManager.requestLocationUpdates( getCriteria(), 0, 0, mListener);
        }
        return bestResult;
    }

    private String getCriteria() {
        this.criteria = new Criteria();
        this.criteria.setAccuracy(Criteria.ACCURACY_FINE);
        this.criteria.setAltitudeRequired(false);
        this.criteria.setBearingRequired(false);
        this.criteria.setCostAllowed(true);
        this.criteria.setPowerRequirement(Criteria.POWER_LOW);
        this.criteria.setSpeedRequired(false);
        return locationManager.getBestProvider(criteria, true);
    }

    android.location.LocationListener mListener = new android.location.LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

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

    }

    @Override
    public void onConnected(Bundle connectionHint) {
        mLocationClient.requestLocationUpdates(LocationFinder.REQUEST, this); // LocationListener
        setLocation(getLastBestLocation(500, System.currentTimeMillis() - (AlarmManager.INTERVAL_FIFTEEN_MINUTES / 4)));
        locationManager.requestLocationUpdates(getCriteria(), 0, 0, mListener);
    }

    @Override
    public void onDisconnected() {
        stopUpdates();
    }

    public void stopUpdates()
    {
        if (mLocationClient != null)
        {
            locationManager.removeUpdates(mListener);
            mLocationClient.removeLocationUpdates(this);
        }
    }

    /**
     * <pre>
     *
     * // Add this method
     * &#064;Override
     * protected void onDestroy() {
     *     myLocation.stopUpdates();
     *     super.onDestroy();
     * }
     * </pre>
     */
    public synchronized Location getLocation() {

        if (location == null)
        {
            setLocation(getLastBestLocation(500, System.currentTimeMillis() - (AlarmManager.INTERVAL_FIFTEEN_MINUTES / 4)));

        }
        return location;
    }

    public synchronized void setLocation(Location location) {
        mLocListner.gotLocation(location);
        this.location = location;
    }


    public interface LocListner
    {
        public void gotLocation(Location location);
    }

}
