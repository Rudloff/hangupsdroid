package pro.rudloff.hangupsdroid.activities;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
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
import pro.rudloff.hangupsdroid.RefreshTokenCache;
import pro.rudloff.hangupsdroid.User;
import pro.rudloff.hangupsdroid.runnables.AddConversationRunnable;
import pro.rudloff.hangupsdroid.runnables.ProgressDialogRunnable;
import pro.rudloff.hangupsdroid.runnables.UpdateConversationRunnable;

/** Activity that displays the list of all conversations. */
public class ConversationListActivity extends Activity
        implements OnDialogClickListener<Conversation> {

    /** ChatKit adapter used to inject the conversations in the view. */
    private DialogsListAdapter<Conversation> conversationAdapter;

    /** Called when the activity is created. */
    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.conversation_list);

        App app = (App) getApplicationContext();
        // If the client has been killed by the OS, we go back to the login.
        if (app.pythonApp.get("client") == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            finishAffinity();
            startActivity(intent);

            return;
        }

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
    @Override
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

    /**
     * Called when creating the menu for this activity.
     *
     * @param menu Menu
     * @return Was the menu created?
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.conversation_list, menu);

        return true;
    }

    /**
     * Called when a menu item is selected.
     *
     * @param item Menu item that was selected.
     * @return Was the action consumed?
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        App app = (App) getApplicationContext();
        RefreshTokenCache cache = new RefreshTokenCache(this);

        if (item.getItemId() == R.id.action_logout) {
            cache.clear();

            app.pythonApp.callAttr("logout");

            Intent intent = new Intent(this, LoginActivity.class);
            finishAffinity();
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.action_refresh) {
            // We force refresh the conversation list.
            runOnUiThread(
                    new ProgressDialogRunnable(this, getString(R.string.conversation_list_dialog)));

            app.pythonApp.callAttr("add_conversations", this, true);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
