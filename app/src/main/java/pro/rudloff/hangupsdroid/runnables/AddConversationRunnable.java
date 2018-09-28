package pro.rudloff.hangupsdroid.runnables;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import java.util.ArrayList;
import pro.rudloff.hangupsdroid.Conversation;

public class AddConversationRunnable implements Runnable {

    private DialogsListAdapter<Conversation> conversationAdapter;
    private PyObject conversationList;

    public AddConversationRunnable(
            DialogsListAdapter<Conversation> newConversationAdapter, PyObject newConversationList) {
        conversationAdapter = newConversationAdapter;
        conversationList = newConversationList;
    }

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
