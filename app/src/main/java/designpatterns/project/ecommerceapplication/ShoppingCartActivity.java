package designpatterns.project.ecommerceapplication;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

import classes.CartItem;
import classes.Item;
import classes.User;
import helpers.CartItemListAdapter;
import helpers.ItemListAdapter;
import helpers.SessionManager;


public class ShoppingCartActivity extends ActionBarActivity {
    private SessionManager sm;
    private User user;

    static TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        sm = SessionManager.getInstance(getApplicationContext());
        user = new User(sm.getSignedInUser());

        setTitle("User: " + user.getName());

        tv = (TextView) findViewById(R.id.collective_price);
        updateCollectivePrice(user.getCollectiveCartPrice(getApplicationContext()));

        List<CartItem> cartItemList = user.getCart(getApplicationContext());

        final CartItemListAdapter adapter = new CartItemListAdapter(this, cartItemList);
        ListView listView = (ListView) findViewById(R.id.cart_item_list);
        listView.setAdapter(adapter);

        for(CartItem i: cartItemList){
            System.out.println(i.getItem().getName());
        }

    }

    public static void updateCollectivePrice(String collectivePrice){
        tv.setText(collectivePrice);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shopping_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sign_out) {
            sm.setSignedOut();
            finish();
            Toast.makeText(getApplicationContext(), "Goodbye!", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
