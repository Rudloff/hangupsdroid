package pro.rudloff.hangupsdroid;

import android.app.ProgressDialog;
import android.content.Context;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.PyApplication;
import org.acra.ACRA;
import org.acra.annotation.AcraCore;
import org.acra.annotation.AcraDialog;
import org.acra.annotation.AcraMailSender;
import org.acra.data.StringFormat;

/** Main application object. */
@AcraCore(reportFormat = StringFormat.KEY_VALUE_LIST)
@AcraDialog(resText = R.string.acra_dialog_text)
@AcraMailSender(mailTo = "contact@rudloff.pro")
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

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        ACRA.init(this);
    }
}
