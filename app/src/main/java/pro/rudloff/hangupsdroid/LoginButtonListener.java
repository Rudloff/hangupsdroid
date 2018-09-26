package pro.rudloff.hangupsdroid;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;

public class LoginButtonListener implements OnClickListener {

    public void onClick(View view) {
        LoginActivity activity = (LoginActivity) view.getContext();

        activity.login();
    }
}
