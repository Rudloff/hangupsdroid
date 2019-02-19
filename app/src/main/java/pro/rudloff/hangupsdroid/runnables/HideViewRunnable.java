package pro.rudloff.hangupsdroid.runnables;

import android.view.View;

/** Runnable used to hide a specific view. */
public class HideViewRunnable implements Runnable {

    /** View to hide. */
    private View view;

    /**
     * HideViewRunnable constructor.
     *
     * @param newView View to hide.
     */
    public HideViewRunnable(View newView) {
        view = newView;
    }

    /** Called to run the runnable. */
    @Override
    public void run() {
        view.animate().alpha(0);
    }
}
