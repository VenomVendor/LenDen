
package vee.HexWhale.LenDen.aUI;

import static vee.HexWhale.LenDen.Utils.Constants.menuclass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.agimind.widget.SlideHolder;

import vee.HexWhale.LenDen.R;

public class MenuBar extends FragmentActivity {

    RelativeLayout mRelLayTop;
    ImageView mImgRight;
    protected ImageView mImgLeft;
    protected TextView mTextCenter;
    private SlideHolder mSlideHolder;
    ListView mDrawerList;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initiateMenu();
    }

    private void initiateMenu() {
        mSlideHolder = (SlideHolder) findViewById(R.id.slideHolder);
        mRelLayTop = (RelativeLayout) findViewById(R.id.menu_lyt);
        mImgRight = (ImageView) findViewById(R.id.menu_right);
        mImgLeft = (ImageView) findViewById(R.id.menu_left);
        mTextCenter = (TextView) findViewById(R.id.menu_center);
        mDrawerList = (ListView) findViewById(R.id.menu_drawer);
        mSlideHolder.setDirection(SlideHolder.DIRECTION_RIGHT);
        mSlideHolder.setAllowInterceptTouch(false);

        mDrawerList.setAdapter(new MenuDrawerAdapter(this));

        mDrawerList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mSlideHolder.toggle();
                startActivity(new Intent(getApplicationContext(), menuclass[position]));
                AnimPrev();
            }
        });
    }


    @Override
    public void onBackPressed() {
        this.finish();
        AnimPrev();
    }

    private void AnimPrev() {
        overridePendingTransition(R.anim.android_slide_in_left, R.anim.android_slide_out_right);
        return;
    }

    private void AnimNext() {
        overridePendingTransition(R.anim.enter, R.anim.exit);
        return;
    }
    public void ToggleMenu(View v) {
        mSlideHolder.toggle();
    }

}
