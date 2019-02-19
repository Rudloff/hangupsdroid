package pro.rudloff.hangupsdroid.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import com.chaquo.python.PyException;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import java.lang.ref.WeakReference;
import pro.rudloff.hangupsdroid.App;
import pro.rudloff.hangupsdroid.runnables.ToastRunnable;

/** Task used to run a Python loop asynchronously. */
public class PythonTask extends AsyncTask<PyObject, Integer, Boolean> {

    /**
     * Reference to the curent activity.
     *
     * <p>We use a weak reference so the activity can be garbage collected.
     */
    private WeakReference<Activity> activityReference;

    /** Should we catch Python exceptions? */
    private boolean catchExceptions = false;

    /**
     * PythonTask constructor.
     *
     * @param activity Current activity
     * @param newCatchExceptions Should we catch Python exceptions?
     */
    public PythonTask(Activity activity, boolean newCatchExceptions) {
        activityReference = new WeakReference<Activity>(activity);
        catchExceptions = newCatchExceptions;
    }

    /**
     * Run tasks asynchronously.
     *
     * @param tasks Python coroutines to execute
     * @return Did the tasks finish correctly?
     */
    @Override
    protected Boolean doInBackground(PyObject... tasks) {
        Python py = Python.getInstance();
        PyObject asyncio = py.getModule("asyncio");

        PyObject loop = asyncio.callAttr("new_event_loop");
        for (PyObject task : tasks) {
            try {
                loop.callAttr("run_until_complete", task);
            } catch (PyException error) {
                if (catchExceptions) {
                    Activity activity = activityReference.get();
                    App app = (App) activity.getApplicationContext();

                    Log.e("hangupsdroid", error.getMessage());
                    if (activity != null) {
                        app.progressDialog.dismiss();
                        activity.runOnUiThread(new ToastRunnable(activity, error.getMessage()));
                    }
                } else {
                    throw error;
                }

                return false;
            }
        }
        loop.callAttr("close");

        return true;
    }
}
