package pro.rudloff.hangupsdroid.listeners;

import android.app.Activity;
import android.content.Intent;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter.OnDialogClickListener;
import pro.rudloff.hangupsdroid.Conversation;
import pro.rudloff.hangupsdroid.activities.ConversationActivity;

public class ConversationClickListener implements OnDialogClickListener<Conversation> {

    private Activity activity;

    public ConversationClickListener(Activity newActivity) {
        activity = newActivity;
    }

    public void onDialogClick(Conversation conversation) {
        Intent intent = new Intent(activity, ConversationActivity.class);
        intent.putExtra("conversationId", conversation.getId());
        activity.startActivity(intent);
    }
}
