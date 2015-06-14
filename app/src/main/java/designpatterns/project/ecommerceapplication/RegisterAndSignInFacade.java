package designpatterns.project.ecommerceapplication;

import android.content.Context;
import android.widget.Toast;

import classes.User;
import database.MySQLiteHelper;
import helpers.SessionManager;

/**
 * Created by fouxx on 2015-06-14.
 */
public class RegisterAndSignInFacade {
    private final Context context;
    private SessionManager sm;
    private MySQLiteHelper db;

    public RegisterAndSignInFacade(Context context){
        this.context = context;
        sm = SessionManager.getInstance(context);
        db = MySQLiteHelper.getInstance(context);
    }

    public String registerUser(String userName, String password){
        if(userName.equals("") || password.equals(""))
            return "Name or Password cannot be empty!";
        else{
            User user = new User(userName);
            if(!db.addUser(user, password))
                return "User already exists!";
            else
                return "Done!";
        }
    }

    public String signInUser(String userName, String password){
        if(userName.equals("") || password.equals(""))
            return "Name or Password cannot be empty!";
        else{
            User user = new User(userName);
            if(db.validateUser(user, password)) {
                sm.setSignedIn(user);
                return "Welcome!";
            }else
                return "Incorrect username or password";
        }
    }

}
