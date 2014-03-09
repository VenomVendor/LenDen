/**
 * ***Copyright(c) : 2014-Present, VenomVendor.***
 * Author : VenomVendor
 * Dated : 18 Feb, 2014 4:25:32 PM
 * Project : LenDen-Android
 * Client : LenDen
 * Contact : info@VenomVendor.com
 * URL : https://www.google.co.in/search?q=VenomVendor
 * Copyright(c) : 2014-Present, VenomVendor.
 * License : This work is licensed under Attribution-NonCommercial 3.0 Unported (CC BY-NC 3.0).
 * License info at http://creativecommons.org/licenses/by-nc/3.0/deed.en_US
 * Read More at http://creativecommons.org/licenses/by-nc/3.0/legalcode
 **/

package vee.HexWhale.LenDen.aUI;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class NoScrollWebView extends WebView {
    WebViewSizeChanged mViewSizeChanged;

    public void setWebViewSizeChanged(WebViewSizeChanged mViewSizeChanged) {
        this.mViewSizeChanged = mViewSizeChanged;
    }

    public NoScrollWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public NoScrollWebView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public NoScrollWebView(Context context) {
        super(context);

    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        this.mViewSizeChanged.onSizeChanged(width, height, oldWidth, oldHeight);
    }

    public interface WebViewSizeChanged {
        public void onSizeChanged(int width, int height, int oldWidth, int oldHeight);
    }
}
