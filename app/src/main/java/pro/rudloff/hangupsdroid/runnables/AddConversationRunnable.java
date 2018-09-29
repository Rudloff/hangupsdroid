package pro.rudloff.hangupsdroid.runnables;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import java.util.ArrayList;
import pro.rudloff.hangupsdroid.Conversation;

/** Runnable used to add new conversations to the view. */
public class AddConversationRunnable implements Runnable {

    /** ChatKit adapter used to inject the conversations in the view. */
    private DialogsListAdapter<Conversation> conversationAdapter;

    /** Python list of hangups Conversation objects. */
    private PyObject conversationList;

    /**
     * AddConversationRunnable constructor.
     *
     * @param newConversationAdapter ChatKit adapter to use
     * @param newConversationList Python list of hangups Conversation objects to add
     */
    public AddConversationRunnable(
            DialogsListAdapter<Conversation> newConversationAdapter, PyObject newConversationList) {
        conversationAdapter = newConversationAdapter;
        conversationList = newConversationList;
    }

    /** Called to run the runnable. */
    public void run() {
        Python py = Python.getInstance();
        PyObject builtins = py.getBuiltins();
        PyObject hangupsdroid = py.getModule("hangupsdroid");

        ArrayList<Conversation> conversations = new ArrayList<Conversation>();
        for (int i = 0; i < builtins.callAttr("len", conversationList).toJava(int.class); i++) {
            conversations.add(
                    new Conversation(hangupsdroid.callAttr("get_from_array", conversationList, i)));
        }

        conversationAdapter.setItems(conversations);
    }
}
