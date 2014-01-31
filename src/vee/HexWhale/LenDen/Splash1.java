package vee.HexWhale.LenDen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Splash1 extends Activity {

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		final RelativeLayout mRR = (RelativeLayout) findViewById(R.id.settings);
		final TextView mTextView = (TextView) findViewById(R.id.textView1);
		mRR.setBackgroundResource(R.drawable.splash1);
		int width = getWindowManager().getDefaultDisplay().getWidth();
		int height = getWindowManager().getDefaultDisplay().getHeight();
		mTextView.setText("1 ");

	}

	public void NextActivity(View v) {
		startActivity((new Intent(getApplicationContext(), Splash2.class)));
	}
}
