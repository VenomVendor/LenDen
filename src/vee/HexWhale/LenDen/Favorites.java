
package vee.HexWhale.LenDen;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import vee.HexWhale.LenDen.aUI.FavoritesAdapter;
import vee.HexWhale.LenDen.aUI.MenuBar;

import java.util.Locale;

public class Favorites extends MenuBar {

    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites);
        mListView = (ListView) findViewById(android.R.id.list);
        FavoritesAdapter adapter = new FavoritesAdapter(this);
        mListView.setAdapter(adapter);
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mImgLeft = (ImageView) findViewById(R.id.menu_left);
        mTextCenter = (TextView) findViewById(R.id.menu_center);

        mImgLeft.setVisibility(View.INVISIBLE);
        mTextCenter.setText(("Favorites").toUpperCase(Locale.UK));

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

}
