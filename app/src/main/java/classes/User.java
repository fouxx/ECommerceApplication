package classes;

import android.content.Context;

import java.util.List;

import database.MySQLiteHelper;
import helpers.Acceptable;
import helpers.ShoppingCartConcreteVisitor;
import helpers.ShoppingCartVisitor;

/**
 * Created by fouxx on 2015-06-13.
 */
public class User {
    private String name;
    private boolean isAdmin;

    public User(String name){
        this.name = name;
        isAdmin = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public boolean exists(Context context){
        return MySQLiteHelper.getInstance(context).userExists(this);
    }

    public void addToCart(Item item, Context context){
        MySQLiteHelper.getInstance(context).addToCart(this, item);
    }

    public void deleteFromCart(Item item, Context context){
        MySQLiteHelper.getInstance(context).deleteFromCart(this, item);
    }

    public List<CartItem> getCart(Context context){
        return MySQLiteHelper.getInstance(context).getUsersCart(this);
    }

    public String getCollectiveCartPrice(Context context){
        List<CartItem> items = getCart(context);
        int collectivePrice = 0;
        ShoppingCartVisitor visitor = new ShoppingCartConcreteVisitor();
        for(CartItem i : items){
            collectivePrice += (((Acceptable)i.getItem()).accept(visitor))*i.getQuantity();
        }
        return Item.getFormattedPrice(collectivePrice);
    }
}