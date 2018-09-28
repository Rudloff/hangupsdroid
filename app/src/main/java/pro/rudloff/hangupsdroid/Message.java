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

public class Message implements IMessage, Image {

    private PyObject message;
    private User user;

    public Message(PyObject newMessage, User newMessageUser) {
        message = newMessage;
        user = newMessageUser;
    }

    public String getId() {
        return message.get("id_").toString();
    }

    public String getText() {
        return message.get("text").toString();
    }

    public String getImageUrl() {
        Python py = Python.getInstance();
        PyObject hangupsdroid = py.getModule("hangupsdroid");

        PyObject attachment = hangupsdroid.callAttr("getAttachment", message);
        if (attachment != null) {
            return attachment.toString();
        }

        return null;
    }

    public User getUser() {
        return user;
    }

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
