package pro.rudloff.hangupsdroid;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import java.util.ArrayList;

public class AddConversationRunnable implements Runnable {

    private DialogsListAdapter<Conversation> conversationAdapter;
    private PyObject conversationList;

    public AddConversationRunnable(DialogsListAdapter<Conversation> newConversationAdapter, PyObject newConversationList) {
        conversationAdapter = newConversationAdapter;
        conversationList = newConversationList;
    }

    public void run() {
        Python py = Python.getInstance();
        PyObject builtins = py.getBuiltins();

        ArrayList<Conversation> conversations = new ArrayList<Conversation>();
        for (int i = 0; i < builtins.callAttr("len", conversationList).toJava(int.class); i++) {
            conversations.add(new Conversation(conversationList.callAttr("pop", i)));
        }

        conversationAdapter.setItems(conversations);
    }
}
