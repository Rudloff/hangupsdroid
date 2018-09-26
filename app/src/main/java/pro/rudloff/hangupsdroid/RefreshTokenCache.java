package pro.rudloff.hangupsdroid;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

class RefreshTokenCache {

    private SharedPreferences preferences;

    public RefreshTokenCache(Activity activity) {
        preferences = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    public String get() {
        return preferences.getString("refresh_token", null);
    }

    public void set(String token) {
        Editor editor = preferences.edit();
        editor.putString("refresh_token", token);
        editor.apply();
    }
}
