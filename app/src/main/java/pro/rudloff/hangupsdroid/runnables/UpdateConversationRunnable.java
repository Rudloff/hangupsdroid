package pro.rudloff.hangupsdroid.runnables;

import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import pro.rudloff.hangupsdroid.Conversation;
import pro.rudloff.hangupsdroid.Message;

/** Runnable used to update a specific conversation in the view. */
public class UpdateConversationRunnable implements Runnable {

    /** ChatKit adapter used to inject the conversations in the view. */
    private DialogsListAdapter<Conversation> conversationAdapter;

    /** Conversation to update. */
    private Conversation conversation;

    /** New message to add to the conversation. */
    private Message message;

    /**
     * UpdateConversationRunnable constructor.
     *
     * @param newConversationAdapter ChatKit adapter to use
     * @param newConversation Conversation to update
     * @param newMessage New message to add to the conversation
     */
    public UpdateConversationRunnable(
            DialogsListAdapter<Conversation> newConversationAdapter,
            Conversation newConversation,
            Message newMessage) {
        conversationAdapter = newConversationAdapter;
        conversation = newConversation;
        message = newMessage;
    }

    /** Called to run the runnable. */
    @Override
    public void run() {
        if (!conversationAdapter.updateDialogWithMessage(conversation.getId(), message)) {
            conversationAdapter.addItem(conversation);
        }
    }
}
