/**
 * ***Copyright(c)  :   2014-Present, VenomVendor.***
 * Author           :   VenomVendor
 * Dated            :   17 Feb, 2014 3:29:01 AM
 * Project          :   LenDen-Android
 * Client           :   LenDen
 * Contact          :   info@VenomVendor.com
 * URL              :   https://www.google.co.in/search?q=VenomVendor
 * Copyright(c)     :   2014-Present, VenomVendor.
 * License          :   This work is licensed under Attribution-NonCommercial 3.0 Unported
 *                      License info at http://creativecommons.org/licenses/by-nc/3.0/deed.en_US
 *                      Read More at http://creativecommons.org/licenses/by-nc/3.0/legalcode
 **/

package vee.HexWhale.LenDen;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.core.assist.MemoryCacheUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import vee.HexWhale.LenDen.Parsers.AddItems.GetAddItems;
import vee.HexWhale.LenDen.Parsers.Categories.GetCategory;
import vee.HexWhale.LenDen.Parsers.Categories.Response;
import vee.HexWhale.LenDen.Parsers.SubCategories.GetSubCategory;
import vee.HexWhale.LenDen.Storage.GlobalSharedPrefs;
import vee.HexWhale.LenDen.Storage.SettersNGetters;
import vee.HexWhale.LenDen.Utils.Constants;
import vee.HexWhale.LenDen.Utils.Constants.KEY;
import vee.HexWhale.LenDen.Utils.Constants.API.HEADERS;
import vee.HexWhale.LenDen.Utils.Constants.API.STRING;
import vee.HexWhale.LenDen.Utils.Constants.API.TYPE;
import vee.HexWhale.LenDen.Utils.Constants.API.URL;
import vee.HexWhale.LenDen.bg.Threads.FetcherListener;
import vee.HexWhale.LenDen.bg.Threads.GetData;
import vee.HexWhale.LenDen.bg.Threads.GetDataFromUrl;
import vee.HexWhale.LenDen.bg.Threads.LocationFinder;
import vee.HexWhale.LenDen.bg.Threads.TagGen;
import vee.HexWhale.LenDen.bg.Threads.Validator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AddItems extends FragmentActivity implements OnClickListener {

    private String tag = TagGen.getTag(this.getClass());
    private GetDataFromUrl mDataFromUrl;
    private GetCategory category;
    private GetSubCategory subCategory;
    private EditText mTitle, mOrgPrice, oSellgPrice, mLocation, mDescription;
    private GetAddItems addItems;
    private Spinner mCategory, mSubCategory;
    private String sCategory = "", sSubCategory = "";
    List<Response> mCate;
    List<vee.HexWhale.LenDen.Parsers.SubCategories.Response> mSubCate;
    GlobalSharedPrefs mPrefs;
    ImageView mImg1, mImg2, mImg3, mImg4, mImg5;
    String picturePath1, picturePath2, picturePath3, picturePath4, picturePath5;
    final int picReqCode1 = 1000, picReqCode2 = 2000, picReqCode3 = 3000, picReqCode4 = 4000, picReqCode5 = 5000;
    List<String> list = new ArrayList<String>();
    List<String> sublist = new ArrayList<String>();

    LocationFinder myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myLocation = new LocationFinder(getApplicationContext());
        mDataFromUrl = new GetDataFromUrl(this, mFetcherListener);
        this.setContentView(R.layout.add_items);
        mPrefs = new GlobalSharedPrefs(this);
        mTitle = (EditText) findViewById(R.id.add_item_title);
        mOrgPrice = (EditText) findViewById(R.id.add_item_price_org);
        oSellgPrice = (EditText) findViewById(R.id.add_item_price_sellg);
        mLocation = (EditText) findViewById(R.id.add_item_location);
        mDescription = (EditText) findViewById(R.id.add_item_description);
        mCategory = (Spinner) findViewById(R.id.add_item_cate);
        mSubCategory = (Spinner) findViewById(R.id.add_item_sub_cate);

        mImg1 = (ImageView) findViewById(R.id.add_item_img_1);
        mImg2 = (ImageView) findViewById(R.id.add_item_img_2);
        mImg3 = (ImageView) findViewById(R.id.add_item_img_3);
        mImg4 = (ImageView) findViewById(R.id.add_item_img_4);
        mImg5 = (ImageView) findViewById(R.id.add_item_img_5);

        mImg1.setOnClickListener(this);
        mImg2.setOnClickListener(this);
        mImg3.setOnClickListener(this);
        mImg4.setOnClickListener(this);
        mImg5.setOnClickListener(this);

        category = SettersNGetters.getCategory();
        if (category == null)
        {
            setTempAdapter();
            return;
        }

        if (category.getStatus().equalsIgnoreCase(STRING.ERROR))
        {
            setTempAdapter();
            return;
        }

        mCate = category.getResponse();
        list.add("Select Category");
        for (Response response : mCate) {
            list.add(response.getCategory_name());
        }
        setSpinner(list);
    }

    @Override
    protected void onDestroy() {
        myLocation.stopUpdates();
        super.onDestroy();
    }

    private void setTempAdapter() {
        list.add(0, "Please Wait...");
        setSpinner(list);

        sublist.add(0, " - None - ");
        setSubSpinner(sublist);
        sendRequest();
    }

    private void setSpinner(List<String> mItems) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        mCategory.setAdapter(adapter);
        mCategory.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                {
                    sCategory = "";
                    sublist = null;
                    sublist =  new ArrayList<String>();
                    sublist.add(" - None - ");
                    setSubSpinner(sublist);
                    return;
                }

                System.out.println(mCate.size() + "," + position + "," + (position - 1));
                sCategory = mCate.get(position - 1).getCategory_id();
                ToastL(sCategory);
                sendSubRequest();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setSubSpinner(List<String> mItems) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        mSubCategory.setAdapter(adapter);
        mSubCategory.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                {
                    sSubCategory = "";
                    return;
                }

                sSubCategory = mSubCate.get(position - 1).getSubcategory_id();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private String getBody(int tokenType) {
        JSONObject mJsonObject = null;
        mJsonObject = new JSONObject();

        try {
            switch (tokenType) {
                case TYPE.CATEGORIES:
                    return null;

                case TYPE.SUB_CATEGORIES:
                    mJsonObject.put(STRING.CATEGORY_ID, sCategory);
                    return mJsonObject.toString();

                case TYPE.ITEMS_CREATE:

                    // {
                    // "category_id": "5316240fe96e1e1ada9f0106",
                    // "title": "test",
                    // "description": "test",
                    // "original_price": "4.95",
                    // "selling_price": "4.95",
                    // "latitude": "4.95",
                    // "longitude": "4.95",
                    // "trade_mode": "0"
                    // }

                    // private EditText mTitle, mOrgPrice, oSellgPrice, mLocation, mDescription;

                    mJsonObject.put(STRING.CATEGORY_ID, sCategory);
                    mJsonObject.put(STRING.SUBCATEGORY_ID, sSubCategory);
                    mJsonObject.put(STRING.TITLE, mTitle.getText().toString());
                    mJsonObject.put(STRING.DESCRIPTION, mDescription.getText().toString());
                    mJsonObject.put(STRING.ORIGINAL_PRICE, mOrgPrice.getText().toString());
                    mJsonObject.put(STRING.SELLING_PRICE, oSellgPrice.getText().toString());
                    ToastL((myLocation == null) ? "Unknown Latitude" : "" + myLocation.getLocation().getLatitude());
                    ToastL((myLocation == null) ? "Unknown Longitude" : "" + myLocation.getLocation().getLongitude());

                    mJsonObject.put(STRING.LATITUDE, (myLocation == null) ? "00" : "" + myLocation.getLocation().getLatitude());
                    mJsonObject.put(STRING.LONGITUDE, (myLocation == null) ? "00" : "" + myLocation.getLocation().getLongitude());

                    mJsonObject.put(STRING.TRADE_MODE, 2);  // TODO - REMOVE HARD CODED VALUE.

                    return mJsonObject.toString();
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void Sales(View v) {
        // setEnabled(1); TODO UnComment Later
        ToastL("Disabled Now");
    }

    public void Exchage(View v) {
        setEnabled(2);
    }

    public void Both(View v) {
        // setEnabled(3); TODO UnComment Later
        ToastL("Disabled Now");
    }

    private void setEnabled(int i) {
        final ImageView im1 = (ImageView) findViewById(R.id.add_items_arrow_s);
        final ImageView im2 = (ImageView) findViewById(R.id.add_items_arrow_e);
        final ImageView im3 = (ImageView) findViewById(R.id.add_items_arrow_b);

        im1.setVisibility(View.INVISIBLE);
        im2.setVisibility(View.INVISIBLE);
        im3.setVisibility(View.INVISIBLE);

        switch (i) {
            case 1:
                im1.setVisibility(View.VISIBLE);
                break;
            case 2:
                im2.setVisibility(View.VISIBLE);
                break;
            case 3:
                im3.setVisibility(View.VISIBLE);
                break;

            default:
                im1.setVisibility(View.VISIBLE);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            final Uri selectedImage = data.getData();
            final String[] filePathColumn = {
                    MediaColumns.DATA
            };
            final Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            final int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

            switch (requestCode) {
                case picReqCode1:
                    this.picturePath1 = cursor.getString(columnIndex);
                    cursor.close();
                    mImg1.setImageBitmap(BitmapFactory.decodeFile(picturePath1));
                    break;

                case picReqCode2:
                    this.picturePath2 = cursor.getString(columnIndex);
                    cursor.close();
                    mImg2.setImageBitmap(BitmapFactory.decodeFile(picturePath2));
                    break;

                case picReqCode3:
                    this.picturePath3 = cursor.getString(columnIndex);
                    cursor.close();
                    mImg3.setImageBitmap(BitmapFactory.decodeFile(picturePath3));
                    break;

                case picReqCode4:
                    this.picturePath4 = cursor.getString(columnIndex);
                    cursor.close();
                    mImg4.setImageBitmap(BitmapFactory.decodeFile(picturePath4));
                    break;

                case picReqCode5:
                    this.picturePath5 = cursor.getString(columnIndex);
                    cursor.close();
                    mImg5.setImageBitmap(BitmapFactory.decodeFile(picturePath5));
                    break;
            }
        }
        else {
            ToastL("Unable to select image...");
        }
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

            /*************** COMMON ***************/
            switch (typ) {
                case TYPE.AUTHORIZE:
                    LogR("DO NOTHING");
                    return;
                case TYPE.ACCESSTOKEN:
                    LogR("LOGOUT HERE");
                    return;
                case TYPE.REFRESH:
                    LogR("RESEND REQUEST HERE");
                    sendRequest();
                    return;
                case TYPE.CATEGORIES:
                    category = SettersNGetters.getCategory();
                    if (category == null)
                    {
                        ToastL("{ Unknown Error }");
                        return;
                    }
                    if (category.getStatus().equalsIgnoreCase(STRING.SUCCESS))
                    {
                        mCate = category.getResponse();

                        list = null;
                        list = new ArrayList<String>();
                        list.add("Select Category");
                        for (Response response : mCate) {
                            list.add(response.getCategory_name());
                        }
                        setSpinner(list);
                    }
                    else {
                        ToastL("{ Error }");
                    }
                    return;

                case TYPE.SUB_CATEGORIES:
                    subCategory = SettersNGetters.getSubCategory();
                    if (subCategory == null)
                    {
                        sublist = null;
                        sublist = new ArrayList<String>();
                        sublist.add(" None ");
                        setSubSpinner(sublist);
                        return;
                    }
                    if (subCategory.getStatus().equalsIgnoreCase(STRING.SUCCESS))
                    {
                        mSubCate = subCategory.getResponse();
                        sublist = null;
                        sublist = new ArrayList<String>();
                        sublist.add("Select Sub-Category");
                        for (vee.HexWhale.LenDen.Parsers.SubCategories.Response response : mSubCate) {
                            sublist.add(response.getSubcategory_name());
                        }
                        setSubSpinner(sublist);
                    }
                    else {
                        sublist = null;
                        sublist = new ArrayList<String>();
                        sublist.add(" None ");
                        setSubSpinner(sublist);
                        return;
                    }
                    return;

                case TYPE.ITEMS_CREATE:
                    addItems = SettersNGetters.getAddItems();
                    if (addItems == null)
                    {
                        ToastL("{ Unknown Error }");
                        return;
                    }
                    if (addItems.getStatus().equalsIgnoreCase(STRING.SUCCESS))
                    {
                        StartImageUploadForItems(addItems.getResponse().getItem_id());
                    }
                    else {
                        ToastL(STRING.ERROR + addItems.getError_message());
                    }
                    break;

            }
            /*************** COMMON ***************/

        }

        private void StartImageUploadForItems(String item_id) {
            new UploadImage().execute(item_id, picturePath1, picturePath2, picturePath3, picturePath4, picturePath5);
        }

        @Override
        public void finishedFetching(int type, String response) {
            LogR("+++ " + response);
        }

        @Override
        public void errorFetching(int type, VolleyError error) {

        }

        @Override
        public void beforeParsing(int type) {

        }

        @Override
        public void ParsingException(Exception e) {
            ToastL("{ Unknown Error }");
        }
    };

    private void ToastL(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    protected void sendRequest() {
        mDataFromUrl.setAccessToken();
        mDataFromUrl.GetString(TYPE.CATEGORIES, getBody(TYPE.CATEGORIES), GetData.getUrl(URL.CATEGORIES));
    }

    protected void sendSubRequest() {
        sublist = null;
        sublist = new ArrayList<String>();
        sublist.add("Please Wait...");
        setSubSpinner(sublist);
        mDataFromUrl.setAccessToken();
        mDataFromUrl.GetString(TYPE.SUB_CATEGORIES, getBody(TYPE.SUB_CATEGORIES), GetData.getUrl(URL.SUB_CATEGORIES));
    }

    public void Finish(View v) {
        finish();
        AnimPrev();
    }

    public void Submit(View v) {

        final int minChars = 3; // 4-1
        // mTitle, mOrgPrice, oSellgPrice, mLocation, mDescription;

        if (mTitle.getText().length() < minChars)
        {
            mTitle.setError("Minimum " + minChars + "Chars");
            return;
        }

        if (sCategory.equalsIgnoreCase(""))
        {
            mCategory.setSelected(true);
            return;
        }
        if (mOrgPrice.getText().length() < 1)
        {
            mOrgPrice.setError("Minimum " + 1 + "Chars");
            return;
        }
        if (oSellgPrice.getText().length() < 1)
        {
            oSellgPrice.setError("Minimum " + 1 + "Chars");
            return;
        }
        if (mLocation.getText().length() < minChars)
        {
            mLocation.setError("Minimum " + minChars + "Chars");
            return;
        }

        if (mDescription.getText().length() < 10)
        {
            mDescription.setError("Minimum " + 10 + "Chars");
            return;
        }

        sendDataToServer();

        // finish();
        // AnimNext();
    }

    private void sendDataToServer() {
        if (picturePath1 == null && picturePath2 == null && picturePath3 == null && picturePath4 == null && picturePath5 == null)
        {
            ToastL("Please Select an Item Image.");
            return;
        }
        mDataFromUrl.setAccessToken();
        mDataFromUrl.GetString(TYPE.ITEMS_CREATE, getBody(TYPE.ITEMS_CREATE), GetData.getUrl(URL.ITEMS_CREATE));
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

    /*******************************************************************/
    /**
     * @param RED
     */
    private void LogR(String msg) {
        if (Constants.enableLog)
        {
            Log.wtf(tag, msg);
        }
    }

    /*******************************************************************/
    public class UploadImage extends AsyncTask<String, Integer, String>
    {
        HttpEntity resEntity;

        @Override
        protected String doInBackground(String... params) {

            final String url = GetData.getUrl(URL.ITEMS_CREATE_UPLOAD);
            final MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
            multipartEntity.setMode(HttpMultipartMode.STRICT); // SET MODE

            multipartEntity.addTextBody(STRING.ITEM_ID, params[0]);

            if (params[1] != null)
            {
                final File mFile1 = new File(params[1]);
                final FileBody mFileBody1 = new FileBody(mFile1);
                multipartEntity.addPart(STRING.PICTURE_1, mFileBody1); // ADD BODY
            }
            if (params[2] != null)
            {
                final File mFile2 = new File(params[2]);
                final FileBody mFileBody2 = new FileBody(mFile2);
                multipartEntity.addPart(STRING.PICTURE_2, mFileBody2); // ADD BODY
            }
            if (params[3] != null)
            {
                final File mFile3 = new File(params[3]);
                final FileBody mFileBody3 = new FileBody(mFile3);
                multipartEntity.addPart(STRING.PICTURE_3, mFileBody3); // ADD BODY
            }
            if (params[4] != null)
            {
                final File mFile4 = new File(params[4]);
                final FileBody mFileBody4 = new FileBody(mFile4);
                multipartEntity.addPart(STRING.PICTURE_4, mFileBody4); // ADD BODY
            }
            if (params[5] != null)
            {
                final File mFile5 = new File(params[5]);
                final FileBody mFileBody5 = new FileBody(mFile5);
                multipartEntity.addPart(STRING.PICTURE_5, mFileBody5); // ADD BODY
            }

            try
            {
                final HttpClient client = new DefaultHttpClient();
                final HttpPost post = new HttpPost(url);

                post.addHeader(HEADERS.ACCESS_TOKEN, mPrefs.getStringInPref(KEY.ACCESS_TOKEN)); // ADD
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

                                    ToastL("Image Loaded Successfully");
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

    @Override
    public void onClick(View v) {
        final Intent mIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        switch (v.getId()) {
            case R.id.add_item_img_1:
                this.startActivityForResult(mIntent, picReqCode1);
                break;

            case R.id.add_item_img_2:
                this.startActivityForResult(mIntent, picReqCode2);
                break;

            case R.id.add_item_img_3:
                this.startActivityForResult(mIntent, picReqCode3);
                break;

            case R.id.add_item_img_4:
                this.startActivityForResult(mIntent, picReqCode4);
                break;

            case R.id.add_item_img_5:
                this.startActivityForResult(mIntent, picReqCode5);
                break;
            default:
                break;
        }

    }

}
