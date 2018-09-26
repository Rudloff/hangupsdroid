package pro.rudloff.hangupsdroid;

import android.app.Activity;
import android.app.ProgressDialog;

public class ProgressDialogRunnable implements Runnable {

    private Activity activity;
    private String message;

    public ProgressDialogRunnable(Activity newActivity, String newMessage) {
        activity = newActivity;
        message = newMessage;
    }

    public void run() {
        App app = (App) activity.getApplicationContext();
        if (app.progressDialog != null) {
            app.progressDialog.dismiss();
        }
        app.progressDialog = ProgressDialog.show(activity, null, message + "…", true, false);
    }
}
