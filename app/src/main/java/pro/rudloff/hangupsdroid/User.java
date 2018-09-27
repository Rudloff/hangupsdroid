package pro.rudloff.hangupsdroid;

import com.chaquo.python.PyObject;
import com.stfalcon.chatkit.commons.models.IUser;

public class User implements IUser {

    private PyObject user;

    public User(PyObject newUser) {
        user = newUser;
    }

    public String getId() {
        return user.get("id_").toString();
    }

    public String getName() {
        return user.get("full_name").toString();
    }

    public String getAvatar() {
        PyObject photoUrl = user.get("photo_url");
        if (photoUrl != null) {
            return "https:" + user.get("photo_url").toString();
        }

        return null;
    }
}
