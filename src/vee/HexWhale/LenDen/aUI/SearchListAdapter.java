
package vee.HexWhale.LenDen.aUI;

import static vee.HexWhale.LenDen.Utils.Constants.menuText;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
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
        View sView = convertView;
        if (sView == null) {
            LayoutInflater mInflater = (LayoutInflater) sActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            sView = mInflater.inflate(R.layout.search_list, null);
        }

        final ImageView sImg = (ImageView) sView.findViewById(R.id.search_list_img);
        final TextView sTitle = (TextView) sView.findViewById(R.id.search_list_title);
        final TextView sType = (TextView) sView.findViewById(R.id.search_list_type);
        final TextView sPrice = (TextView) sView.findViewById(R.id.search_list_price);
        final TextView sFav = (TextView) sView.findViewById(R.id.search_list_fav);
        final TextView sCmt = (TextView) sView.findViewById(R.id.search_list_cmt);
        final TextView sStr = (TextView) sView.findViewById(R.id.search_list_str);
        final TextView sBtn = (TextView) sView.findViewById(R.id.search_list_type_btn);

        if (position % 2 == 1) {
            sTitle.setText("Am Loooooooooooooooooooooooooooooooooong title");
            sBtn.setText("SALE");
            sBtn.setBackgroundResource(R.drawable.sales_rnd_bg);
            sPrice.setText("$45");
            sPrice.setTextColor(sView.getResources().getColor(R.color.menu_bg));
        } else {
            sTitle.setText("Am small title");
            sBtn.setText("EXCHANGE");
            sBtn.setBackgroundResource(R.drawable.exch_rnd_bg);
            sPrice.setTextColor(sView.getResources().getColor(R.color.orange));
        }

        sTitle.setSelected(true);

        return sView;
    }
}
