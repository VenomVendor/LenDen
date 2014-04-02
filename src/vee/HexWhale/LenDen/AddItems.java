/**
 * ***Copyright(c)  :   2014-Present, VenomVendor.***
 * Author           :   VenomVendor
 * Dated            :   17 Feb, 2014 3:29:01 AM
 * Project          :   LenDen-Android
 * Client           :   LenDen
 * Contact          :   info@VenomVendor.com
 * URL              :   https://www.google.co.in/search?q=VenomVendor
 * Copyright(c)     :   2014-Present, VenomVendor.
 * License          :   This work is licensed under Attribution-NonCommercial 3.0 Unported
 *                      License info at http://creativecommons.org/licenses/by-nc/3.0/deed.en_US
 *                      Read More at http://creativecommons.org/licenses/by-nc/3.0/legalcode
 **/

package vee.HexWhale.LenDen;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class AddItems extends FragmentActivity {

    View tempView = null;

    final static int tempID = 23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.add_items);
    }

    public void OpenImage(View view) {
        tempView = view;
        final Intent mIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        this.startActivityForResult(mIntent, AddItems.tempID);
    }

    public void Sales(View v) {
        // setEnabled(1); TODO UnComment Later
        ToastL("Disabled Now");
    }

    public void Exchage(View v) {
        setEnabled(2);
    }

    public void Both(View v) {
        // setEnabled(3); TODO UnComment Later
        ToastL("Disabled Now");
    }

    private void setEnabled(int i) {
        final ImageView im1 = (ImageView) findViewById(R.id.add_items_arrow_s);
        final ImageView im2 = (ImageView) findViewById(R.id.add_items_arrow_e);
        final ImageView im3 = (ImageView) findViewById(R.id.add_items_arrow_b);

        im1.setVisibility(View.INVISIBLE);
        im2.setVisibility(View.INVISIBLE);
        im3.setVisibility(View.INVISIBLE);

        switch (i) {
            case 1:
                im1.setVisibility(View.VISIBLE);
                break;
            case 2:
                im2.setVisibility(View.VISIBLE);
                break;
            case 3:
                im3.setVisibility(View.VISIBLE);
                break;

            default:
                im1.setVisibility(View.VISIBLE);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AddItems.tempID && resultCode == Activity.RESULT_OK && data != null) {
            final Uri selectedImage = data.getData();
            final String[] filePathColumn = {
                    MediaColumns.DATA
            };
            final Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            final int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            final String picturePath = cursor.getString(columnIndex);
            cursor.close();
            ((ImageView) tempView).setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }
        else {
            ToastL("Unable to select image...");
        }
    }

    private void ToastL(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    public void Finish(View v) {
        finish();
        AnimPrev();
    }

    public void Submit(View v) {
        finish();
        AnimNext();
    }

    @Override
    public void onBackPressed() {
        finish();
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
