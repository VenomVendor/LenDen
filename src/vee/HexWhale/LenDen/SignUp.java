
package vee.HexWhale.LenDen;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;

public class SignUp extends FragmentActivity {

    EditText mName, mUName, mMail, mPsw, mRePsw;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        setContentView(R.layout.signup);

        mName = (EditText) findViewById(R.id.signup_name);
        mUName = (EditText) findViewById(R.id.signup_uname);
        mMail = (EditText) findViewById(R.id.signup_email);
        mPsw = (EditText) findViewById(R.id.signup_psw);
        mRePsw = (EditText) findViewById(R.id.signup_repsw);

    }

    public void Finish(View v) {
        finish();
        AnimNext();
    }

    public void Validate(View v) {
        finish();
        AnimNext();
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
