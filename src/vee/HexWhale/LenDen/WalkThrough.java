
package vee.HexWhale.LenDen;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import vee.HexWhale.LenDen.viewpagerindicator.CirclePageIndicator;
import vee.HexWhale.LenDen.viewpagerindicator.PageIndicator;

public class WalkThrough extends Activity {

    private ViewPager pager;
    PageIndicator mIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.walkthrough);
        pager = (ViewPager) findViewById(R.id.pager);
        InitilizePager(); // XXX 1
    }

    private void InitilizePager() {
        final WalkThroughPager adapter = new WalkThroughPager(this);
        pager.setAdapter(adapter);
        final CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mIndicator = indicator;
        indicator.setViewPager(pager);
    }

    @Override
    public void onBackPressed() {

        this.finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        return;
    }
}
