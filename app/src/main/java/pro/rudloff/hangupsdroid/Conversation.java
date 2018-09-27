package pro.rudloff.hangupsdroid;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.commons.models.IMessage;
import java.util.ArrayList;

public class Conversation implements IDialog {

    private PyObject conversation;
    private IMessage lastMessage;

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

        ArrayList<User> users = new ArrayList<User>();
        PyObject conversationUsers = conversation.get("users");
        for (int i = 0; i < builtins.callAttr("len", conversationUsers).toJava(int.class); i++) {
            users.add(new User(conversationUsers.callAttr("pop", i)));
        }

        return users;
    }

    public IMessage getLastMessage() {
        Python py = Python.getInstance();
        PyObject hangupsdroid = py.getModule("hangupsdroid");

        if (lastMessage == null) {
            PyObject message = hangupsdroid.callAttr("getLastMessage", conversation);
            if (message != null) {
                setLastMessage(
                        new Message(
                                message,
                                conversation.callAttr("get_user", message.get("user_id"))));
            }
        }

        return lastMessage;
    }

    public void setLastMessage(IMessage message) {
        lastMessage = message;
    }

    public int getUnreadCount() {
        Python py = Python.getInstance();
        PyObject hangupsdroid = py.getModule("hangupsdroid");

        return hangupsdroid.callAttr("getNumUnread", conversation).toJava(int.class);
    }
}
