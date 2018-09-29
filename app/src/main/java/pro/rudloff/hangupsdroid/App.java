package pro.rudloff.hangupsdroid;

import android.app.ProgressDialog;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.PyApplication;

/** Main application object. */
public class App extends PyApplication {

    /** Python hangupsdroid.App instance. */
    public PyObject pythonApp;

    /** Current progress dialog (if any). */
    public ProgressDialog progressDialog;

    /** Called when the app is started. */
    public void onCreate() {
        super.onCreate();
        Python py = Python.getInstance();
        PyObject hangupsdroid = py.getModule("hangupsdroid");

        pythonApp = hangupsdroid.callAttr("App");
    }
}
