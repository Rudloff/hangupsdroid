package pro.rudloff.hangupsdroid;

import android.app.ProgressDialog;

import com.chaquo.python.android.PyApplication;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

public class App extends PyApplication {

    public PyObject pythonApp;
    public ProgressDialog progressDialog;

    public void onCreate() {
        super.onCreate();
        Python py = Python.getInstance();
        PyObject hangupsdroid = py.getModule("hangupsdroid");

        pythonApp = hangupsdroid.callAttr("App");
    }
}
