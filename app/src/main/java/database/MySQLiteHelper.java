package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Random;

import classes.CartItem;
import classes.Item;
import classes.User;
import helpers.SessionManager;

/**
 * Created by fouxx on 2015-06-13.
 */
public class MySQLiteHelper extends SQLiteOpenHelper{
    private static MySQLiteHelper dbInstance = null;
    private final Context context;
    private static SQLiteDatabase db;

    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USERS = "users_table";
    private static final String USERS_COLUMN_ID = "user_id";
    private static final String USERS_COLUMN_NAME = "user_name";
    private static final String USERS_COLUMN_PASSWORD = "user_password";
    private static final String USERS_COLUMN_IS_ADMIN = "user_is_admin";
    // Boolean flag = (cursor.getInt(cursor.getColumnIndex("flag")) == 1);

    private static final String[] USER_COLUMNS = {USERS_COLUMN_ID, USERS_COLUMN_NAME, USERS_COLUMN_PASSWORD, USERS_COLUMN_IS_ADMIN};


    private static final String TABLE_ITEMS = "items_table";
    private static final String ITEMS_COLUMN_ID = "item_id";
    private static final String ITEMS_COLUMN_NAME = "item_name";
    private static final String ITEMS_COLUMN_PRICE = "item_price";

    private static final String[] ITEM_COLUMNS = {ITEMS_COLUMN_ID, ITEMS_COLUMN_NAME, ITEMS_COLUMN_PRICE};

    private static final String TABLE_CART = "cart_table";
    private static final String CART_COLUMN_ITEM_ID = "cart_item_id";
    private static final String CART_COLUMN_USER_ID = "cart_user_id";
    private static final String CART_COLUMN_QUANTITY = "cart_item_quantity";

    private static final String[] CART_COLUMNS = {CART_COLUMN_ITEM_ID, CART_COLUMN_USER_ID, CART_COLUMN_QUANTITY};

    private MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public static MySQLiteHelper getInstance(Context context){
        if(dbInstance == null) {
            dbInstance = new MySQLiteHelper(context.getApplicationContext());
        }
        return dbInstance;
    }

    public SQLiteDatabase getMyWritableDatabase() {
        if ((db == null) || (!db.isOpen())) {
            db = this.getWritableDatabase();
        }
        return db;
    }

    @Override
    public void close() {
        super.close();
        if (db != null) {
            db.close();
            db = null;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db_) {
        final String USERS_CREATE = "create table "
                + TABLE_USERS + "( " + USERS_COLUMN_ID
                + " integer primary key autoincrement, " + USERS_COLUMN_NAME
                + " text not null, " + USERS_COLUMN_PASSWORD
                + " text not null, " + USERS_COLUMN_IS_ADMIN
                + " boolean not null default 0 );";
        db_.execSQL(USERS_CREATE);
/*
        User user = new User("admin", "admin");
        if(!userExists(user)){
            ContentValues values = new ContentValues();
            values.put(USERS_COLUMN_NAME, user.getName());
            values.put(USERS_COLUMN_PASSWORD, user.getPassword());
            values.put(USERS_COLUMN_IS_ADMIN, 1);

            db.insert(TABLE_USERS, null, values);
        }
*/
        final String ITEMS_CREATE = "create table "
                + TABLE_ITEMS + "( " + ITEMS_COLUMN_ID
                + " integer primary key autoincrement, " + ITEMS_COLUMN_NAME
                + " text not null, " + ITEMS_COLUMN_PRICE
                + " integer not null default 0 );";
        db_.execSQL(ITEMS_CREATE);

        final String CART_CREATE = "create table "
                + TABLE_CART + "( "  + CART_COLUMN_ITEM_ID
                + " integer not null, " + CART_COLUMN_USER_ID
                + " integer not null, " + CART_COLUMN_QUANTITY
                + " integer not null default 1 );";
        db_.execSQL(CART_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db_, int oldVersion, int newVersion) {
        db_.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db_.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        db_.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db_);
    }

    public boolean userExists(User user){

        Cursor exists = getMyWritableDatabase().query(TABLE_USERS, USER_COLUMNS, "user_name = ?", new String[] {user.getName()}, null, null, null );
        if(exists != null && exists.getCount() > 0)
            return true;
        return false;
    }

    public boolean addUser(User user, String userPassword){

        if(userExists(user))
            return false;
        else{
            ContentValues values = new ContentValues();
            values.put(USERS_COLUMN_NAME, user.getName());
            values.put(USERS_COLUMN_PASSWORD, user.getName());
            values.put(USERS_COLUMN_IS_ADMIN, user.isAdmin() ? 1 : 0);

            getMyWritableDatabase().insert(TABLE_USERS, null, values);
            return true;
        }
    }

    public boolean validateUser(User user, String userPassword){

        if(userExists(user)){
            Cursor cursor = getMyWritableDatabase().query(TABLE_USERS, USER_COLUMNS, "user_name = ?", new String[]{user.getName()}, null, null, null );
            cursor.moveToFirst();

            String password = cursor.getString(cursor.getColumnIndex(USERS_COLUMN_PASSWORD));

            if(userPassword.equals(password))
                return true;
        }
        return false;
    }

    public List<Item> getAllItems(){
        List<Item> itemList = new ArrayList<Item>();

        String query = "SELECT * FROM " + TABLE_ITEMS;
        Cursor cursor = getMyWritableDatabase().rawQuery(query, null);

        if(cursor.moveToFirst()){
            do {
                itemList.add(new Item(cursor.getInt(0), cursor.getString(1), cursor.getInt(2)));
            }while (cursor.moveToNext());
        }

        return itemList;
    }

    private int getUserID(User user){
        Cursor cursor = getMyWritableDatabase().query(TABLE_USERS, USER_COLUMNS, "user_name = ?", new String[]{user.getName()}, null, null, null );
        cursor.moveToFirst();
        int userID = cursor.getInt(cursor.getColumnIndex(USERS_COLUMN_ID));
        return userID;
    }

    public void addToCart(User user, Item item){
        // TODO
        String query = "SELECT * FROM " + TABLE_CART + " WHERE " + CART_COLUMN_USER_ID + " = " + getUserID(user) + " AND " + CART_COLUMN_ITEM_ID + " = " + item.getID();
        Cursor exists = getMyWritableDatabase().rawQuery(query, null);
        if(exists != null && exists.getCount() > 0){
            exists.moveToFirst();
            int quantity = exists.getInt(exists.getColumnIndex(CART_COLUMN_QUANTITY));
            quantity++;
            String updateQuery = "UPDATE " + TABLE_CART + " SET " + CART_COLUMN_QUANTITY + " = " + quantity + " WHERE " + CART_COLUMN_USER_ID + " = " + getUserID(user) + " AND " + CART_COLUMN_ITEM_ID + " = " + item.getID();
            getMyWritableDatabase().execSQL(updateQuery);
        }else{
            ContentValues values = new ContentValues();
            values.put(CART_COLUMN_ITEM_ID, item.getID());
            values.put(CART_COLUMN_USER_ID, getUserID(user));

            getMyWritableDatabase().insert(TABLE_CART, null, values);
        }
    }

    public void deleteFromCart(User user, Item item){
        String query = "SELECT * FROM " + TABLE_CART + " WHERE " + CART_COLUMN_USER_ID + " = " + getUserID(user) + " AND " + CART_COLUMN_ITEM_ID + " = " + item.getID();
        Cursor exists = getMyWritableDatabase().rawQuery(query, null);
        if(exists != null && exists.getCount() > 0){
            exists.moveToFirst();
            int quantity = exists.getInt(exists.getColumnIndex(CART_COLUMN_QUANTITY));
            if(quantity == 1){
                getMyWritableDatabase().delete(TABLE_CART, CART_COLUMN_ITEM_ID + " = " + item.getID() + " AND " + CART_COLUMN_USER_ID + " = " + getUserID(user), null);
            }
            else{
                quantity--;
                String updateQuery = "UPDATE " + TABLE_CART + " SET " + CART_COLUMN_QUANTITY + " = " + quantity + " WHERE " + CART_COLUMN_USER_ID + " = " + getUserID(user) + " AND " + CART_COLUMN_ITEM_ID + " = " + item.getID();
                getMyWritableDatabase().execSQL(updateQuery);
            }
        } else {
            getMyWritableDatabase().delete(TABLE_CART, CART_COLUMN_ITEM_ID + " = " + item.getID() + " AND " + CART_COLUMN_USER_ID + " = " + getUserID(user), null);
        }
    }

    public List<CartItem> getUsersCart(User user){
        List<CartItem> cartItemList = new ArrayList<CartItem>();

        String query = "SELECT * FROM " + TABLE_CART + " WHERE " + CART_COLUMN_USER_ID + " = " + getUserID(user);
        Cursor cursor = getMyWritableDatabase().rawQuery(query, null);
        if(cursor.moveToFirst()){
            do {
                int itemID = cursor.getInt(0);
                String query2 = "SELECT * FROM " + TABLE_ITEMS + " WHERE " + ITEMS_COLUMN_ID + " = " + itemID;
                Cursor cursor2 = getMyWritableDatabase().rawQuery(query2, null);

                if(cursor2.moveToFirst()){
                    do {
                        cartItemList.add(new CartItem(new Item(cursor2.getInt(0), cursor2.getString(1), cursor2.getInt(2)), cursor.getInt(2)));
                    }while (cursor2.moveToNext());
                }
            }while (cursor.moveToNext());
        }

        return cartItemList;
    }

    public void generateItems(){
        ContentValues values;
        for(int i = 1; i <= 20; i++){
            values = new ContentValues();
            String name = "Item No. " + i;
            values.put(ITEMS_COLUMN_NAME, name);
            Random rand = new Random();
            int price = rand.nextInt((10000 - 100) + 1) + 100;
            values.put(ITEMS_COLUMN_PRICE, price);
            System.out.println("Name: " + name + "; Price: " + price);
            getMyWritableDatabase().insert(TABLE_ITEMS, null, values);
        }
    }
}
