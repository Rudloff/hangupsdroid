package pro.rudloff.hangupsdroid.runnables;

import android.app.Activity;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import java.util.ArrayList;
import pro.rudloff.hangupsdroid.App;
import pro.rudloff.hangupsdroid.Message;
import pro.rudloff.hangupsdroid.R;
import pro.rudloff.hangupsdroid.User;

/** Runnable used to add new messages to the view. */
public class AddMessageRunnable implements Runnable {

    /** Current activity. */
    private Activity activity;

    /** ChatKit adapter used to inject the messages in the view. */
    private MessagesListAdapter<Message> messageAdapter;

    /** Python list of hangups ConversationEvent objects. */
    private PyObject messageList;

    /** hangups ConversationEvent object. */
    private Message message;

    /**
     * Constructor when add a list of messages.
     *
     * @param newActivity Current activity
     * @param newMessageAdapter ChatKit adapter to use
     * @param newMessageList Python list of hangups ConversationEvent objects to add
     */
    public AddMessageRunnable(
            Activity newActivity,
            MessagesListAdapter<Message> newMessageAdapter,
            PyObject newMessageList) {
        activity = newActivity;
        messageAdapter = newMessageAdapter;
        messageList = newMessageList;
    }

    /**
     * Consutructor when adding a single message.
     *
     * @param newActivity Current activity
     * @param newMessageAdapter ChatKit adapter to use
     * @param newMessage hangups ConversationEvent to add
     */
    public AddMessageRunnable(
            Activity newActivity,
            MessagesListAdapter<Message> newMessageAdapter,
            Message newMessage) {
        activity = newActivity;
        messageAdapter = newMessageAdapter;
        message = newMessage;
    }

    /** Called to run the runnable. */
    public void run() {
        App app = (App) activity.getApplicationContext();

        Python py = Python.getInstance();
        PyObject builtins = py.getBuiltins();
        PyObject hangupsdroid = py.getModule("hangupsdroid");

        if (message != null) {
            boolean isBottom = false;
            MessagesList messagesListView = activity.findViewById(R.id.messagesList);
            if (!messagesListView.canScrollVertically(1)) {
                isBottom = true;
            }
            messageAdapter.addToStart(message, isBottom);
        } else if (messageList != null) {
            ArrayList<Message> messages = new ArrayList<Message>();
            for (int i = 0; i < builtins.callAttr("len", messageList).toJava(int.class); i++) {
                PyObject message = hangupsdroid.callAttr("get_from_array", messageList, i);
                messages.add(
                        new Message(
                                message,
                                new User(
                                        app.pythonApp.callAttr(
                                                "get_user", message.get("user_id")))));
            }

            messageAdapter.addToEnd(messages, true);
        }
    }
}
