package classes;

import android.content.Context;

import java.util.List;

import database.MySQLiteHelper;

/**
 * Created by fouxx on 2015-06-13.
 */
public class Item {
    private int ID;
    private String name;
    private int price;

    public Item(int ID, String name, int price){
        this.ID = ID;
        this.name = name;
        this.price = price;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public String getFormattedPrice(){
        return "$" + price/100 + "." + ((price%100 < 10)?"0":"") + price%100;
    }

    public static String getFormattedPrice(int price){
        return "$" + price/100 + "." + ((price%100 < 10)?"0":"") + price%100;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public static List<Item> getAllItems(Context context){
        return MySQLiteHelper.getInstance(context).getAllItems();
    }

}
