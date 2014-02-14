
package vee.HexWhale.LenDen.aUI;

import static vee.HexWhale.LenDen.Utils.Constants.menuText;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import vee.HexWhale.LenDen.R;

public class SearchListAdapter extends BaseAdapter {

    Activity sActivity;

    public SearchListAdapter(Activity activity) {
        this.sActivity = activity;
    }

    @Override
    public int getCount() {

        return menuText.length;
    }

    @Override
    public Object getItem(int position) {

        return position;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View sView = convertView;
        if (sView == null) {
            LayoutInflater mInflater = (LayoutInflater) sActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            sView = mInflater.inflate(R.layout.search_list, null);
        }
        return sView;
    }
}
