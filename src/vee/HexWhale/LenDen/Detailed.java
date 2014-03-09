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

package vee.HexWhale.LenDen;

import java.util.Locale;

import vee.HexWhale.LenDen.aUI.NoScrollWebView;
import vee.HexWhale.LenDen.aUI.NoScrollWebView.WebViewSizeChanged;
import vee.HexWhale.LenDen.aUI.Pagers.DetailedPager;
import vee.HexWhale.LenDen.viewpagerindicator.CirclePageIndicator;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Detailed extends FragmentActivity implements WebViewSizeChanged {
    NoScrollWebView mWebView;
    ViewPager mPager;

    @SuppressLint("SetJavaScriptEnabled")
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed);

        ((TextView) findViewById(R.id.menu_center)).setText(("sony ps4").toUpperCase(Locale.UK));
        ((ImageView) findViewById(R.id.menu_right)).setImageResource(R.drawable.detailed_up);

        mWebView = (NoScrollWebView) findViewById(R.id.webiew);
        mPager = (ViewPager) findViewById(R.id.pager);

        final CirclePageIndicator mCirclePageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);

        mPager.setAdapter(new DetailedPager(this));

        mCirclePageIndicator.setViewPager(mPager);

        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(webViewClient);
        mWebView.setVerticalScrollBarEnabled(true);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.scrollTo(0, 0);
        mWebView.setFocusable(true);
        final WebSettings settings = mWebView.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setNeedInitialFocus(true);
        settings.setJavaScriptEnabled(true);
        settings.setUserAgentString("Android");
        settings.setDomStorageEnabled(true);
        settings.setAppCachePath(getApplicationContext().getFilesDir().getPath() + "/" + getPackageName() + "/cache");

        System.out.println("++++++++++++++++++++++++++++++++");
        System.out.println(getApplicationContext().getFilesDir().getPath() + "/" + getPackageName() + "/cache");
        System.out.println("++++++++++++++++++++++++++++++++");

        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setRenderPriority(RenderPriority.HIGH);
        settings.setPluginState(PluginState.ON);
        settings.setSavePassword(false);
        settings.setAppCacheMaxSize(1024 * 1024 * 8);

        final String Starting = "<!DOCTYPE html><html><head><style type=\"text/css\">div#veecontent {text-align:justify;padding:1px 10px 20px;}</style></head><body><div id=\"veecontent\">";

        String contentToLoad = ""
                + "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas non sapien erat. Duis nisi nisi, liquam velit ligula, condimentum et lorem quis, pharetra laoreet diam.</p>\r\n"
                + "<img src=\"https://fbcdn-dragon-a.akamaihd.net/hphotos-ak-prn1/t39.1997/851568_501183173335094_1782670102_n.png\"/>"
                + "<p>Nulla rhoncus lobortis pulvina. Vestibulum felis diam, volutpat non congue at, tincidunt quis libero. Nam elit enim, lacinia et arcu in, laoreet facilisis elit. Curabitur aliquam justo vitae ante fringilla, vitae fermentum turpis sagittis. In ut ullamcorper nibh. Integer faucibus ante sed nisl malesuada molestie. Nunc placerat condimentum lacus. Phasellus viverra tristique eros id rutrum. Sed rhoncus augue ullamcorper, adipiscing dolor at, commodo orci. Duis erat nisi, ultrices nec sodales volutpat, pretium vel quam. Ut a eros mollis, condimentum est et, iaculis sapien. Morbi porta, diam ac viverra venenatis, odio ligula consectetur nunc, quis congue tellus nulla eu lectus. Ut eu blandit tortor. Aliquam sapien erat, egestas vel dolor eget, tincidunt lacinia ligula. Maecenas a odio ullamcorper, placerat libero at, viverra dolor. Proin hendrerit, odio non mollis feugiat, neque tortor elementum eros, ut ullamcorper ante lacus nec metus.</p>\r\n"
                + "<p>Donec vitae ante magna. Cras tristique, lectus non tempor ultrices, justo felis vulputate enim, ut rhoncus est dui sit amet sem. Proin tincidunt imperdiet massa nec bibendum. Suspendisse a nisi dolor. Nam fermentum, lacus placerat lobortis varius, orci ipsum tempor urna, in rhoncus enim nisl in velit. Maecenas nisl lacus, feugiat vel bibendum nec, interdum in velit. Nam quis tortor consequat, pretium tellus eget, hendrerit eros. Nam eget neque leo. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Etiam sed sodales mi.</p>\r\n"
                + "<p>Donec at laoreet nisi. Vestibulum a dictum dui. Sed a pharetra lacus. Curabitur in diam eget sapien elementum tempor. Maecenas elementum metus sit amet adipiscing volutpat. Donec ac gravida erat. Fusce sagittis metus a viverra dapibus.</p>\r\n"
                + "<p>Mauris rutrum lorem vitae porttitor eleifend. Nam orci mauris, placerat feugiat euismod non, imperdiet a ligula. Proin luctus mattis leo, convallis luctus nisi suscipit et. Duis imperdiet arcu vitae nibh ultrices laoreet. Vestibulum pulvinar bibendum nunc, adipiscing elementum libero aliquam non. Morbi mi dolor, rhoncus sed nulla posuere, euismod venenatis mi. Vivamus non urna eu justo sagittis eleifend. Nulla iaculis eros ac auctor fermentum. Nulla ut interdum lorem, quis ultricies erat. Proin pulvinar, justo eu tempus fermentum, urna ante convallis odio, eget lacinia nulla nibh eu purus. Donec tempus sapien sed risus gravida, id faucibus tortor posuere. Nullam luctus tempus nisl non facilisis. Pellentesque vel mauris sagittis, feugiat eros sed, convallis arcu. Sed rhoncus sodales facilisis. Vivamus vestibulum tempus metus nec ultricies.</p>\r\n";

        final String Ending = "</div></body></html>";

        mWebView.loadDataWithBaseURL("file:///android_asset/", Starting + contentToLoad + Ending, "text/html", "UTF-8", null);
        mWebView.setWebViewSizeChanged(this);
    }

    private final WebViewClient webViewClient = new WebViewClient() {

        @Override
        public void onPageStarted(final WebView view, final String url, final Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(final WebView view, final String url) {
            Log.wtf("", url);
        }

        @Override
        public void onReceivedError(final WebView view, final int errorCode, final String description, final String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

            System.out.println("errorCode " + errorCode);
            System.out.println("description " + description);
            System.out.println("failingUrl " + failingUrl);

            Toast.makeText(getApplicationContext(), description, Toast.LENGTH_SHORT).show();
            // webView.loadUrl(url);
        }

    };

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
    public void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        System.out.println("XTXwidth " + width);
        System.out.println("XTXheight " + height);
        System.out.println("XTXoldWidth " + oldWidth);
        System.out.println("XTXoldHeight " + oldHeight);
    }

}
