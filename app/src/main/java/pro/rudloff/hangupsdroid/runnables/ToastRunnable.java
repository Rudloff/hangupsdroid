package pro.rudloff.hangupsdroid.runnables;

import android.app.Activity;
import android.widget.Toast;

/** Runnable used to display a toast. */
public class ToastRunnable implements Runnable {

    /** Current activity. */
    private Activity activity;

    /** Message to display. */
    private String message;

    /**
     * ToastRunnable constructor
     *
     * @param newActivity Current activity
     * @param newMessage Message to display
     */
    public ToastRunnable(Activity newActivity, String newMessage) {
        activity = newActivity;
        message = newMessage;
    }

    /** Called to run the runnable. */
    @Override
    public void run() {
        if (!activity.isFinishing()) {
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
        }
    }
}
