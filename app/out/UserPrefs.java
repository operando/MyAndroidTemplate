package com.os.operando.myandroidtemplate.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.rejasupotaro.android.kvs.PrefsSchema;

import java.lang.String;

/**
 * package com.os.operando.myandroidtemplate.model;
 * <p>
 * import com.rejasupotaro.android.kvs.annotations.Key;
 * import com.rejasupotaro.android.kvs.annotations.Table;
 *
 * @Table(name = "user")
 * public class UserPrefsSchema {
 * @Key(name = "user_id")
 * int userId;
 * }
 */
public final class UserPrefs extends PrefsSchema {
    public static final String TABLE_NAME = "user";

    private static UserPrefs singleton;

    public UserPrefs(Context context) {
        init(context, TABLE_NAME);
    }

    public UserPrefs(SharedPreferences prefs) {
        init(prefs);
    }

    public static UserPrefs get(Context context) {
        if (singleton != null) return singleton;
        synchronized (UserPrefs.class) {
            if (singleton == null) singleton = new UserPrefs(context);
        }
        ;
        return singleton;
    }

    public int getUserId(int defValue) {
        return getInt("user_id", defValue);
    }

    public int getUserId() {
        return getInt("user_id", 0);
    }

    public void setUserId(int userId) {
        putInt("user_id", userId);
    }

    public void putUserId(int userId) {
        putInt("user_id", userId);
    }

    public boolean hasUserId() {
        return has("user_id");
    }

    public void removeUserId() {
        remove("user_id");
    }
}
