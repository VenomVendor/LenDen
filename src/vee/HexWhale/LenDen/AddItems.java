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
import android.os.Bundle;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import vee.HexWhale.LenDen.Parsers.Categories.GetCategory;
import vee.HexWhale.LenDen.Parsers.Categories.Response;
import vee.HexWhale.LenDen.Parsers.SubCategories.GetSubCategory;
import vee.HexWhale.LenDen.Storage.SettersNGetters;
import vee.HexWhale.LenDen.Utils.Constants;
import vee.HexWhale.LenDen.Utils.Constants.API.STRING;
import vee.HexWhale.LenDen.Utils.Constants.API.TYPE;
import vee.HexWhale.LenDen.Utils.Constants.API.URL;
import vee.HexWhale.LenDen.bg.Threads.FetcherListener;
import vee.HexWhale.LenDen.bg.Threads.GetData;
import vee.HexWhale.LenDen.bg.Threads.GetDataFromUrl;
import vee.HexWhale.LenDen.bg.Threads.LocationFinder;
import vee.HexWhale.LenDen.bg.Threads.TagGen;
import vee.HexWhale.LenDen.bg.Threads.Validator;

import java.util.ArrayList;
import java.util.List;

public class AddItems extends FragmentActivity {

    View tempView = null;
    private String tag = TagGen.getTag(this.getClass());
    final static int tempID = 23;
    private GetDataFromUrl mDataFromUrl;
    private GetCategory category;
    private GetSubCategory subCategory;
    private EditText mTitle, mOrgPrice, oSellgPrice, mLocation, mDescription;

    private Spinner mCategory, mSubCategory;
    private String sCategory = "", sSubCategory = "";
    List<Response> mCate;
    List<vee.HexWhale.LenDen.Parsers.SubCategories.Response> mSubCate;

    final List<String> list = new ArrayList<String>();
    List<String> sublist = new ArrayList<String>();

    LocationFinder myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myLocation = new LocationFinder(getApplicationContext());
        mDataFromUrl = new GetDataFromUrl(this, mFetcherListener);
        this.setContentView(R.layout.add_items);
        mTitle = (EditText) findViewById(R.id.add_item_title);
        mOrgPrice = (EditText) findViewById(R.id.add_item_price_org);
        oSellgPrice = (EditText) findViewById(R.id.add_item_price_sellg);
        mLocation = (EditText) findViewById(R.id.add_item_location);
        mDescription = (EditText) findViewById(R.id.add_item_description);
        mCategory = (Spinner) findViewById(R.id.add_item_cate);
        mSubCategory = (Spinner) findViewById(R.id.add_item_sub_cate);

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
                    return;
                }

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

                    mJsonObject.put(STRING.TRADE_MODE, 2);

                    return mJsonObject.toString();
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void OpenImage(View view) {
        tempView = view;
        final Intent mIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        this.startActivityForResult(mIntent, AddItems.tempID);
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
        if (requestCode == AddItems.tempID && resultCode == Activity.RESULT_OK && data != null) {
            final Uri selectedImage = data.getData();
            final String[] filePathColumn = {
                    MediaColumns.DATA
            };
            final Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            final int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            final String picturePath = cursor.getString(columnIndex);
            cursor.close();
            ((ImageView) tempView).setImageBitmap(BitmapFactory.decodeFile(picturePath));
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
            // TODO Auto-generated method stub

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

                        list.add(0, "Select Category");
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
            }
            /*************** COMMON ***************/

        }

        @Override
        public void finishedFetching(int type, String response) {
            LogR("+++ " + response);
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

        final int minChars = 3;
        // mTitle, mOrgPrice, oSellgPrice, mLocation, mDescription;

        if (!Validator.hasMinChars(mTitle, minChars).equalsIgnoreCase("k"))
        {
            mTitle.setError(Validator.hasMinChars(mTitle, minChars));
            return;
        }

        if (sCategory.equalsIgnoreCase(""))
        {
            mCategory.setSelected(true);
            return;
        }

        if (!Validator.hasMinChars(mOrgPrice, minChars).equalsIgnoreCase("k"))
        {
            mOrgPrice.setError(Validator.hasMinChars(mOrgPrice, minChars));
            return;
        }

        if (!Validator.hasMinChars(oSellgPrice, minChars).equalsIgnoreCase("k"))
        {
            oSellgPrice.setError(Validator.hasMinChars(oSellgPrice, minChars));
            return;
        }

        if (!Validator.hasMinChars(mLocation, minChars).equalsIgnoreCase("k"))
        {
            mLocation.setError(Validator.hasMinChars(mLocation, minChars));
            return;
        }

        if (!Validator.hasMinChars(mDescription, minChars).equalsIgnoreCase("k"))
        {
            mDescription.setError(Validator.hasMinChars(mDescription, minChars));
            return;
        }

        sendDataToServer();

        // finish();
        // AnimNext();
    }

    private void sendDataToServer() {
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
}
