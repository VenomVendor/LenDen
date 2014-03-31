/**
 * ***Copyright(c) : 2014-Present, VenomVendor.***
 * Author : VenomVendor
 * Dated : 17 Feb, 2014 3:28:57 AM
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

package vee.HexWhale.LenDen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import vee.HexWhale.LenDen.Parsers.Profile.ChangePassword;
import vee.HexWhale.LenDen.Storage.GlobalSharedPrefs;
import vee.HexWhale.LenDen.Storage.SettersNGetters;
import vee.HexWhale.LenDen.Utils.Constants.KEY;
import vee.HexWhale.LenDen.Utils.Constants.API.IMAGEURL;
import vee.HexWhale.LenDen.Utils.Constants.API.STRING;
import vee.HexWhale.LenDen.Utils.Constants.API.TYPE;
import vee.HexWhale.LenDen.Utils.Constants.API.URL;
import vee.HexWhale.LenDen.aUI.Adapters.ProfileListAdapter;
import vee.HexWhale.LenDen.bg.Threads.FetcherListener;
import vee.HexWhale.LenDen.bg.Threads.GetData;
import vee.HexWhale.LenDen.bg.Threads.GetDataFromUrl;
import vee.HexWhale.LenDen.bg.Threads.TagGen;

public class Settings extends FragmentActivity {

    TextView mTitle, profileSettings;
    ImageView mOk;
    GlobalSharedPrefs mPrefs;

    AlertDialog.Builder builder;
    AlertDialog dialog;
    GetDataFromUrl mDataFromUrl;
    private String tag = "UNKNOWN";
    ChangePassword changePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tag = TagGen.getTag(getClass());
        mPrefs = new GlobalSharedPrefs(this);
        this.setContentView(R.layout.settings);
        mTitle = (TextView) findViewById(R.id.menu_center);
        mTitle.setText("SETTINGS");

        profileSettings = (TextView) findViewById(R.id.settings_profile);

        mOk = (ImageView) findViewById(R.id.menu_right);
        mOk.setVisibility(View.INVISIBLE);
        mDataFromUrl = new GetDataFromUrl(this, mFetcherListener);

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

            switch (typ) {

                case TYPE.CHANGE_PASSWORD:

                    changePassword = SettersNGetters.getChangePassword();

                    if (changePassword == null) {
                        ToastL("{ Unknown Error }");
                        return;
                    }

                    if (changePassword.getStatus().equalsIgnoreCase(STRING.SUCCESS))
                    {
                        ToastL("{ Password Changed Successfully }");
                    }
                    else {
                        ToastL(changePassword.getError_message());
                    }

                    break;

            }

        }

        @Override
        public void finishedFetching(int type, String response) {
            LogR(response);

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
            // TODO Auto-generated method stub

        }
    };

    protected void LogR(String msg) {
        Log.wtf(tag, msg);
    }

    /**
     * @param text
     */
    private void ToastL(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        profileSettings.setText(mPrefs.getStringInPref(KEY.MY_F_NAME).equalsIgnoreCase("") ? "XXXXXXX" : mPrefs.getStringInPref(KEY.MY_F_NAME));
    }

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
        finish();
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

    public void EditProfile(View view) {
        this.startActivity(new Intent(getApplicationContext(), EditProfile.class));
        AnimNext();
    }

    public void EditPassword(View v) {

        justAnotherMethod("");

        AnimNext();
    }

    protected void justAnotherMethod(String oldPassword) {

        builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Edit Password");
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.change_password, null);
        final EditText oldPass = (EditText) promptsView.findViewById(R.id.change_password_old);
        final EditText input = (EditText) promptsView.findViewById(R.id.change_password_new);
        oldPass.setText(oldPassword);
        builder.setView(promptsView);

        input.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null)
                {
                    if (s.toString().trim().length() > 5 && oldPass.getText().length() > 0)
                    {
                        dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(true);
                    }
                    else {
                        dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
                    }
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                if (input != null)
                {
                    goOutSideWithPassword(oldPass.getText().toString(), input.getText().toString().trim());
                }
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        dialog = builder.create();
        dialog.show();
        dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);

    }

    protected void goOutSideWithPassword(final String oldPassword, String newPassword) {

        if (newPassword.contains(" "))
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Warning!");
            alertDialog.setMessage("Password cannot have 'WhiteSpace'\n would you like to update?");
            alertDialog.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            justAnotherMethod(oldPassword);

                        }
                    });
            alertDialog.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    });

            alertDialog.show();
            return;
        }

        JSONObject mJsonObject = null;
        mJsonObject = new JSONObject();
        try {
            mJsonObject.put(STRING.CURRENT_PASSWORD, oldPassword);
            mJsonObject.put(STRING.NEW_PASSWORD, newPassword);
            System.out.println(mJsonObject.toString());
            mDataFromUrl.setAccessToken();
            mDataFromUrl.GetString(TYPE.CHANGE_PASSWORD, mJsonObject.toString(), GetData.getUrl(URL.CHANGE_PASSWORD));

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void Notifications(View v) {
        this.startActivity(new Intent(getApplicationContext(), Notifications.class));
        AnimNext();
    }

}
