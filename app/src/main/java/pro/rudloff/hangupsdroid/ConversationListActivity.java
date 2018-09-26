package pro.rudloff.hangupsdroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import com.stfalcon.chatkit.commons.ImageLoader;

import android.util.Log;

public class ConversationListActivity extends AppCompatActivity {

    private DialogsListAdapter<Conversation> conversationAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation_list);
        setTitle("HangupsDroid");

        App app = (App) getApplicationContext();

        DialogsList dialogsListView = findViewById(R.id.conversationList);
        conversationAdapter = new DialogsListAdapter<Conversation>(new AvatarLoader());
        conversationAdapter.setDatesFormatter(new MessageDateFormatter());
        conversationAdapter.setOnDialogClickListener(new ConversationClickListener());
        dialogsListView.setAdapter(conversationAdapter);

        runOnUiThread(new ProgressDialogRunnable(this, getString(R.string.conversations_dialog)));

        app.pythonApp.callAttr("addConversations", this);
    }

    public void addConversations(PyObject conversationList) {
        App app = (App) getApplicationContext();

        runOnUiThread(new AddConversationRunnable(conversationAdapter, conversationList));
        app.progressDialog.dismiss();
    }

}
