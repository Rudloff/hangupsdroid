package pro.rudloff.hangupsdroid.listeners;

import android.util.Log;

import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter.OnDialogClickListener;

import pro.rudloff.hangupsdroid.Conversation;

public class ConversationClickListener implements OnDialogClickListener<Conversation> {
    public void onDialogClick(Conversation conversation) {
       Log.d("hangupsdroid", "Click: " + conversation.getId());
   }
}
