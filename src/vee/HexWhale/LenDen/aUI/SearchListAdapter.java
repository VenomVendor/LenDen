
package vee.HexWhale.LenDen.aUI;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import vee.HexWhale.LenDen.R;

public class SearchListAdapter extends BaseAdapter {

    Activity sActivity;

    public SearchListAdapter(Activity activity) {
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
            LayoutInflater mInflater = (LayoutInflater) sActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.search_list, null);
            holder = new ViewHolder();
          holder.sImg = (ImageView) convertView.findViewById(R.id.search_list_img);
          holder.sTitle = (TextView) convertView.findViewById(R.id.search_list_title);
          holder.sType = (TextView) convertView.findViewById(R.id.search_list_type);
          holder.sPrice = (TextView) convertView.findViewById(R.id.search_list_price);
          holder.sFav = (TextView) convertView.findViewById(R.id.search_list_fav);
          holder.sCmt = (TextView) convertView.findViewById(R.id.search_list_cmt);
          holder.sStr = (TextView) convertView.findViewById(R.id.search_list_str);
          holder.sBtn = (TextView) convertView.findViewById(R.id.search_list_type_btn);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position % 2 == 1) {
            holder.sTitle.setText("Am Loooooooooooooooooooooooooooooooooong title");
            holder.sBtn.setText("SALE");
            holder.sBtn.setBackgroundResource(R.drawable.sales_rnd_bg);
            holder.sPrice.setText("$45");
            holder.sPrice.setTextColor(convertView.getResources().getColor(R.color.menu_bg));
        } else {
            holder.sTitle.setText("Am small title");
            holder.sBtn.setText("EXCHANGE");
            holder.sBtn.setBackgroundResource(R.drawable.exch_rnd_bg);
            holder.sPrice.setTextColor(convertView.getResources().getColor(R.color.orange));
        }

        holder.sTitle.setSelected(true);

        return convertView;
    }

    private static class ViewHolder {
      public ImageView sImg;
      public TextView sTitle;
      public TextView sType;
      public TextView sPrice;
      public TextView sFav;
      public TextView sCmt;
      public TextView sStr;
      public TextView sBtn;
    }
}


