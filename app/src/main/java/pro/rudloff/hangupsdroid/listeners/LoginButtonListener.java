package pro.rudloff.hangupsdroid.listeners;

import android.view.View;
import android.view.View.OnClickListener;
import pro.rudloff.hangupsdroid.activities.LoginActivity;

public class LoginButtonListener implements OnClickListener {

    public void onClick(View view) {
        LoginActivity activity = (LoginActivity) view.getContext();

        activity.login();
    }
}
