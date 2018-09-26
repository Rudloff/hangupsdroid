package pro.rudloff.hangupsdroid;

import android.app.Activity;
import android.widget.EditText;

class CredentialsPrompt {

    private Activity activity;

    public CredentialsPrompt(Activity newActivity) {
        activity = newActivity;
    }

    public String get_email() {
        EditText emailText = activity.findViewById(R.id.input_email);

        return emailText.getText().toString();
    }

    public String get_password() {
        EditText passwordText = activity.findViewById(R.id.input_password);

        return passwordText.getText().toString();
    }
}
