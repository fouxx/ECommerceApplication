package designpatterns.project.ecommerceapplication;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import classes.Item;
import helpers.ItemListAdapter;
import helpers.SessionManager;


public class MainActivity extends ActionBarActivity {

    SessionManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sm = SessionManager.getInstance(this);
        //MySQLiteHelper.getInstance(getApplicationContext()).generateItems();

        final ItemListAdapter adapter = new ItemListAdapter(this, Item.getAllItems(getApplicationContext()));
        adapter.sort(new NameDescComparator());
        ListView listView = (ListView) findViewById(R.id.item_list);
        listView.setAdapter(adapter);

        Spinner spinner = (Spinner) findViewById(R.id.sort_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sortOrder = parent.getItemAtPosition(position).toString();
                ComparatorFactory comparator = new ComparatorFactory();
                adapter.sort(comparator.getComparator(sortOrder));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        if (sm.isSignedIn()) {
            menu.findItem(R.id.action_sign_in).setEnabled(false).setVisible(false);
            menu.findItem(R.id.action_sign_out).setEnabled(true).setVisible(true);
            menu.findItem(R.id.action_shopping_cart).setEnabled(true).setVisible(true);
        }
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_sign_in) {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.sign_in_dialog);
            dialog.setTitle("Sign in");

            final EditText username = (EditText) dialog.findViewById(R.id.username);
            final EditText password = (EditText) dialog.findViewById(R.id.password);

            final Button signIn = (Button) dialog.findViewById(R.id.signIn);
            Button register = (Button) dialog.findViewById(R.id.register);
            Button cancel = (Button) dialog.findViewById(R.id.cancel);

            signIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userName = username.getText().toString();
                    String userPassword = password.getText().toString();
                    RegisterAndSignInFacade signInFacade = new RegisterAndSignInFacade(getApplicationContext());
                    String signInMessage = signInFacade.signInUser(userName, userPassword);
                    Toast.makeText(getApplicationContext(), signInMessage, Toast.LENGTH_SHORT).show();
                    if(signInMessage.equals("Welcome!")){
                            invalidateOptionsMenu();
                            dialog.dismiss();
                    }
                }
            });

            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(intent);
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
            return true;
        }else if(id == R.id.action_sign_out){
            sm.setSignedOut();
            Toast.makeText(getApplicationContext(), "Goodbye!", Toast.LENGTH_SHORT).show();
            invalidateOptionsMenu();
        }else if(id == R.id.action_shopping_cart){
             Intent intent = new Intent(getApplicationContext(), ShoppingCartActivity.class);
             startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
