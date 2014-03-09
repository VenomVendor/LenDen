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

import vee.HexWhale.LenDen.Parsers.AccessToken.GetAccessToken;
import vee.HexWhale.LenDen.Parsers.AuthCode.GetAuthCode;
import vee.HexWhale.LenDen.Storage.GlobalSharedPrefs;
import vee.HexWhale.LenDen.Utils.Constants.API.TYPE;
import vee.HexWhale.LenDen.Utils.Constants.KEY;
import vee.HexWhale.LenDen.bg.Threads.FetcherListener;
import vee.HexWhale.LenDen.bg.Threads.GetTokens;
import vee.HexWhale.LenDen.bg.Threads.NetworkConnection;
import vee.HexWhale.LenDen.bg.Threads.TagGen;
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
        tag = TagGen.getTag(this.getClass());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        mBar = (ProgressBar) findViewById(R.id.splash_progress);
        mTextView = (TextView) findViewById(R.id.splash_text);
        checkInternet();
    }

    private void checkInternet() {
        if (!NetworkConnection.isAvail(getApplicationContext())) {
            ToastL("No internet Connection");
            mBar.setVisibility(View.GONE);
            setAlert("No internet connection");
            return;
        }
        mPrefs = new GlobalSharedPrefs(this);

        mGetTokens = new GetTokens(this, mFetcherListener);

        if (mPrefs.getStringInPref(KEY.REFRESH_TOKEN) == null) {
            mTextView.setText("Authorizing...");
            LogBlk("Authorizing");
            mGetTokens.getAuthToken();
        } else {
            mTextView.setText("Refreshing...");
            LogBlk("Refreshing");
            mGetTokens.refreshToken();
        }

    }

    private void setAlert(String title) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Splash.this);
        alertDialog.setTitle(title);
        alertDialog.setMessage("Retry?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkInternet();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        alertDialog.show();

    }

    private FetcherListener mFetcherListener = new FetcherListener() {

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

            mTextView.setText("Validating/Accessing...");
            LogBlk("Validating/Accessing");

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
                    startNextActivity();
                    break;
                case TYPE.ACCESSTOKEN:
                    LogBlk("Accessing");
                    startNextActivity();
                    break;

                default:
                    break;
            }

        }

        @Override
        public void tokenError(String tokenError) {

            setAlert(tokenError);
        }

    };

    /*******************************************************************/

    protected void startNextActivity() {
        startActivity(new Intent(getApplicationContext(), WalkThrough.class));
        finish();
        AnimNext();
    }

    private void AnimNext() {
        overridePendingTransition(R.anim.enter, R.anim.exit);
        return;
    }

    /**
     * @param RED
     */
    public void LogR(String msg) {
        Log.wtf(tag, msg);
    }

    /**
     * @param Black
     */
    public void LogBlk(String msg) {
        Log.v(tag, msg);
    }

    /**
     * @param text
     */
    private void ToastL(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    /*******************************************************************/
}
