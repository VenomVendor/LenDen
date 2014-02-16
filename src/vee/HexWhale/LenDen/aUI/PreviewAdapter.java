
package vee.HexWhale.LenDen.aUI;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import vee.HexWhale.LenDen.R;

public class PreviewAdapter extends BaseAdapter {

    Activity sActivity;

    public PreviewAdapter(Activity activity) {
        this.sActivity = activity;
    }

    @Override
    public int getCount() {

        return 10;
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
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater mInflater = (LayoutInflater) sActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.preview_list, null);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    private static class ViewHolder {

    }
}
