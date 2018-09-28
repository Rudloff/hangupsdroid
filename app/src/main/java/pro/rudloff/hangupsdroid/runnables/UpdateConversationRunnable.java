package pro.rudloff.hangupsdroid.runnables;

import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import pro.rudloff.hangupsdroid.Conversation;
import pro.rudloff.hangupsdroid.Message;

public class UpdateConversationRunnable implements Runnable {

    private DialogsListAdapter<Conversation> conversationAdapter;
    private Conversation conversation;
    private Message message;

    public UpdateConversationRunnable(
            DialogsListAdapter<Conversation> newConversationAdapter,
            Conversation newConversation,
            Message newMessage) {
        conversationAdapter = newConversationAdapter;
        conversation = newConversation;
        message = newMessage;
    }

    public void run() {
        if (!conversationAdapter.updateDialogWithMessage(conversation.getId(), message)) {
            conversationAdapter.addItem(conversation);
        }
    }
}
