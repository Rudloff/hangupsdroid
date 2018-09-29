package pro.rudloff.hangupsdroid;

import android.app.Activity;
import android.widget.EditText;

/** Class used by hangups to get credentials entered by the user. */
public class CredentialsPrompt {

    /** Current activity. */
    private Activity activity;

    /**
     * CredentialsPrompt constructor.
     *
     * @param newActivity Current activity
     */
    public CredentialsPrompt(Activity newActivity) {
        activity = newActivity;
    }

    /**
     * Get the e-mail address entered by the user.
     *
     * @return E-mail address
     */
    public String get_email() {
        EditText emailText = activity.findViewById(R.id.input_email);

        return emailText.getText().toString();
    }

    /**
     * Get the password entered by the user.
     *
     * @return Password
     */
    public String get_password() {
        EditText passwordText = activity.findViewById(R.id.input_password);

        return passwordText.getText().toString();
    }
}
