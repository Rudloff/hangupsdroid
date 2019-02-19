package pro.rudloff.hangupsdroid;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.InputType;
import android.widget.EditText;
import pro.rudloff.hangupsdroid.runnables.ShowDialogRunnable;

/** Class used by hangups to get credentials entered by the user. */
public class CredentialsPrompt implements OnClickListener {

    /** Current activity. */
    private Activity activity;

    /** Verification code. */
    private String verificationCode;

    /** Text input containing the verification code. */
    private EditText verificationCodeInput;

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

    /**
     * Ask the user for a verification code.
     *
     * @return Verification code
     */
    public String get_verification_code() {
        Builder builder = new Builder(activity);
        verificationCodeInput = new EditText(activity);
        verificationCodeInput.setInputType(
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(verificationCodeInput)
                .setCancelable(false)
                .setTitle("Verification code")
                .setPositiveButton(android.R.string.yes, this);

        activity.runOnUiThread(new ShowDialogRunnable(builder));

        while (verificationCode == null) {}

        return verificationCode;
    }

    /**
     * Called when the verification code dialog is submitted.
     *
     * @param dialog Verification code dialog
     * @param which Button that was clicked
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {
        verificationCode = verificationCodeInput.getText().toString();
    }
}
