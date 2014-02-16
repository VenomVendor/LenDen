
package vee.HexWhale.LenDen;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import vee.HexWhale.LenDen.aUI.MenuBar;
import vee.HexWhale.LenDen.aUI.ProfileListAdapter;

import java.util.Locale;

public class Profile extends MenuBar {

    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        mListView = (ListView) findViewById(android.R.id.list);
        ProfileListAdapter adapter = new ProfileListAdapter(this);
        mListView.setAdapter(adapter);
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mImgLeft = (ImageView) findViewById(R.id.menu_left);
        mTextCenter = (TextView) findViewById(R.id.menu_center);

        mImgLeft.setImageResource(R.drawable.add_items_back);
        mTextCenter.setText(("Profile").toUpperCase(Locale.UK));

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

    public void FlipView(View v) {
        this.finish();
        AnimPrev();

    }

    private void AnimNext() {
        overridePendingTransition(R.anim.enter, R.anim.exit);
        return;
    }

}