package pro.rudloff.hangupsdroid;

import android.app.Activity;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.commons.models.IMessage;
import java.util.ArrayList;

public class Conversation implements IDialog {

    private PyObject conversation;

    public Conversation(PyObject newConversation) {
        conversation = newConversation;
    }

    public String getId() {
        return conversation.get("id_").toString();
    }

    public User getSelfUser() {
        Python py = Python.getInstance();
        PyObject hangupsdroid = py.getModule("hangupsdroid");

        return new User(hangupsdroid.callAttr("getSelfUser", conversation.get("users")));
    }

    public String getDialogPhoto() {
        Python py = Python.getInstance();
        PyObject builtins = py.getBuiltins();
        PyObject hangupsdroid = py.getModule("hangupsdroid");

        PyObject conversationUsers = conversation.get("users");
        int nbUsers = builtins.callAttr("len", conversationUsers).toJava(int.class);
        if (nbUsers <= 2) {
            PyObject otherUser = hangupsdroid.callAttr("getOtherUser", conversationUsers);
            if (otherUser != null) {
                User user = new User(otherUser);
                return user.getAvatar();
            }
        }

        return "android.resource://android/drawable/ic_menu_allfriends";
    }

    public String getDialogName() {
        Python py = Python.getInstance();
        PyObject utils = py.getModule("hangups.ui.utils");

        return utils.callAttr("get_conv_name", conversation).toString();
    }

    public ArrayList<User> getUsers() {
        Python py = Python.getInstance();
        PyObject builtins = py.getBuiltins();
        PyObject hangupsdroid = py.getModule("hangupsdroid");

        ArrayList<User> users = new ArrayList<User>();
        PyObject conversationUsers = conversation.get("users");

        int nbUsers = builtins.callAttr("len", conversationUsers).toJava(int.class);
        if (nbUsers <= 2) {
            // If this is not a group conversation, we only display the other user.
            PyObject otherUser = hangupsdroid.callAttr("getOtherUser", conversationUsers);
            if (otherUser != null) {
                users.add(new User(otherUser));
            }
        } else {
            for (int i = 0;
                    i < builtins.callAttr("len", conversationUsers).toJava(int.class);
                    i++) {
                users.add(new User(hangupsdroid.callAttr("getFromArray", conversationUsers, i)));
            }
        }

        return users;
    }

    public IMessage getLastMessage() {
        Python py = Python.getInstance();
        PyObject hangupsdroid = py.getModule("hangupsdroid");

        PyObject message = hangupsdroid.callAttr("getLastMessage", conversation);
        if (message != null) {
            return new Message(
                    message, new User(conversation.callAttr("get_user", message.get("user_id"))));
        }

        return null;
    }

    public IMessage getFirstMessage() {
        Python py = Python.getInstance();
        PyObject hangupsdroid = py.getModule("hangupsdroid");

        PyObject message = hangupsdroid.callAttr("getFirstMessage", conversation);
        if (message != null) {
            return new Message(
                    message, new User(conversation.callAttr("get_user", message.get("user_id"))));
        }

        return null;
    }

    public void setLastMessage(IMessage message) {
        // We the hangups conversation object to manage the last message.
    }

    public int getUnreadCount() {
        Python py = Python.getInstance();
        PyObject hangupsdroid = py.getModule("hangupsdroid");

        return hangupsdroid.callAttr("getNumUnread", conversation).toJava(int.class);
    }

    public ArrayList<Message> getMessages(Activity activity) {
        App app = (App) activity.getApplicationContext();

        Python py = Python.getInstance();
        PyObject builtins = py.getBuiltins();
        PyObject hangupsdroid = py.getModule("hangupsdroid");

        ArrayList<Message> messages = new ArrayList<Message>();
        PyObject messageList = hangupsdroid.callAttr("getChatMessages", conversation.get("events"));
        for (int i = 0; i < builtins.callAttr("len", messageList).toJava(int.class); i++) {
            PyObject message = hangupsdroid.callAttr("getFromArray", messageList, i);
            messages.add(
                    new Message(
                            message,
                            new User(app.pythonApp.callAttr("getUser", message.get("user_id")))));
        }
        return messages;
    }
}
