package helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import classes.BookItem;
import classes.FruitItem;
import classes.Item;
import classes.User;
import designpatterns.project.ecommerceapplication.ComparatorFactory;
import designpatterns.project.ecommerceapplication.R;

/**
 * Created by fouxx on 2015-06-14.
 */
public class ItemListAdapter extends ArrayAdapter<Item> {
    private final Context context;
    private List<Item> itemList;

    SessionManager sm;

    public ItemListAdapter(Context context, List<Item> itemList){
        super(context, -1, itemList);
        this.context = context;
        this.itemList = itemList;

        this.sm = SessionManager.getInstance(context);
    }

    @Override
    public int getCount()
    {
        return itemList.size();
    }

    @Override
    public Item getItem (int pos){
        return itemList.get(pos);
    }

    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_list_adapter, null);

        Item item = itemList.get(position);

        TextView additionalInfo = (TextView) view.findViewById(R.id.additional_info);
        final TextView itemName = (TextView) view.findViewById(R.id.item_name);
        TextView itemPrice = (TextView) view.findViewById(R.id.item_price);

        itemName.setText(item.getName());

        ImageView itemIcon = (ImageView) view.findViewById(R.id.item_image);
        if(itemList.get(position).getCategory().equals("book")){
            itemIcon.setImageResource(R.drawable.book_icon);
            additionalInfo.setText("Author: " + ((BookItem) item).getAuthor());
            itemPrice.setText(item.getFormattedPrice());
        }else if(itemList.get(position).getCategory().equals("fruit")){
            itemIcon.setImageResource(R.drawable.fruit_icon);
            additionalInfo.setText("Weight: " + ((FruitItem) item).getFormattedWeight());
            itemPrice.setText("(price per 100g) " + item.getFormattedPrice());
        }

        ImageButton addToCartButton = (ImageButton) view.findViewById(R.id.add_to_cart);
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sm.isSignedIn()) {
                    User user = new User(sm.getSignedInUser());
                    user.addToCart(itemList.get(position), context);
                    Toast.makeText(context, "Added to Cart!", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(context, "Sign in first!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;

    }

    public void displayAll(){
        itemList = Item.getAllItems(context);
        Collections.sort(itemList, new ComparatorFactory().getComparator("Name ascending"));
        notifyDataSetChanged();
    }

    public void displayBooks(){
        itemList = Item.getAllItems(context);
        Collections.sort(itemList, new ComparatorFactory().getComparator("Name ascending"));
        for (Iterator<Item> iterator = itemList.iterator(); iterator.hasNext();) {
            Item item = iterator.next();
            if (item.getCategory().equals("fruit")) {
                iterator.remove();
            }
        }
        notifyDataSetChanged();
    }

    public void displayFruits(){
        itemList = Item.getAllItems(context);
        Collections.sort(itemList, new ComparatorFactory().getComparator("Name ascending"));
        for (Iterator<Item> iterator = itemList.iterator(); iterator.hasNext();) {
            Item item = iterator.next();
            if (item.getCategory().equals("book")) {
                iterator.remove();
            }
        }
        notifyDataSetChanged();
    }

    public void sortOrder(Comparator cf){
        Collections.sort(itemList, cf);
    }

    @Override
    public void sort(Comparator<? super Item> comparator) {
        super.sort(comparator);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

}
