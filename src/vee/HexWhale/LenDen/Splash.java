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

package vee.HexWhale.LenDen;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import vee.HexWhale.LenDen.Parsers.AccessToken.GetAccessToken;
import vee.HexWhale.LenDen.Parsers.AuthCode.GetAuthCode;
import vee.HexWhale.LenDen.Storage.GlobalSharedPrefs;
import vee.HexWhale.LenDen.Utils.Constants.API.TYPE;
import vee.HexWhale.LenDen.Utils.Constants.KEY;
import vee.HexWhale.LenDen.bg.Threads.FetcherListener;
import vee.HexWhale.LenDen.bg.Threads.GetTokens;
import vee.HexWhale.LenDen.bg.Threads.NetworkConnection;
import vee.HexWhale.LenDen.bg.Threads.TagGen;

public class Splash extends Activity {

    private String tag = "UNKNOWN";
    int type = -1;
    GetAuthCode mAuthCode;
    GetAccessToken mAccessToken;
    GlobalSharedPrefs mPrefs;
    GetTokens mGetTokens;
    ProgressBar mBar;
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.tag = TagGen.getTag(this.getClass());
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.splash);
        this.mBar = (ProgressBar) this.findViewById(R.id.splash_progress);
        this.mTextView = (TextView) this.findViewById(R.id.splash_text);
        this.checkInternet();
    }

    private void checkInternet() {
        if (!NetworkConnection.isAvail(this.getApplicationContext())) {
            this.ToastL("No internet Connection");
            this.mBar.setVisibility(View.GONE);
            this.setAlert("No internet connection");
            return;
        }
        this.mPrefs = new GlobalSharedPrefs(this);

        this.mGetTokens = new GetTokens(this, this.mFetcherListener);

        if (this.mPrefs.getStringInPref(KEY.REFRESH_TOKEN) == null) {
            this.mTextView.setText("Authorizing...");
            this.LogBlk("Authorizing");
            this.mGetTokens.getAuthToken();
        } else {
            this.mTextView.setText("Refreshing...");
            this.LogBlk("Refreshing");
            this.mGetTokens.refreshToken();
        }

    }

    private void setAlert(String title) {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Splash.this);
        alertDialog.setTitle(title);
        alertDialog.setMessage("Retry?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Splash.this.checkInternet();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Splash.this.finish();
            }
        });

        alertDialog.show();

    }

    private final FetcherListener mFetcherListener = new FetcherListener() {

        @Override
        public void errorFetching(int type, VolleyError error) {
            // TODO Auto-generated method stub

        }

        @Override
        public void finishedFetching(int type, String response) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeParsing(int type) { // used as alternative

            Splash.this.mTextView.setText("Validating/Accessing...");
            Splash.this.LogBlk("Validating/Accessing");

        }

        @Override
        public void startedParsing(int type) {
            // TODO Auto-generated method stub

        }

        @Override
        public void ParsingException(Exception e) {
            // TODO Auto-generated method stub

        }

        @Override
        public void finishedParsing(int typ) {

            switch (typ) {
                case TYPE.REFRESH:
                    Splash.this.startNextActivity();
                    break;
                case TYPE.ACCESSTOKEN:
                    Splash.this.LogBlk("Accessing");
                    Splash.this.startNextActivity();
                    break;

                default:
                    break;
            }

        }

        @Override
        public void tokenError(String tokenError) {

            ToastL(tokenError);
        }

    };

    /*******************************************************************/

    protected void startNextActivity() {
        this.startActivity(new Intent(this.getApplicationContext(), WalkThrough.class));
        this.finish();
        this.AnimNext();
    }

    private void AnimNext() {
        this.overridePendingTransition(R.anim.enter, R.anim.exit);
        return;
    }

    /**
     * @param RED
     */
    public void LogR(String msg) {
        Log.wtf(this.tag, msg);
    }

    /**
     * @param Black
     */
    public void LogBlk(String msg) {
        Log.v(this.tag, msg);
    }

    /**
     * @param text
     */
    private void ToastL(String text) {
        Toast.makeText(this.getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    /*******************************************************************/
}
