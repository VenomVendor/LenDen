/**
 * ***Copyright(c)  :   2014-Present, VenomVendor.***
 * Author           :   VenomVendor
 * Dated            :   17 Feb, 2014 3:28:57 AM
 * Project          :   LenDen-Android
 * Client           :   LenDen
 * Contact          :   info@VenomVendor.com
 * URL              :   https://www.google.co.in/search?q=VenomVendor
 * Copyright(c)     :   2014-Present, VenomVendor.
 * License          :   This work is licensed under Attribution-NonCommercial 3.0 Unported (CC BY-NC 3.0).
 *                      License info at http://creativecommons.org/licenses/by-nc/3.0/deed.en_US
 *                      Read More at http://creativecommons.org/licenses/by-nc/3.0/legalcode
 **/

package vee.HexWhale.LenDen;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import vee.HexWhale.LenDen.aUI.MenuBar;
import vee.HexWhale.LenDen.aUI.Fragments.HomeBackFragment;
import vee.HexWhale.LenDen.aUI.Fragments.HomeFrontFragment;

import java.util.List;

public class Home extends MenuBar implements FragmentManager.OnBackStackChangedListener {
    /**
     * A handler object, used for deferring UI operations.
     */
    private final Handler mHandler = new Handler();

    /**
     * Whether or not we're showing the back of the card (otherwise showing the
     * front).
     */
    private boolean mShowingBack = false;

    View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.home);
        mView = findViewById(R.id.menuinc);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new HomeFrontFragment()).commit();
        }
        else {
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

        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.android_slide_in_left, R.anim.android_slide_out_right);
        ft.replace(R.id.container, new HomeBackFragment());
        ft.addToBackStack(null);
        ft.commit();

        // ft.setCustomAnimations(
        // R.animator.fragment_slide_left_enter,
        // R.animator.fragment_slide_left_exit,
        // R.animator.fragment_slide_right_enter,
        // R.animator.fragment_slide_right_exit);

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Home.this.supportInvalidateOptionsMenu();
            }
        });
    }

    @Override
    public void onBackStackChanged() {
        mShowingBack = (getSupportFragmentManager().getBackStackEntryCount() > 0);
        supportInvalidateOptionsMenu();
    }

    public void HideTop() {
        mView.setVisibility(View.GONE);
    }

    public void ShowTop() {
        mView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        final ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final List<RunningTaskInfo> tasks = am.getRunningTasks(3); // 3 because
        // we have to
        // give it
        // something. This is an arbitrary
        // number
        final int activityCount = tasks.get(0).numActivities;
        System.out.println("activityCount " + activityCount);

        if (activityCount < 2) {

            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
            alertDialog.setMessage("Would you like to?");
            alertDialog.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Home.this.finish();
                }
            });
            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    return;
                }
            });

            alertDialog.show();

        }
    }

}
