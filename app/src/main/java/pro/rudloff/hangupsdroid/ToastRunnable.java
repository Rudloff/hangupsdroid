package pro.rudloff.hangupsdroid;

import android.app.Activity;
import android.widget.Toast;

public class ToastRunnable implements Runnable {

    private Activity activity;
    private String message;

    public ToastRunnable(Activity newActivity, String newMessage) {
        activity = newActivity;
        message = newMessage;
    }

    public void run() {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
    }
}
