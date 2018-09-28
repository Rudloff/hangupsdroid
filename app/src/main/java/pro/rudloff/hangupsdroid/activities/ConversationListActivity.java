package pro.rudloff.hangupsdroid.activities;

import android.os.Bundle;
import com.chaquo.python.PyObject;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import pro.rudloff.hangupsdroid.App;
import pro.rudloff.hangupsdroid.AvatarLoader;
import pro.rudloff.hangupsdroid.Conversation;
import pro.rudloff.hangupsdroid.MessageDateFormatter;
import pro.rudloff.hangupsdroid.R;
import pro.rudloff.hangupsdroid.listeners.ConversationClickListener;
import pro.rudloff.hangupsdroid.runnables.AddConversationRunnable;
import pro.rudloff.hangupsdroid.runnables.ProgressDialogRunnable;

public class ConversationListActivity extends Activity {

    private DialogsListAdapter<Conversation> conversationAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation_list);

        App app = (App) getApplicationContext();

        DialogsList dialogsListView = findViewById(R.id.conversationList);
        conversationAdapter = new DialogsListAdapter<Conversation>(new AvatarLoader());
        conversationAdapter.setDatesFormatter(new MessageDateFormatter());
        conversationAdapter.setOnDialogClickListener(new ConversationClickListener(this));
        dialogsListView.setAdapter(conversationAdapter);

        runOnUiThread(
                new ProgressDialogRunnable(this, getString(R.string.conversation_list_dialog)));

        app.pythonApp.callAttr("addConversations", this);
    }

    protected void onRestart() {
        super.onRestart();
        conversationAdapter.notifyDataSetChanged();
    }

    public void addConversations(PyObject conversationList) {
        App app = (App) getApplicationContext();

        runOnUiThread(new AddConversationRunnable(conversationAdapter, conversationList));
        app.progressDialog.dismiss();
    }
}
