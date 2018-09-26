package pro.rudloff.hangupsdroid;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.chaquo.python.PyException;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

import java.lang.ref.WeakReference;

public class PythonTask extends AsyncTask<PyObject, Integer, Boolean> {

    // Use a weak reference so the activity can be garbage collected.
    private WeakReference<Activity> activityReference;

    public PythonTask(Activity activity) {
        activityReference = new WeakReference(activity);
    }

    protected Boolean doInBackground(PyObject... tasks) {
        Python py = Python.getInstance();
        PyObject asyncio = py.getModule("asyncio");

        PyObject loop = asyncio.callAttr("new_event_loop");
        for (PyObject task : tasks) {
            try {
                loop.callAttr("run_until_complete", task);
            } catch (PyException error) {
                Activity activity = activityReference.get();
                if (activity == null) {
                    Log.e("hangupsdroid", error.getMessage());
                } else {
                    App app = (App) activity.getApplicationContext();

                    app.progressDialog.dismiss();
                    activity.runOnUiThread(new ToastRunnable(activity, error.getMessage()));
                }
            }
            loop.callAttr("close");
        }

        return true;
    }
}
