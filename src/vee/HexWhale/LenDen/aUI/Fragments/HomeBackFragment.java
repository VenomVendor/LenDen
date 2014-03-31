/**
 * ***Copyright(c)  :   2014-Present, VenomVendor.***
 * Author           :   VenomVendor
 * Dated            :   17 Feb, 2014 3:28:57 AM
 * Project          :   LenDen-Android
 * Client           :   LenDen
 * Contact          :   info@VenomVendor.com
 * URL              :   https://www.google.co.in/search?q=VenomVendor
 * Copyright(c)     :   2014-Present, VenomVendor.
 * License          :   This work is licensed under Attribution-NonCommercial 3.0 Unported (CC BY-NC 3.0).
 *                      License info at http://creativecommons.org/licenses/by-nc/3.0/deed.en_US
 *                      Read More at http://creativecommons.org/licenses/by-nc/3.0/legalcode
 **/


package vee.HexWhale.LenDen.aUI.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.haarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

import vee.HexWhale.LenDen.Preview;
import vee.HexWhale.LenDen.R;
import vee.HexWhale.LenDen.Parsers.Categories.Response;
import vee.HexWhale.LenDen.Storage.SettersNGetters;
import vee.HexWhale.LenDen.Utils.Constants.API.STRING;
import vee.HexWhale.LenDen.Utils.Constants.API.TYPE;
import vee.HexWhale.LenDen.Utils.Constants.API.URL;
import vee.HexWhale.LenDen.aUI.Adapters.HomeGridAdapter;
import vee.HexWhale.LenDen.bg.Threads.FetcherListener;
import vee.HexWhale.LenDen.bg.Threads.GetData;
import vee.HexWhale.LenDen.bg.Threads.GetDataFromUrl;
import vee.HexWhale.LenDen.bg.Threads.TagGen;

import java.util.List;

/**
 * A fragment representing the front of the card.
 */
public class HomeBackFragment extends Fragment {
    Activity sActivity;
    GridView mGridView;
    GetDataFromUrl mDataFromUrl;
    private String tag = "UNKNOWN";

    List<Response> resposne = SettersNGetters.getCategory().getResponse();

    public HomeBackFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        sActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.home_back, container, false);
        mGridView = (GridView) rootView.findViewById(R.id.home_grid);
        tag = TagGen.getTag(this.getClass());
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDataFromUrl = new GetDataFromUrl(sActivity, mFetcherListener);
        sendRequest();

    }

    private void sendRequest() {

        mDataFromUrl.setAccessToken();
        mDataFromUrl.GetString(TYPE.CATEGORIES, getBody(TYPE.CATEGORIES), GetData.getUrl(URL.CATEGORIES));

    }

    private String getBody(int tokenType) {
        return null;
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
            }
            /*************** COMMON ***************/

            if (SettersNGetters.getCategory().getStatus().equalsIgnoreCase(STRING.SUCCESS))
            {
                final HomeGridAdapter adapter = new HomeGridAdapter(sActivity, resposne);
                final SwingBottomInAnimationAdapter mScaleInAnimationAdapter = new SwingBottomInAnimationAdapter(adapter, 110, 400);
                mScaleInAnimationAdapter.setAbsListView(mGridView);
                mGridView.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapter, View parent, int position, long id) {

                        final Intent i = new Intent(HomeBackFragment.this.getActivity(), Preview.class);

                        i.putExtra("cate_id", resposne.get(position).getCategory_id());
                        HomeBackFragment.this.startActivity(i);
                        HomeBackFragment.this.AnimNext();

                    }

                });
                mGridView.setAdapter(mScaleInAnimationAdapter);
            }
            else {
                ToastL("{ Error }");
            }
        }

        @Override
        public void finishedFetching(int type, String response) {
            HomeBackFragment.this.LogR("+++ " + response);

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

    protected void AnimNext() {
        getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
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
        Toast.makeText(sActivity, text, Toast.LENGTH_LONG).show();
    }
    /*******************************************************************/

}
