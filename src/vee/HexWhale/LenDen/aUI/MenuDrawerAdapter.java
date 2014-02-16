
package vee.HexWhale.LenDen.aUI;

import static vee.HexWhale.LenDen.Utils.Constants.menuImages;
import static vee.HexWhale.LenDen.Utils.Constants.menuText;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import vee.HexWhale.LenDen.R;

public class MenuDrawerAdapter extends BaseAdapter {

    Activity sActivity;

    public MenuDrawerAdapter(Activity activity) {
        this.sActivity = activity;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return menuImages.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater mInflater = (LayoutInflater) sActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.menu_drawer_list, null);
            holder.mTView = (TextView) convertView.findViewById(R.id.menu_drawer_list_txt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTView.setText(menuText[position]);
        holder.mTView.setCompoundDrawablesWithIntrinsicBounds(0, menuImages[position], 0, 0);

        return convertView;
    }

    private static class ViewHolder {
        TextView mTView;
    }

}