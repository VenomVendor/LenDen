package vee.HexWhale.LenDen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Splash4 extends Activity {

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		final RelativeLayout mRR = (RelativeLayout) findViewById(R.id.settings);
		final TextView mTextView = (TextView) findViewById(R.id.textView1);
		mRR.setBackgroundResource(R.drawable.splash2);
		int width = getWindowManager().getDefaultDisplay().getWidth();
		int height = getWindowManager().getDefaultDisplay().getHeight();
		mTextView.setText("FullScreen 4");

	}

	public void NextActivity(View v) {
		startActivity((new Intent(getApplicationContext(), Splash5.class)));
	}
}
