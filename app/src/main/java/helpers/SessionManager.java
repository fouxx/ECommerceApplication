package helpers;

import android.content.Context;
import android.content.SharedPreferences;

import classes.User;

/**
 * Created by fouxx on 2015-06-13.
 */
public class SessionManager {
    private static SessionManager sessionManager = null;
    private SharedPreferences pref;
    private Context _context;
    private SharedPreferences.Editor editor;

    private static final String PREF_NAME = "ECommerceAppPrefs";
    int PRIVATE_MODE = 0;

    public static final String KEY_USERNAME = "username";

    private SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public static SessionManager getInstance(Context context){
        if(sessionManager == null) {
            sessionManager = new SessionManager(context.getApplicationContext());
        }
        return sessionManager;
    }

    public boolean isSignedIn(){
        String s = pref.getString(KEY_USERNAME, null);
        if(s == null || s.isEmpty())
            return false;
        return true;
    }

    public String getSignedInUser(){
        return pref.getString(KEY_USERNAME, null);
    }

    public void setSignedIn(User user) {
        editor.putString(KEY_USERNAME, user.getName());
        editor.commit();
    }

    public void setSignedOut(){
        editor.clear();
        editor.commit();
    }
}
