
package vee.HexWhale.LenDen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class AddItems extends FragmentActivity {
    private static int RESULT_LOAD_IMAGE = 1;

    ListView mListView;

    static View tempView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_items);
        mListView = (ListView) findViewById(android.R.id.list);

        AddItemsAdapter adapter = new AddItemsAdapter(this);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tempView = view;
                Toast.makeText(getApplicationContext(), view.getTag() + "", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {
                MediaColumns.DATA
            };
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);

            cursor.close();

            ((ImageView) tempView).setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    public void Finish(View v) {
        finish();
    }

    public void Submit(View v) {
        finish();
    }

    public class AddItemsAdapter extends BaseAdapter implements cool {
        Activity sActivity;

        public AddItemsAdapter(Activity activity) {
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
                sView = mInflater.inflate(R.layout.add_items_list, null);
            }

            final ImageView mImageView = (ImageView) sView.findViewById(R.id.add_items_list_img);
            mImageView.setTag("psot" + position);

            return sView;
        }

        @Override
        public void triggered(String picturePath) {
            // TODO Auto-generated method stub

        }

    }

    public interface cool {
        public void triggered(String picturePath);
    }

}
