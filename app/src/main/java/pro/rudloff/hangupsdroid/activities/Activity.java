package pro.rudloff.hangupsdroid.activities;

import android.support.v7.app.AppCompatActivity;
import pro.rudloff.hangupsdroid.App;

/** Abstract class common to all our activities. */
abstract class Activity extends AppCompatActivity {

    /** Called when the activity is destroyed. */
    protected void onDestroy() {
        super.onDestroy();

        App app = (App) getApplicationContext();

        if (app.progressDialog != null) {
            app.progressDialog.dismiss();
        }
    }
}
