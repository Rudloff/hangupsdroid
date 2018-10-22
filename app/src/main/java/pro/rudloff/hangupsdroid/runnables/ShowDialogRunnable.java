package pro.rudloff.hangupsdroid.runnables;

import android.app.AlertDialog.Builder;

/** Runnable used to display a dialog. */
public class ShowDialogRunnable implements Runnable {

    /** Dialog builder */
    private Builder builder;

    /**
     * ShowDialogRunnable constructor.
     *
     * @param newBuilder Dialog builder
     */
    public ShowDialogRunnable(Builder newBuilder) {
        builder = newBuilder;
    }

    /** Called to run the runnable. */
    public void run() {
        builder.show();
    }
}
