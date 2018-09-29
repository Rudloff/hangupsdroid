package pro.rudloff.hangupsdroid.activities;

import android.content.Intent;
import android.os.Bundle;
import com.chaquo.python.PyObject;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter.OnDialogClickListener;
import pro.rudloff.hangupsdroid.App;
import pro.rudloff.hangupsdroid.AvatarLoader;
import pro.rudloff.hangupsdroid.Conversation;
import pro.rudloff.hangupsdroid.Message;
import pro.rudloff.hangupsdroid.MessageDateFormatter;
import pro.rudloff.hangupsdroid.R;
import pro.rudloff.hangupsdroid.User;
import pro.rudloff.hangupsdroid.runnables.AddConversationRunnable;
import pro.rudloff.hangupsdroid.runnables.ProgressDialogRunnable;
import pro.rudloff.hangupsdroid.runnables.UpdateConversationRunnable;

/** Activity that displays the list of all conversations. */
public class ConversationListActivity extends Activity
        implements OnDialogClickListener<Conversation> {

    /** ChatKit adapter used to inject the conversations in the view. */
    private DialogsListAdapter<Conversation> conversationAdapter;

    /**
     * Called when the activity is created.
     *
     * @param savedInstanceState Saved state of the activity if it has been previously killed by the
     *     OS.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation_list);

        App app = (App) getApplicationContext();

        DialogsList dialogsListView = findViewById(R.id.conversationList);
        conversationAdapter = new DialogsListAdapter<Conversation>(new AvatarLoader());
        conversationAdapter.setDatesFormatter(new MessageDateFormatter(this));
        conversationAdapter.setOnDialogClickListener(this);
        dialogsListView.setAdapter(conversationAdapter);

        runOnUiThread(
                new ProgressDialogRunnable(this, getString(R.string.conversation_list_dialog)));

        app.pythonApp.callAttr("add_conversations", this);
    }

    /**
     * Called when the user clics on a conversation.
     *
     * @param conversation Conversation clicked
     */
    public void onDialogClick(Conversation conversation) {
        Intent intent = new Intent(this, ConversationActivity.class);
        intent.putExtra("conversationId", conversation.getId());
        startActivity(intent);
    }

    /**
     * Called by our Python modfule when new conversations are avaiable.
     *
     * @param conversationList Python list of hangups Conversation objects.
     */
    public void onNewConversations(PyObject conversationList) {
        App app = (App) getApplicationContext();

        runOnUiThread(new AddConversationRunnable(conversationAdapter, conversationList));
        app.progressDialog.dismiss();
    }

    /**
     * Called by our Python module when a new hangups ChatMessageEvent is received.
     *
     * @param event hangups ConversationEvent object
     */
    public void onChatMessageEvent(PyObject event) {
        App app = (App) getApplicationContext();

        runOnUiThread(
                new UpdateConversationRunnable(
                        conversationAdapter,
                        new Conversation(
                                app.pythonApp.callAttr(
                                        "get_conversation", event.get("conversation_id"))),
                        new Message(
                                event,
                                new User(
                                        app.pythonApp.callAttr(
                                                "get_user", event.get("user_id"))))));
    }
}
