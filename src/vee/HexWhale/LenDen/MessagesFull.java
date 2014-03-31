/**
 * ***Copyright(c)  :   2014-Present, VenomVendor.***
 * Author           :   VenomVendor
 * Dated            :   21 Feb, 2014 4:41:51 PM
 * Project          :   LenDen-Android
 * Client           :   LenDen
 * Contact          :   info@VenomVendor.com
 * URL              :   https://www.google.co.in/search?q=VenomVendor
 * Copyright(c)     :   2014-Present, VenomVendor.
 * License          :   This work is licensed under Attribution-NonCommercial 3.0 Unported (CC BY-NC 3.0).
 *                      License info at http://creativecommons.org/licenses/by-nc/3.0/deed.en_US
 *                      Read More at http://creativecommons.org/licenses/by-nc/3.0/legalcode
 **/


package vee.HexWhale.LenDen;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import vee.HexWhale.LenDen.Parsers.Messages.CreateMessage;
import vee.HexWhale.LenDen.Parsers.Messages.GetMessages;
import vee.HexWhale.LenDen.Parsers.Messages.Messages;
import vee.HexWhale.LenDen.Parsers.MessagesFull.GetMessagesFull;
import vee.HexWhale.LenDen.Storage.SettersNGetters;
import vee.HexWhale.LenDen.Utils.Constants.API.STRING;
import vee.HexWhale.LenDen.Utils.Constants.API.TYPE;
import vee.HexWhale.LenDen.Utils.Constants.API.URL;
import vee.HexWhale.LenDen.aUI.Adapters.MessagesFullAdapter;
import vee.HexWhale.LenDen.bg.Threads.FetcherListener;
import vee.HexWhale.LenDen.bg.Threads.GetData;
import vee.HexWhale.LenDen.bg.Threads.GetDataFromUrl;
import vee.HexWhale.LenDen.bg.Threads.NetworkConnection;
import vee.HexWhale.LenDen.bg.Threads.TagGen;

import java.util.List;

public class MessagesFull extends FragmentActivity {

    ListView mListView;
    private String tag = "UNKNOWN";
    int mPosi = 0;
    List<Messages> bulkMessages;
    GetMessages mMessages;
    TextView mView;
    private GetDataFromUrl mDataFromUrl;
    private GetMessagesFull messagesFull;
    String mText;
    EditText send_text;
    private CreateMessage mCreateMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tag = TagGen.getTag(getClass());
        this.setContentView(R.layout.messages_full);
        mListView = (ListView) findViewById(android.R.id.list);
        mView = (TextView) findViewById(R.id.no_item);

        if (!NetworkConnection.isAvail(getApplicationContext())) {
            ToastL("No internet Connection");
            return;
        }

        // bulkMessages = mMessages.getResponse().getMessages();
        //
        // final Intent mIntent = getIntent();
        //
        // if (mIntent != null && bulkMessages != null)
        // {
        // mPosi = mIntent.getIntExtra(STRING.POSITION, 0);
        // }
        // else {
        // mView.setVisibility(View.VISIBLE);
        // mListView.setVisibility(View.GONE);
        // return;
        // }

        mDataFromUrl = new GetDataFromUrl(this, mFetcherListener);
        mDataFromUrl.setAccessToken();

        mDataFromUrl.GetString(TYPE.MESSSAGES_USER, getBody(TYPE.MESSSAGES_USER), GetData.getUrl(URL.MESSSAGES_USER));

        // TODO-REMOVE
        // mDataFromUrl.GetString(TYPE.MESSSAGES_USER, null,
        // "http://dl.dropboxusercontent.com/u/40690073/LenDen-API/gettoken.json");

    }

    public void SendMessage(View view)
    {
        send_text = (EditText) findViewById(R.id.send_text);

        if (send_text.getText().length() > 0)
        {
            mText = send_text.getText().toString().trim();
            if (mText.length() > 0)
            {
                mDataFromUrl.setAccessToken();
                mDataFromUrl.GetString(TYPE.MESSSAGES_CREATE, getBody(TYPE.MESSSAGES_CREATE), GetData.getUrl(URL.MESSSAGES_CREATE));
            }
            else {
                send_text.setError("Enter text");
            }
        }
        else {
            send_text.setError("Enter text");
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

            switch (typ) {

                case TYPE.MESSSAGES_USER:

                    messagesFull = SettersNGetters.getMessagesFull();
                    if (messagesFull == null) {
                        mView.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.GONE);
                        ToastL("{ Unknown Error }");
                        return;
                    }

                    if (messagesFull.getResponse().getMessages_list().size() == 0) {
                        mView.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.GONE);
                        ToastL("{ No Messages }");
                        return;
                    }

                    final MessagesFullAdapter adapter = new MessagesFullAdapter(MessagesFull.this, messagesFull);
                    mListView.setAdapter(adapter);

                    mView.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    mListView.setOnItemClickListener(new OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            ToastL("" + position);

                        }

                    });

                case TYPE.MESSSAGES_CREATE:
                    mCreateMessage = SettersNGetters.getCreateMessage();

                    if (mCreateMessage == null)
                    {
                        ToastL("{ Unknown Error }");
                        return;
                    }
                    if (mCreateMessage.getStatus().equalsIgnoreCase("success"))
                    {
                        ToastL("" + "Posted");
                        send_text.setText("");

                    }
                    else {
                        ToastL("" + "Failed");
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

    private String getBody(final int mType) {
        JSONObject mJsonObject = null;
        mJsonObject = new JSONObject();
        try {
            final Messages individualMessage = bulkMessages.get(mPosi);
            switch (mType) {
                case TYPE.MESSSAGES_USER:

                    mJsonObject.put(STRING.PARTNER_ID, "" + individualMessage.getPartner_id());
                    mJsonObject.put(STRING.ITEM_ID, "" + individualMessage.getItem_id());
                    break;

                case TYPE.MESSSAGES_CREATE:

                    mJsonObject.put(STRING.PARTNER_ID, "" + individualMessage.getPartner_id());
                    mJsonObject.put(STRING.ITEM_ID, "" + individualMessage.getItem_id());
                    mJsonObject.put(STRING.MESSAGE, "" + mText);
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

    /*******************************************************************/
    /**
     * @param RED
     */
    private void LogR(String msg) {
        Log.wtf(tag, msg);
    }

    /**
     * @param text
     */
    private void ToastL(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    /*******************************************************************/

}
