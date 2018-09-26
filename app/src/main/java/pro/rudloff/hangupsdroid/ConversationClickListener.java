package pro.rudloff.hangupsdroid;

import android.util.Log;

import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter.OnDialogClickListener;

public class ConversationClickListener implements OnDialogClickListener<Conversation> {
    public void onDialogClick(Conversation conversation) {
       Log.d("hangupsdroid", "Click: " + conversation.getId());
   }
}
