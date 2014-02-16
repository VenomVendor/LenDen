
package vee.HexWhale.LenDen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends Activity {

    final long time = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Handler mHandler = new Handler();

        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), WalkThrough.class));
                finish();
                // overridePendingTransition(R.anim.enter, R.anim.exit);
                overridePendingTransition(R.anim.enter, R.anim.exit);

                // overridePendingTransition(R.anim.android_slide_in_right,
                // R.anim.android_slide_out_left);
            }
        }, time);

    }

}
