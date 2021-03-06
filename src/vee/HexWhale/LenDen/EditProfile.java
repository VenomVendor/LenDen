/**
 * ***Copyright(c)	:	2014-Present, VenomVendor.***
 * Author			:	VenomVendor
 * Dated			:	17 Feb, 2014 3:29:01 AM
 * Project			:	LenDen-Android
 * Client			:	LenDen
 * Contact			:	info@VenomVendor.com
 * URL				:	https://www.google.co.in/search?q=VenomVendor
 * Copyright(c)		:	2014-Present, VenomVendor.
 * License			:	This work is licensed under Attribution-NonCommercial 3.0 Unported
 *						License info at http://creativecommons.org/licenses/by-nc/3.0/deed.en_US
 *						Read More at http://creativecommons.org/licenses/by-nc/3.0/legalcode
 **/

package vee.HexWhale.LenDen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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

import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.MemoryCacheUtil;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.utils.L;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import vee.HexWhale.LenDen.Parsers.Profile.GetEditProfile;
import vee.HexWhale.LenDen.Parsers.Profile.GetProfile;
import vee.HexWhale.LenDen.Storage.GlobalSharedPrefs;
import vee.HexWhale.LenDen.Storage.SettersNGetters;
import vee.HexWhale.LenDen.Utils.Constants.API.HEADERS;
import vee.HexWhale.LenDen.Utils.Constants.API.IMAGEURL;
import vee.HexWhale.LenDen.Utils.Constants.API.STRING;
import vee.HexWhale.LenDen.Utils.Constants.API.TYPE;
import vee.HexWhale.LenDen.Utils.Constants.API.URL;
import vee.HexWhale.LenDen.Utils.Constants.KEY;
import vee.HexWhale.LenDen.bg.Threads.FetcherListener;
import vee.HexWhale.LenDen.bg.Threads.GetData;
import vee.HexWhale.LenDen.bg.Threads.GetDataFromUrl;
import vee.HexWhale.LenDen.bg.Threads.TagGen;
import vee.HexWhale.LenDen.bg.Threads.Validator;

import java.io.File;
import java.util.Locale;

public class EditProfile extends FragmentActivity {

    boolean enableEdit = false;
    boolean isChanged = false;

    View tempView = null;
    final static int tempID = 23;

    String picturePath;
    GlobalSharedPrefs mPrefs;

    static boolean updatedText = true;
    static boolean updatedImage = true;

    GetProfile profile;
    GetDataFromUrl mDataFromUrl;
    DisplayImageOptions optionsDp;
    File cacheDir = new File(Environment.getExternalStorageDirectory(), STRING.CACHE_LOC);
    ImageLoader imageLoader;
    ImageView mSubmit, mDpic;
    private String tag = "UNKNOWN";
    EditText firstName, lastName, eMail;
    String tempFName, tempLName, tempEmail;
    GetEditProfile editProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.edit_profile);
        tag = TagGen.getTag(getClass());
        ((TextView) findViewById(R.id.menu_center)).setText(("edit profile").toUpperCase(Locale.UK));
        firstName = (EditText) findViewById(R.id.edit_profile_name);
        lastName = (EditText) findViewById(R.id.edit_profile_lname);
        eMail = (EditText) findViewById(R.id.edit_profile_mail);

        mDpic = (ImageView) findViewById(R.id.edit_profile_dp);
        mDpic.setFocusable(true);
        mSubmit = (ImageView) findViewById(R.id.menu_right);
        mSubmit.setVisibility(View.INVISIBLE);
        initilizeImageCache();
        mPrefs = new GlobalSharedPrefs(this);
        mDataFromUrl = new GetDataFromUrl(this, mFetcherListener);
        mDataFromUrl.setAccessToken();

        if (mPrefs.getStringInPref(KEY.MY_E_MAIL).equalsIgnoreCase("") || mPrefs.getStringInPref(KEY.MY_E_MAIL) == null)
        {
            mDataFromUrl.GetString(TYPE.PROFILE_ME, getBody(TYPE.PROFILE_ME), GetData.getUrl(URL.PROFILE_ME));
        }
        else {
            firstName.setText(mPrefs.getStringInPref(KEY.MY_F_NAME));
            lastName.setText(mPrefs.getStringInPref(KEY.MY_L_NAME));
            eMail.setText(mPrefs.getStringInPref(KEY.MY_E_MAIL));
            imageLoader.displayImage("" + mPrefs.getStringInPref(KEY.MY_I_URL), mDpic, optionsDp);
            mDpic.setFocusable(true);
            try {
                tempFName = firstName.getText().toString();
                tempLName = lastName.getText().toString();
                tempEmail = eMail.getText().toString();
            }
            catch (final Exception e) {
                e.printStackTrace();
            }

        }
        return;

    }

    private final FetcherListener mFetcherListener = new FetcherListener() {

        @Override
        public void tokenError(String tokenError) {
            ToastL(tokenError);

        }

        @Override
        public void startedParsing(int type) {
        }

        @Override
        public void finishedParsing(int typ) {

            switch (typ) {

                case TYPE.PROFILE_ME:
                    System.out.println("SEtting PROFILE details");
                    profile = SettersNGetters.getProfile();

                    if (profile == null) {
                        ToastL("{ Unknown Profile }");
                        return;
                    }
                    mPrefs.setStringInPref(KEY.MY_F_NAME, profile.getResponse().getFirst_name());
                    mPrefs.setStringInPref(KEY.MY_L_NAME, profile.getResponse().getLast_name());
                    mPrefs.setStringInPref(KEY.MY_E_MAIL, profile.getResponse().getEmail());
                    mPrefs.setStringInPref(KEY.MY_I_URL, "" + GetData.getImageUrl(IMAGEURL.DP + profile.getResponse().getId()));

                    firstName.setText(profile.getResponse().getFirst_name());
                    lastName.setText(profile.getResponse().getLast_name());
                    eMail.setText(profile.getResponse().getEmail());
                    imageLoader.displayImage("" + GetData.getImageUrl(IMAGEURL.DP + profile.getResponse().getId()), mDpic, optionsDp);

                    try {
                        tempFName = firstName.getText().toString();
                        tempLName = lastName.getText().toString();
                        tempEmail = eMail.getText().toString();
                    }
                    catch (final Exception e) {
                        e.printStackTrace();
                    }

                    break;

                case TYPE.PROFILE_EDIT:

                    editProfile = SettersNGetters.getEditProfile();
                    if (editProfile == null) {
                        ToastL("{ Unable to Update }");
                        return;
                    }

                    if (editProfile.getStatus().equalsIgnoreCase(STRING.SUCCESS))
                    {
                        mPrefs.setStringInPref(KEY.MY_F_NAME, firstName.getText().toString());
                        mPrefs.setStringInPref(KEY.MY_L_NAME, lastName.getText().toString());
                        mPrefs.setStringInPref(KEY.MY_E_MAIL, eMail.getText().toString());

                        EditProfile.updatedText = true;
                        if (EditProfile.updatedImage && EditProfile.updatedText)
                        {
                            ToastL("{ Updated Successfully }");
                            finish();
                            AnimNext();
                        }
                    }
                    else {

                        ToastL("{ Couldn't update profile. \n Try again later. }");

                    }
            }

        }

        @Override
        public void finishedFetching(int type, String response) {
            LogR(response);

        }

        @Override
        public void errorFetching(int type, VolleyError error) {
        }

        @Override
        public void beforeParsing(int type) {
        }

        @Override
        public void ParsingException(Exception e) {
        }
    };

    protected void LogR(String msg) {
        Log.wtf(tag, msg);
    }

    public void Finish(View v) {
        finish();
        AnimPrev();
    }

    public void Submit(View v) {
        // finish();

        if (!Validator.hasMinChars(firstName, 3).equalsIgnoreCase("k"))
        {
            firstName.setError(Validator.hasMinChars(firstName, 3));
            return;
        }

        if (!Validator.hasMinChars(lastName, 1).equalsIgnoreCase("k"))
        {
            lastName.setError(Validator.hasMinChars(lastName, 1));
            return;
        }

        if (!Validator.isvalidEmail(eMail).equalsIgnoreCase("k"))
        {
            eMail.setError(Validator.isvalidEmail(eMail));
            return;
        }

        if (!tempFName.equalsIgnoreCase(firstName.getText().toString())
                || !tempLName.equalsIgnoreCase(lastName.getText().toString())
                || !tempEmail.equalsIgnoreCase(eMail.getText().toString()))
        {
            EditProfile.updatedText = false;
            mDataFromUrl.setAccessToken();
            mDataFromUrl.GetString(TYPE.PROFILE_EDIT, getBody(TYPE.PROFILE_EDIT), GetData.getUrl(URL.PROFILE_EDIT));
        }

        if (isChanged)
        {
            EditProfile.updatedImage = false;
            new UploadImage().execute(picturePath);
        }

    }

    private String getBody(int typ) {
        switch (typ) {
            case TYPE.PROFILE_EDIT:
                JSONObject mJsonObject = null;
                mJsonObject = new JSONObject();

                try {
                    mJsonObject.put(STRING.FIRSTNAME, firstName.getText().toString());
                    mJsonObject.put(STRING.LASTNAME, lastName.getText().toString());
                    mJsonObject.put(STRING.EMAIL, eMail.getText().toString());
                    System.out.println(mJsonObject.toString());
                    return mJsonObject.toString();
                }
                catch (final JSONException e) {
                    e.printStackTrace();
                }

                break;

        }
        return null;
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
            this.startActivityForResult(mIntent, EditProfile.tempID);
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
                view.animate().translationX(50)
                .alpha(0.23f)
                .scaleX(0.90f)
                .scaleY(0.90f)
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
                final Animation animation = new TranslateAnimation(25, 0, 0, 0);
                animation.setDuration(350);
                animation.setFillAfter(true);
                view.startAnimation(animation);

            }
            else {
                final Animation animation = new TranslateAnimation(0, 25, 0, 0);
                animation.setDuration(350);
                animation.setFillAfter(true);
                view.startAnimation(animation);
            }
        }

        changeInputType(mEditTExt);
    }

    private void changeInputType(EditText[] mEditTExt) {
        for (final EditText editText : mEditTExt) {

            if (enableEdit)
            {
                mSubmit.setVisibility(View.VISIBLE);
                editText.setEnabled(true);
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                editText.setTextColor(getResources().getColor(R.color.blue_normal));
                editText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
                editText.setFocusable(true);
                editText.setSelected(true);
            }
            else {
                mSubmit.setVisibility(View.INVISIBLE);
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
        if (requestCode == EditProfile.tempID && resultCode == Activity.RESULT_OK && data != null) {
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

            isChanged = true;

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
            final File mFile = new File(params[0]);
            final String url = GetData.getUrl(URL.PROFILE_PIC);

            try
            {
                final HttpClient client = new DefaultHttpClient();
                final HttpPost post = new HttpPost(url);
                final FileBody mFileBody = new FileBody(mFile);
                final MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
                multipartEntity.setMode(HttpMultipartMode.STRICT); // SET MODE
                multipartEntity.addPart(STRING.PICTURE, mFileBody); // ADD BODY
                post.addHeader(HEADERS.ACCESS_TOKEN, mPrefs.getStringInPref(KEY.ACCESS_TOKEN)); // ADD
                // HEADER

                post.setEntity(multipartEntity.build());
                System.out.println(post.getEntity());

                final HttpResponse response = client.execute(post);
                resEntity = response.getEntity();

                if (resEntity != null) {

                    final String response_str = EntityUtils.toString(resEntity);

                    Log.i("RESPONSE", response_str);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                final JSONObject mObject = new JSONObject(response_str);

                                final String statis = mObject.getString(STRING.STATUS);
                                if (statis.equalsIgnoreCase(STRING.SUCCESS))
                                {
                                    EditProfile.updatedImage = true;
                                    final File imageFile = imageLoader.getDiscCache().get(mPrefs.getStringInPref(KEY.MY_I_URL));
                                    if (imageFile.exists()) {
                                        imageFile.delete();
                                    }
                                    MemoryCacheUtil.removeFromCache(mPrefs.getStringInPref(KEY.MY_I_URL), imageLoader.getMemoryCache());

                                    if (EditProfile.updatedImage && EditProfile.updatedText)
                                    {
                                        ToastL("{ Updated Successfully }");
                                        finish();
                                        AnimNext();
                                    }
                                }
                                else {
                                    ToastL("{ Couldn't update Image \n Try again Later. }");
                                }

                            }
                            catch (final Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
            catch (final Exception ex) {
                Log.e("Debug", "error: " + ex.getMessage(), ex);
            }
            return null;
        }

    }

    private void initilizeImageCache() {
        L.disableLogging();
        optionsDp =
                new DisplayImageOptions.Builder()
        .showImageForEmptyUri(R.drawable.camera)
        .showImageOnFail(R.drawable.camera)
        .resetViewBeforeLoading(false)
        .cacheInMemory(true)
        .cacheOnDisc(true)
        .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
        .bitmapConfig(Bitmap.Config.RGB_565)
        .displayer(new RoundedBitmapDisplayer(10))
        .displayer(new FadeInBitmapDisplayer(0))
        .build();

        final ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
        .defaultDisplayImageOptions(optionsDp)
        .threadPriority(Thread.NORM_PRIORITY)
        .threadPoolSize(3)
        .denyCacheImageMultipleSizesInMemory()
        .discCache(new UnlimitedDiscCache(cacheDir))
        // .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
        .tasksProcessingOrder(QueueProcessingType.FIFO)
        .build();

        ImageLoader.getInstance().init(config); // Do it on Application start
        imageLoader = ImageLoader.getInstance();
    }
}
