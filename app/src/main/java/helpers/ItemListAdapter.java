package helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import classes.Item;
import classes.User;
import designpatterns.project.ecommerceapplication.R;

/**
 * Created by fouxx on 2015-06-14.
 */
public class ItemListAdapter extends ArrayAdapter<Item> {
    private final Context context;
    private final List<Item> itemList;

    SessionManager sm;

    public ItemListAdapter(Context context, List<Item> itemList){
        super(context, -1, itemList);
        this.context = context;
        this.itemList = itemList;

        this.sm = SessionManager.getInstance(context);
    }

    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_list_adapter, parent, false);

        final TextView itemName = (TextView) view.findViewById(R.id.item_name);
        TextView itemPrice = (TextView) view.findViewById(R.id.item_price);

        ImageButton addToCartButton = (ImageButton) view.findViewById(R.id.add_to_cart);
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sm.isSignedIn()){
                    User user = new User(sm.getSignedInUser());
                    user.addToCart(itemList.get(position), context);
                    Toast.makeText(context, "Added to Cart!", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(context, "Sign in first!", Toast.LENGTH_SHORT).show();
            }
        });

        Item item = itemList.get(position);
        itemName.setText(item.getName());
        itemPrice.setText(item.getFormattedPrice());

        return view;

    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

}
