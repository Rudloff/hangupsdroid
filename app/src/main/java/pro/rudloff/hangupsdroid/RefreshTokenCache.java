package pro.rudloff.hangupsdroid;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/** Cache used by hangups to store the refresh token. */
public class RefreshTokenCache {

    /** App preferences. */
    private SharedPreferences preferences;

    /**
     * RefreshTokenCache constructor.
     *
     * @param activity Current activity
     */
    public RefreshTokenCache(Activity activity) {
        preferences = activity.getSharedPreferences("login", Activity.MODE_PRIVATE);
    }

    /**
     * Get the cached refresh token.
     *
     * @return Refresh token
     */
    public String get() {
        return preferences.getString("refresh_token", null);
    }

    /**
     * Add a new refresh token to cache.
     *
     * @param token Refresh token
     */
    public void set(String token) {
        Editor editor = preferences.edit();
        editor.putString("refresh_token", token);
        editor.apply();
    }

    /** Clear the cache. */
    public void clear() {
        Editor editor = preferences.edit();
        editor.remove("refresh_token");
        editor.apply();
    }
}
