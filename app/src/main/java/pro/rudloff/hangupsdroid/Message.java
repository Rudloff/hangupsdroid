package pro.rudloff.hangupsdroid;

import android.util.Log;

import com.chaquo.python.PyObject;

import com.stfalcon.chatkit.commons.models.IMessage;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Message implements IMessage {

    private PyObject message;
    private PyObject user;

    public Message(PyObject newMessage, PyObject newMessageUser) {
        message = newMessage;
        user = newMessageUser;
    }

    public String getId() {
        return message.get("id_").toString();
    }

    public String getText() {
        return message.get("text").toString();
    }

    public User getUser() {
        return new User(user);
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
