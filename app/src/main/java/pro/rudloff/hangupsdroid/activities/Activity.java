package pro.rudloff.hangupsdroid.activities;

import android.support.v7.app.AppCompatActivity;
import pro.rudloff.hangupsdroid.App;

abstract class Activity extends AppCompatActivity {

    protected void onDestroy() {
        super.onDestroy();

        App app = (App) getApplicationContext();

        if (app.progressDialog != null) {
            app.progressDialog.dismiss();
        }
    }
}
