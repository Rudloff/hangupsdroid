package pro.rudloff.hangupsdroid.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.chaquo.python.PyObject;
import pro.rudloff.hangupsdroid.App;
import pro.rudloff.hangupsdroid.R;
import pro.rudloff.hangupsdroid.runnables.HideViewRunnable;
import pro.rudloff.hangupsdroid.runnables.ToastRunnable;

/** Abstract class common to all our activities. */
abstract class Activity extends AppCompatActivity {

    /** Called when the activity is destroyed. */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        App app = (App) getApplicationContext();

        if (app.progressDialog != null) {
            app.progressDialog.dismiss();
        }
    }

    /** Called when Hangups returns an error. */
    protected void onPythonError(PyObject error, Boolean reconnect) {
        App app = (App) getApplicationContext();
        View view = findViewById(R.id.conversationLoader);

        if (view != null) {
            runOnUiThread(new HideViewRunnable(view));
        }
        if (app.progressDialog != null) {
            app.progressDialog.dismiss();
        }
        runOnUiThread(new ToastRunnable(this, error.toString()));

        if (reconnect) {
            // Force reconnect.
            app.pythonApp.put("client", null);

            Intent intent = new Intent(this, LoginActivity.class);
            finishAffinity();
            startActivity(intent);
        }
    }
}
