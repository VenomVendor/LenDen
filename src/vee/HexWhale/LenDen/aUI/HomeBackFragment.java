
package vee.HexWhale.LenDen.aUI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import vee.HexWhale.LenDen.Preview;
import vee.HexWhale.LenDen.R;

/**
 * A fragment representing the front of the card.
 */
public class HomeBackFragment extends Fragment {
    Activity sActivity;
    GridView mGridView;

    public HomeBackFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.sActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_back, container, false);
        mGridView = (GridView) rootView.findViewById(R.id.home_grid);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        HomeGridAdapter adapter = new HomeGridAdapter(sActivity);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View parent, int position, long id) {

                Intent i = new Intent(getActivity(), Preview.class);
                startActivity(i);
                AnimNext();

            }

        });
    }

    protected void AnimNext() {
        getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
        return;
    }

}