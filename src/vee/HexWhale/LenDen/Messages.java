/**
 * ***Copyright(c) : 2014-Present, VenomVendor.***
 * Author : VenomVendor
 * Dated : 18 Feb, 2014 4:25:32 PM
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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import vee.HexWhale.LenDen.Parsers.Messages.GetMessages;
import vee.HexWhale.LenDen.Storage.SettersNGetters;
import vee.HexWhale.LenDen.Utils.Constants.API.STRING;
import vee.HexWhale.LenDen.Utils.Constants.API.TYPE;
import vee.HexWhale.LenDen.Utils.Constants.API.URL;
import vee.HexWhale.LenDen.aUI.Adapters.MessagesAdapter;
import vee.HexWhale.LenDen.bg.Threads.FetcherListener;
import vee.HexWhale.LenDen.bg.Threads.GetData;
import vee.HexWhale.LenDen.bg.Threads.GetDataFromUrl;
import vee.HexWhale.LenDen.bg.Threads.TagGen;

import java.util.Locale;

public class Messages extends FragmentActivity {

    private static int page = 1;
    ListView mListView;
    GetDataFromUrl mDataFromUrl;
    GetMessages mMessages;
    private String tag = "UNKNOWN";
    TextView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tag = TagGen.getTag(getClass());
        this.setContentView(R.layout.messages);
        mView = (TextView) findViewById(R.id.no_item);
        ((TextView) findViewById(R.id.menu_center)).setText(("messages").toUpperCase(Locale.UK));
        ((ImageView) findViewById(R.id.menu_right)).setVisibility(View.INVISIBLE);
        mListView = (ListView) findViewById(android.R.id.list);

        mDataFromUrl = new GetDataFromUrl(this, mFetcherListener);
        mDataFromUrl.setAccessToken();
        mDataFromUrl.GetString(TYPE.MESSSAGES, getBody(TYPE.MESSSAGES), GetData.getUrl(URL.MESSSAGES));



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

                case TYPE.MESSSAGES:

                    mMessages = SettersNGetters.getMessages();
                    if (mMessages == null) {
                        mView.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.GONE);
                        ToastL("{ Unknown Error }");
                        return;
                    }

                    if (mMessages.getResponse().getMessages().size() == 0) {
                        mView.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.GONE);
                        return;
                    }

                    final MessagesAdapter adapter = new MessagesAdapter(Messages.this, mMessages);

                    mView.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    mListView.setAdapter(adapter);
                    mListView.setOnItemClickListener(new OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            final Intent mIntent = new Intent(getApplicationContext(), MessagesFull.class);
                            mIntent.putExtra(STRING.POSITION, position);
                            startActivity(mIntent);
                            AnimNext();

                        }

                    });

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

    private String getBody(final int mType) {
        JSONObject mJsonObject = null;
        mJsonObject = new JSONObject();
        try {
            switch (mType) {
                case TYPE.MESSSAGES:
                    mJsonObject.put(STRING.PAGE, "" + Messages.page);
                    mJsonObject.put(STRING.OFFSET, "" + 10);
                    break;
            }
            return mJsonObject.toString();
        }
        catch (final JSONException e) {
            e.printStackTrace();
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void LogR(String msg) {
        Log.wtf(tag, msg);
    }

    /**
     * @param text
     */
    private void ToastL(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    public void Finish(View v) {
        finish();
        AnimPrev();
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
}
