package pro.rudloff.hangupsdroid.listeners;

import android.view.View;
import android.view.View.OnClickListener;
import pro.rudloff.hangupsdroid.activities.LoginActivity;

public class LoginButtonListener implements OnClickListener {

    private LoginActivity activity;

    public LoginButtonListener(LoginActivity newActivity) {
        activity = newActivity;
    }

    public void onClick(View view) {
        activity.login();
    }
}
