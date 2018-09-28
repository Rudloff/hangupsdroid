package pro.rudloff.hangupsdroid.runnables;

import android.app.Activity;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import java.util.ArrayList;
import pro.rudloff.hangupsdroid.App;
import pro.rudloff.hangupsdroid.Message;
import pro.rudloff.hangupsdroid.User;

public class AddMessageRunnable implements Runnable {

    private Activity activity;
    private MessagesListAdapter<Message> messageAdapter;
    private PyObject messageList;

    public AddMessageRunnable(
            Activity newActivity,
            MessagesListAdapter<Message> newMessageAdapter,
            PyObject newMessageList) {
        activity = newActivity;
        messageAdapter = newMessageAdapter;
        messageList = newMessageList;
    }

    public void run() {
        App app = (App) activity.getApplicationContext();

        Python py = Python.getInstance();
        PyObject builtins = py.getBuiltins();
        PyObject hangupsdroid = py.getModule("hangupsdroid");

        ArrayList<Message> messages = new ArrayList<Message>();
        for (int i = 0; i < builtins.callAttr("len", messageList).toJava(int.class); i++) {
            PyObject message = hangupsdroid.callAttr("getFromArray", messageList, i);
            messages.add(
                    new Message(
                            message,
                            new User(app.pythonApp.callAttr("getUser", message.get("user_id")))));
        }

        messageAdapter.addToEnd(messages, true);
    }
}
