
package vee.HexWhale.LenDen.aUI;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import vee.HexWhale.LenDen.R;

public class FavoritesAdapter extends BaseAdapter {

    Activity sActivity;

    public FavoritesAdapter(Activity activity) {
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
            convertView = mInflater.inflate(R.layout.favorites_list, null);

            holder.type = (TextView) convertView.findViewById(R.id.search_list_type_btn);
            holder.price = (TextView) convertView.findViewById(R.id.search_list_price);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.price.setText("$" + (23 + position));
        holder.type.setText("Remove");
        holder.type.setBackgroundResource(R.drawable.remv_rnd_bg);
        return convertView;
    }

    private static class ViewHolder {
        TextView type;
        TextView price;
    }
}
