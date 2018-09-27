package pro.rudloff.hangupsdroid.activities;

import android.os.Bundle;
import com.chaquo.python.PyObject;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import pro.rudloff.hangupsdroid.App;
import pro.rudloff.hangupsdroid.AvatarLoader;
import pro.rudloff.hangupsdroid.Conversation;
import pro.rudloff.hangupsdroid.Message;
import pro.rudloff.hangupsdroid.R;
import pro.rudloff.hangupsdroid.runnables.AddMessageRunnable;
import pro.rudloff.hangupsdroid.runnables.ProgressDialogRunnable;

public class ConversationActivity extends Activity {

    private MessagesListAdapter<Message> messageAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation);

        App app = (App) getApplicationContext();

        Conversation conversation =
                new Conversation(
                        app.pythonApp.callAttr(
                                "getConversation", getIntent().getStringExtra("conversationId")));
        setTitle(conversation.getDialogName());

        MessagesList messagesListView = findViewById(R.id.messagesList);
        messageAdapter =
                new MessagesListAdapter<Message>(
                        conversation.getSelfUser().getId(), new AvatarLoader());
        messagesListView.setAdapter(messageAdapter);

        runOnUiThread(new ProgressDialogRunnable(this, getString(R.string.conversation_dialog)));

        app.pythonApp.callAttr("addMessages", this, conversation.getId());
    }

    public void addMessages(PyObject messageList) {
        App app = (App) getApplicationContext();

        runOnUiThread(new AddMessageRunnable(this, messageAdapter, messageList));
        app.progressDialog.dismiss();
    }
}
