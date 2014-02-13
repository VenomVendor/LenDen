
package vee.HexWhale.LenDen;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import vee.HexWhale.LenDen.aUI.HomeBackFragment;
import vee.HexWhale.LenDen.aUI.HomeFrontFragment;
import vee.HexWhale.LenDen.aUI.MenuBar;

public class Home extends MenuBar implements FragmentManager.OnBackStackChangedListener {
    /**
     * A handler object, used for deferring UI operations.
     */
    private Handler mHandler = new Handler();

    /**
     * Whether or not we're showing the back of the card (otherwise showing the
     * front).
     */
    private boolean mShowingBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new HomeFrontFragment()).commit();
        } else {
            mShowingBack = (getSupportFragmentManager().getBackStackEntryCount() > 0);
        }

        getSupportFragmentManager().addOnBackStackChangedListener(this);
    }

    public void FlipView(View v) {
        flipCard();

    }

    private void flipCard() {
        if (mShowingBack) {
            getSupportFragmentManager().popBackStack();
            return;
        }
        mShowingBack = true;

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction().addToBackStack(null);

        tx.replace(R.id.container, new HomeBackFragment())
        // .setCustomAnimations(R.anim.flip_left_in, R.anim.flip_left_out, R.anim.flip_right_in,
        // R.anim.flip_right_out)
                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit).commit();

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                supportInvalidateOptionsMenu();
            }
        });
    }

    @Override
    public void onBackStackChanged() {
        mShowingBack = (getSupportFragmentManager().getBackStackEntryCount() > 0);
        supportInvalidateOptionsMenu();
    }
}
