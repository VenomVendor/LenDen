
package vee.HexWhale.LenDen;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WalkThroughPager extends PagerAdapter {

    Activity sActivity;

    public WalkThroughPager(Activity act) {
        this.sActivity = act;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 5;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        // TODO Auto-generated method stub
        return view == (object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final LayoutInflater inflater = (LayoutInflater) sActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View v = inflater.inflate(R.layout.walkthrough_pager, null);

        final TextView sTitle = (TextView) v.findViewById(R.id.wt_pager_title);
        final TextView sText = (TextView) v.findViewById(R.id.wt_pager_text);

        sTitle.setText("" + position);
        sText.setText("" + position);

        ((ViewPager) container).addView(v, 0);

        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        ((ViewPager) container).removeView((View) view);
    }

}
