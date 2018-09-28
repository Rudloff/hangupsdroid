package pro.rudloff.hangupsdroid.activities;

import android.os.Bundle;
import com.chaquo.python.PyObject;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.stfalcon.chatkit.messages.MessagesListAdapter.OnLoadMoreListener;
import pro.rudloff.hangupsdroid.App;
import pro.rudloff.hangupsdroid.AvatarLoader;
import pro.rudloff.hangupsdroid.Conversation;
import pro.rudloff.hangupsdroid.Message;
import pro.rudloff.hangupsdroid.R;
import pro.rudloff.hangupsdroid.User;
import pro.rudloff.hangupsdroid.runnables.AddMessageRunnable;

public class ConversationActivity extends Activity implements OnLoadMoreListener {

    private MessagesListAdapter<Message> messageAdapter;
    private Conversation conversation;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation);
        getSupportActionBar().setHomeButtonEnabled(true);

        App app = (App) getApplicationContext();

        conversation =
                new Conversation(
                        app.pythonApp.callAttr(
                                "getConversation", getIntent().getStringExtra("conversationId")));
        setTitle(conversation.getDialogName());

        MessagesList messagesListView = findViewById(R.id.messagesList);
        messageAdapter =
                new MessagesListAdapter<Message>(
                        conversation.getSelfUser().getId(), new AvatarLoader());
        messagesListView.setAdapter(messageAdapter);
        messageAdapter.setLoadMoreListener(this);
        messageAdapter.addToEnd(conversation.getMessages(this), true);
        app.pythonApp.callAttr(
                "addMessages", this, conversation.getId(), conversation.getFirstMessage().getId());
    }

    public void addMessages(PyObject messageList) {
        App app = (App) getApplicationContext();

        runOnUiThread(new AddMessageRunnable(this, messageAdapter, messageList));
        app.progressDialog.dismiss();
    }

    public void onLoadMore(int page, int totalItemsCount) {
        App app = (App) getApplicationContext();

        app.pythonApp.callAttr(
                "addMessages", this, conversation.getId(), conversation.getFirstMessage().getId());
    }

    public void onEvent(PyObject event) {
        App app = (App) getApplicationContext();

        runOnUiThread(
                new AddMessageRunnable(
                        this,
                        messageAdapter,
                        new Message(
                                event,
                                new User(
                                        app.pythonApp.callAttr("getUser", event.get("user_id"))))));
    }
}
