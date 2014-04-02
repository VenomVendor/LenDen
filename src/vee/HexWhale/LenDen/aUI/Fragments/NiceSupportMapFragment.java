/**
 * ***Copyright(c)	:	2014-Present, VenomVendor.***
 * Author			:	VenomVendor
 * Dated			:	1 Apr, 2014 3:41:21 AM
 * Project			:	LenDen-Android
 * Client			:	LenDen
 * Contact			:	info@VenomVendor.com
 * URL				:	https://www.google.co.in/search?q=VenomVendor
 * Copyright(c)		:	2014-Present, VenomVendor.
 * License			:	This work is licensed under Attribution-NonCommercial 3.0 Unported
 *						License info at http://creativecommons.org/licenses/by-nc/3.0/deed.en_US
 *						Read More at http://creativecommons.org/licenses/by-nc/3.0/legalcode
 * *

 **/

package vee.HexWhale.LenDen.aUI.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;

public class NiceSupportMapFragment extends SupportMapFragment {

    private int detectedBestPixelFormat = -1;
    private View drawingView;

    // Many thanks to Pepsi1x1 for his contribution to this Texture View
    // detection flag
    private final boolean hasTextureViewSupport = android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    private final boolean isRGBA_8888ByDefault = android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;

    private boolean preventParentScrolling = true;

    private View searchAndFindDrawingView(ViewGroup group) {
        final int childCount = group.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = group.getChildAt(i);
            if (child instanceof ViewGroup) {
                final View view = searchAndFindDrawingView((ViewGroup) child);

                if (view != null) {
                    return view;
                }
            }

            if (child instanceof SurfaceView) {
                return child;
            }

            if (hasTextureViewSupport) { // if we have support for texture view
                if (child instanceof TextureView) {
                    return child;
                }
            }
        }
        return null;
    }

    private int detectBestPixelFormat() {

        // Skip check if this is a new device as it will be RGBA_8888 by
        // default.
        if (isRGBA_8888ByDefault) {
            return PixelFormat.RGBA_8888;
        }

        final Context context = getActivity();
        final WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        final Display display = wm.getDefaultDisplay();

        // Get display pixel format
        @SuppressWarnings("deprecation")
        final int displayFormat = display.getPixelFormat();

        if (PixelFormat.formatHasAlpha(displayFormat)) {
            return displayFormat;
        } else {
            return PixelFormat.RGB_565;// Fallback for those who don't support
            // Alpha
        }
    }

    @Override
    @SuppressLint("NewApi")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        final ViewGroup view = (ViewGroup) super.onCreateView(inflater, container,
                savedInstanceState);

        // Transparent Color For Views, android.R.color.transparent dosn't work
        // on all devices
        final int transparent = 0x00000000;

        view.setBackgroundColor(transparent); // Set Root View to be
        // transparent
        // to prevent black screen on
        // load

        drawingView = searchAndFindDrawingView(view); // Find the view the map
        // is using for Open GL

        if (drawingView == null)
        {
            return view; // If we didn't get anything then abort
        }

        drawingView.setBackgroundColor(transparent); // Stop black artifact from
        // being left behind on
        // scroll

        // Create On Touch Listener for MapView Parent Scrolling Fix - Many
        // thanks to Gemerson Ribas (gmribas) for help with this fix.
        final OnTouchListener touchListener = new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                final int action = event.getAction();

                switch (action) {

                    case MotionEvent.ACTION_DOWN:
                        // Disallow Parent to intercept touch events.
                        view.getParent().requestDisallowInterceptTouchEvent(
                                preventParentScrolling);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow Parent to intercept touch events.
                        view.getParent().requestDisallowInterceptTouchEvent(
                                !preventParentScrolling);
                        break;

                }

                // Handle View touch events.
                view.onTouchEvent(event);
                return false;
            }
        };

        // texture view
        if (hasTextureViewSupport) { // If we support texture view and the
            // drawing view is a TextureView then
            // tweak it and return the fragment view

            if (drawingView instanceof TextureView) {

                final TextureView textureView = (TextureView) drawingView;

                // Stop Containing Views from moving when a fbUserName is
                // interacting
                // with Map View Directly
                textureView.setOnTouchListener(touchListener);

                return view;
            }

        }

        // Otherwise continue onto legacy surface view hack
        final SurfaceView surfaceView = (SurfaceView) drawingView;

        // Fix for reducing black view flash issues
        final SurfaceHolder holder = surfaceView.getHolder();

        // Detect Display Format if we havn't already
        if (detectedBestPixelFormat == -1) {
            detectedBestPixelFormat = detectBestPixelFormat();
        }

        // Use detected best pixel format
        holder.setFormat(detectedBestPixelFormat);

        // Stop Containing Views from moving when a fbUserName is interacting
        // with
        // Map View Directly
        surfaceView.setOnTouchListener(touchListener);
        final GoogleMapOptions options = new GoogleMapOptions();
        options.zOrderOnTop(true);
        SupportMapFragment.newInstance(options);
        return view;
    }

    public boolean getPreventParentScrolling() {
        return preventParentScrolling;
    }

    public void setPreventParentScrolling(boolean value) {
        preventParentScrolling = value;
    }

}
