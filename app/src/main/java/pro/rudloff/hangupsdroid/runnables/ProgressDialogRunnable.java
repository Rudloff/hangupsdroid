package pro.rudloff.hangupsdroid.runnables;

import android.app.Activity;
import android.app.ProgressDialog;
import pro.rudloff.hangupsdroid.App;

/** Runnabled used to display a progress dialog. */
public class ProgressDialogRunnable implements Runnable {

    /** Current activity. */
    private Activity activity;

    /** Message to display. */
    private String message;

    /**
     * ProgressDialogRunnable constructor.
     *
     * @param newActivity Current activity
     * @param newMessage Message to display
     */
    public ProgressDialogRunnable(Activity newActivity, String newMessage) {
        activity = newActivity;
        message = newMessage;
    }

    /** Called to run the runnable. */
    public void run() {
        App app = (App) activity.getApplicationContext();
        if (app.progressDialog != null) {
            app.progressDialog.dismiss();
        }
        app.progressDialog = ProgressDialog.show(activity, null, message + "…", true, false);
    }
}
