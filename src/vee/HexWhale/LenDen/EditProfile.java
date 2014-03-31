/**
 * ***Copyright(c) : 2014-Present, VenomVendor.***
 * Author : VenomVendor
 * Dated : 17 Feb, 2014 3:29:01 AM
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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import vee.HexWhale.LenDen.Parsers.Profile.GetProfile;
import vee.HexWhale.LenDen.Storage.GlobalSharedPrefs;
import vee.HexWhale.LenDen.Utils.Constants.API.HEADERS;
import vee.HexWhale.LenDen.Utils.Constants.API.STRING;
import vee.HexWhale.LenDen.Utils.Constants.API.URL;
import vee.HexWhale.LenDen.Utils.Constants.KEY;
import vee.HexWhale.LenDen.bg.Threads.GetData;

import java.io.File;
import java.util.Locale;

public class EditProfile extends FragmentActivity {

    boolean enableEdit = false;
    View tempView = null;
    final static int tempID = 23;
    String picturePath;
    GlobalSharedPrefs mPrefs;

    GetProfile profile;
    ImageView menu_right;
    EditText firstName, lastName, eMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.edit_profile);

        ((TextView) findViewById(R.id.menu_center)).setText(("edit profile").toUpperCase(Locale.UK));
        firstName = (EditText) findViewById(R.id.edit_profile_name);
        lastName = (EditText) findViewById(R.id.edit_profile_lname);
        eMail = (EditText) findViewById(R.id.edit_profile_mail);
        menu_right = (ImageView) findViewById(R.id.menu_right);
        menu_right.setVisibility(View.INVISIBLE);
        // edit_profile_name
        mPrefs = new GlobalSharedPrefs(this);
    }

    public void Finish(View v) {
        finish();
        AnimPrev();
    }

    public void Submit(View v) {
        // finish();
        new UploadImage().execute(picturePath);
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

    public void OpenImage(View view) {

        if (enableEdit)
        {
            tempView = view;
            final Intent mIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            this.startActivityForResult(mIntent, AddItems.tempID);
        }
    }

    @SuppressLint("NewApi")
    public void EnableEdit(View view) {
        enableEdit = !enableEdit;
        final EditText[] mEditTExt = {
                firstName, lastName, eMail
        };

        if (android.os.Build.VERSION.SDK_INT > 11)
        {
            if (enableEdit)
            {
                view.animate().translationX(25)
                        .alpha(0.23f)
                        .scaleX(0.75f)
                        .scaleY(0.75f)
                        .setDuration(350)
                        .setListener(null);
            }
            else {
                view.animate().translationX(0)
                        .alpha(1f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(350)
                        .setListener(null);
            }
        }
        else {
            if (enableEdit)
            {
                Animation animation = new TranslateAnimation(25, 0, 0, 0);
                animation.setDuration(350);
                animation.setFillAfter(true);
                view.startAnimation(animation);

            }
            else {
                Animation animation = new TranslateAnimation(0, 25, 0, 0);
                animation.setDuration(350);
                animation.setFillAfter(true);
                view.startAnimation(animation);
            }
        }

        changeInputType(mEditTExt);
    }

    private void changeInputType(EditText[] mEditTExt) {
        for (EditText editText : mEditTExt) {

            if (enableEdit)
            {
                menu_right.setVisibility(View.VISIBLE);
                editText.setEnabled(true);
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                editText.setTextColor(getResources().getColor(R.color.blue_normal));
                editText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
                editText.setFocusable(true);
            }
            else {
                menu_right.setVisibility(View.INVISIBLE);
                editText.setEnabled(false);
                editText.setFocusable(false);
                editText.setTextColor(getResources().getColor(R.color.list_txt));
                editText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
                editText.setInputType(InputType.TYPE_NULL);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AddItems.tempID && resultCode == Activity.RESULT_OK && data != null) {
            final Uri selectedImage = data.getData();
            final String[] filePathColumn = {
                    MediaColumns.DATA
            };
            final Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            final int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
            ((ImageView) tempView).setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }
        else {
            ToastL("Unable to select image...");
        }
    }

    private void ToastL(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    public class UploadImage extends AsyncTask<String, Integer, String>
    {
        String picPath = "";
        HttpEntity resEntity;

        @Override
        protected String doInBackground(String... params) {
            File mFile = new File(params[0]);
            String url = GetData.getUrl(URL.PROFILE_PIC);

            try
            {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(url);
                FileBody mFileBody = new FileBody(mFile);
                MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
                multipartEntity.setMode(HttpMultipartMode.STRICT); // SET MODE
                multipartEntity.addPart(STRING.PICTURE, mFileBody); // ADD BODY
                post.addHeader(HEADERS.ACCESS_TOKEN, mPrefs.getStringInPref(KEY.ACCESS_TOKEN)); // ADD
                                                                                                // HEADER

                post.setEntity(multipartEntity.build());
                System.out.println(post.getEntity());

                HttpResponse response = client.execute(post);
                resEntity = response.getEntity();

                if (resEntity != null) {
                    final String response_str = EntityUtils.toString(resEntity);
                    Log.i("RESPONSE", response_str);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                Toast.makeText(getApplicationContext(), "Upload Complete. Check the server uploads directory.", Toast.LENGTH_LONG)
                                        .show();
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
            catch (Exception ex) {
                Log.e("Debug", "error: " + ex.getMessage(), ex);
            }
            return null;
        }

    }

}
