package designpatterns.project.ecommerceapplication;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import classes.User;
import database.MySQLiteHelper;
import helpers.SessionManager;


public class RegisterActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText registerUsername = (EditText) findViewById(R.id.register_username);
        final EditText registerPassword = (EditText) findViewById(R.id.register_password);

        Button registerButton = (Button) findViewById(R.id.register_register);
        Button registerCancelButton = (Button) findViewById(R.id.register_cancel);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String registerUserName = registerUsername.getText().toString();
                String registerUserPassword = registerPassword.getText().toString();

                RegisterAndSignInFacade register = new RegisterAndSignInFacade(getApplicationContext());
                String registerMessage = register.registerUser(registerUserName, registerUserPassword);
                Toast.makeText(getApplicationContext(), registerMessage, Toast.LENGTH_SHORT).show();
                if(registerMessage.equals("Done!"))
                    finish();
            }
        });

        registerCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
