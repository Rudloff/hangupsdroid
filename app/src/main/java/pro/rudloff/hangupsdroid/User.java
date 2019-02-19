package pro.rudloff.hangupsdroid;

import com.chaquo.python.PyObject;
import com.stfalcon.chatkit.commons.models.IUser;

/** Class used to manage users. */
public class User implements IUser {

    /** hangups User object. */
    private PyObject user;

    /**
     * User constructor.
     *
     * @param newUser hangups User object
     */
    public User(PyObject newUser) {
        user = newUser;
    }

    /**
     * Get the user ID.
     *
     * @return ID
     */
    @Override
    public String getId() {
        return user.get("id_").toString();
    }

    /**
     * Get the user full name.
     *
     * @return Full name
     */
    @Override
    public String getName() {
        return user.get("full_name").toString();
    }

    /**
     * Get the user avatar.
     *
     * @return Avatar URL
     */
    @Override
    public String getAvatar() {
        PyObject photoUrl = user.get("photo_url");
        if (photoUrl != null) {
            return "https:" + user.get("photo_url").toString();
        }

        return null;
    }
}
