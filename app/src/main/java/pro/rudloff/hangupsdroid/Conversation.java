package pro.rudloff.hangupsdroid;

import android.app.Activity;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.commons.models.IMessage;
import java.util.ArrayList;

/** Class used to manage conversations. */
public class Conversation implements IDialog {

    /** hangups Conversation object. */
    private PyObject conversation;

    /**
     * Conversation constructor.
     *
     * @param newConversation hangups Conversation object
     */
    public Conversation(PyObject newConversation) {
        conversation = newConversation;
    }

    /**
     * Get the conversation ID.
     *
     * @return ID
     */
    @Override
    public String getId() {
        return conversation.get("id_").toString();
    }

    /**
     * Get the User object corresponding to the user currently using the app.
     *
     * @return Current user
     */
    public User getSelfUser() {
        Python py = Python.getInstance();
        PyObject hangupsdroid = py.getModule("hangupsdroid");

        return new User(hangupsdroid.callAttr("get_self_user", conversation.get("users")));
    }

    /**
     * Get the conversation thumbnail.
     *
     * @return Thumbnail URL
     */
    @Override
    public String getDialogPhoto() {
        Python py = Python.getInstance();
        PyObject builtins = py.getBuiltins();
        PyObject hangupsdroid = py.getModule("hangupsdroid");

        PyObject conversationUsers = conversation.get("users");
        int nbUsers = builtins.callAttr("len", conversationUsers).toJava(int.class);
        if (nbUsers <= 2) {
            PyObject otherUser = hangupsdroid.callAttr("get_other_user", conversationUsers);
            if (otherUser != null) {
                User user = new User(otherUser);
                return user.getAvatar();
            }
        }

        return "android.resource://android/drawable/ic_menu_allfriends";
    }

    /**
     * Get the conversation title.
     *
     * @return Conversation title
     */
    @Override
    public String getDialogName() {
        Python py = Python.getInstance();
        PyObject utils = py.getModule("hangups.ui.utils");

        return utils.callAttr("get_conv_name", conversation).toString();
    }

    /**
     * Get a list of users in this conversation.
     *
     * @return List of users
     */
    @Override
    public ArrayList<User> getUsers() {
        Python py = Python.getInstance();
        PyObject builtins = py.getBuiltins();
        PyObject hangupsdroid = py.getModule("hangupsdroid");

        ArrayList<User> users = new ArrayList<User>();
        PyObject conversationUsers = conversation.get("users");

        int nbUsers = builtins.callAttr("len", conversationUsers).toJava(int.class);
        if (nbUsers <= 2) {
            // If this is not a group conversation, we only display the other user.
            PyObject otherUser = hangupsdroid.callAttr("get_other_user", conversationUsers);
            if (otherUser != null) {
                users.add(new User(otherUser));
            }
        } else {
            for (int i = 0;
                    i < builtins.callAttr("len", conversationUsers).toJava(int.class);
                    i++) {
                users.add(new User(hangupsdroid.callAttr("get_from_array", conversationUsers, i)));
            }
        }

        return users;
    }

    /**
     * Get the last (newest) message loaded in this conversation.
     *
     * @return Last message
     */
    @Override
    public IMessage getLastMessage() {
        Python py = Python.getInstance();
        PyObject hangupsdroid = py.getModule("hangupsdroid");

        PyObject message = hangupsdroid.callAttr("get_last_message", conversation);
        if (message != null) {
            return new Message(
                    message, new User(conversation.callAttr("get_user", message.get("user_id"))));
        }

        return null;
    }

    /**
     * Get the first (oldest) message loaded in this conversation.
     *
     * @return First message
     */
    public IMessage getFirstMessage() {
        Python py = Python.getInstance();
        PyObject hangupsdroid = py.getModule("hangupsdroid");

        PyObject message = hangupsdroid.callAttr("get_first_message", conversation);
        if (message != null) {
            return new Message(
                    message, new User(conversation.callAttr("get_user", message.get("user_id"))));
        }

        return null;
    }

    /**
     * Set a new last message manually.
     *
     * <p>This actually does nothing because we use hangups to handle this.
     *
     * @param message Message
     */
    @Override
    public void setLastMessage(IMessage message) {
        // We use the hangups conversation object to manage the last message.
    }

    /**
     * Get the number of unread messages in the conversation.
     *
     * @return Number of unread messages
     */
    @Override
    public int getUnreadCount() {
        Python py = Python.getInstance();
        PyObject hangupsdroid = py.getModule("hangupsdroid");

        return hangupsdroid.callAttr("get_num_unread", conversation).toJava(int.class);
    }

    /**
     * Get a list of all messages in the conversation.
     *
     * @param activity Current activity
     * @return List of all messages
     */
    public ArrayList<Message> getMessages(Activity activity) {
        App app = (App) activity.getApplicationContext();

        Python py = Python.getInstance();
        PyObject builtins = py.getBuiltins();
        PyObject hangupsdroid = py.getModule("hangupsdroid");

        ArrayList<Message> messages = new ArrayList<Message>();
        PyObject messageList =
                hangupsdroid.callAttr("get_chat_messages", conversation.get("events"));
        for (int i = 0; i < builtins.callAttr("len", messageList).toJava(int.class); i++) {
            PyObject message = hangupsdroid.callAttr("get_from_array", messageList, i);
            messages.add(
                    new Message(
                            message,
                            new User(app.pythonApp.callAttr("get_user", message.get("user_id")))));
        }
        return messages;
    }
}
