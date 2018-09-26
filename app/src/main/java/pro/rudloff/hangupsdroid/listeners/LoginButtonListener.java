package pro.rudloff.hangupsdroid.listeners;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import pro.rudloff.hangupsdroid.activities.LoginActivity;

public class LoginButtonListener implements OnClickListener, OnEditorActionListener {

    private LoginActivity activity;

    public LoginButtonListener(LoginActivity newActivity) {
        activity = newActivity;
    }

    public void onClick(View view) {
        activity.login();
    }

    public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            activity.login();
            return true;
        }

        return false;
    }
}
