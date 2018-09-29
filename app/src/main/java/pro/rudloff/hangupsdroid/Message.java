package pro.rudloff.hangupsdroid;

import android.util.Log;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.MessageContentType.Image;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/** Class used to manage messages. */
public class Message implements IMessage, Image {

    /** hangups ConversationEvent object. */
    private PyObject message;

    /** User that sent this message. */
    private User user;

    /**
     * Message constructor.
     *
     * @param newMessage hangups ConversationEvent object
     * @param newMessageUser User that sent this message
     */
    public Message(PyObject newMessage, User newMessageUser) {
        message = newMessage;
        user = newMessageUser;
    }

    /**
     * Get the message ID.
     *
     * @return ID
     */
    public String getId() {
        return message.get("id_").toString();
    }

    /**
     * Get the message content.
     *
     * @return Content
     */
    public String getText() {
        return message.get("text").toString();
    }

    /**
     * Get the image URL (if the message is an image).
     *
     * @return Image URL
     */
    public String getImageUrl() {
        Python py = Python.getInstance();
        PyObject hangupsdroid = py.getModule("hangupsdroid");

        PyObject attachment = hangupsdroid.callAttr("get_attachment", message);
        if (attachment != null) {
            return attachment.toString();
        }

        return null;
    }

    /**
     * Get the user that sent the message.
     *
     * @return User
     */
    public User getUser() {
        return user;
    }

    /**
     * Get the message creation date.
     *
     * @return Creation date
     */
    public Date getCreatedAt() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS", Locale.US);
        String date = message.get("timestamp").toString();
        Date createdAt = null;

        try {
            createdAt = dateFormat.parse(date);
        } catch (ParseException e) {
            Log.e("hangupsdroid", "Invalid date: " + date);
        }

        return createdAt;
    }
}
