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

import classes.CartItem;
import classes.FruitItem;
import classes.Item;
import classes.User;
import designpatterns.project.ecommerceapplication.R;
import designpatterns.project.ecommerceapplication.ShoppingCartActivity;

/**
 * Created by fouxx on 2015-06-14.
 */
public class CartItemListAdapter extends ArrayAdapter<CartItem> {
    private final Context context;
    private final List<CartItem> itemList;

    SessionManager sm;

    public CartItemListAdapter(Context context, List<CartItem> itemList){
        super(context, -1, itemList);
        this.context = context;
        this.itemList = itemList;

        this.sm = SessionManager.getInstance(context);
    }

    public View getView(final int position, final View convertView, final ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.cart_item_list_adapter, parent, false);

        final TextView itemName = (TextView) view.findViewById(R.id.cart_item_name);
        final TextView itemQuantity = (TextView) view.findViewById(R.id.cart_item_quantity);
        final TextView itemPrice = (TextView) view.findViewById(R.id.cart_item_price);
        final TextView collectiveItemPrice = (TextView) view.findViewById(R.id.collective_item_price);

        final Item item = itemList.get(position).getItem();

        ImageButton deleteFromCartButton = (ImageButton) view.findViewById(R.id.delete_from_cart);
        deleteFromCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(sm.getSignedInUser());
                user.deleteFromCart(itemList.get(position).getItem(), context);
                itemList.get(position).setQuantity(itemList.get(position).getQuantity() - 1);

                ShoppingCartActivity.updateCollectivePrice(user.getCollectiveCartPrice(context));

                if (itemList.get(position).getQuantity() == 0) {
                    remove(itemList.get(position));
                }
                notifyDataSetChanged();
            }
        });

        itemName.setText(item.getName());
        itemQuantity.setText("Quantity: " + itemList.get(position).getQuantity());
        if(item.getCategory().equals("fruit")){
            itemPrice.setText(((FruitItem)item).getFormattedWeight() + " * " + item.getFormattedPrice() + "(/100g)");
            ShoppingCartVisitor visitor = new ShoppingCartConcreteVisitor();
            collectiveItemPrice.setText(Item.getFormattedPrice((((Acceptable)item).accept(visitor))*itemList.get(position).getQuantity()));
        } else {
            itemPrice.setText(item.getFormattedPrice());
            collectiveItemPrice.setText(Item.getFormattedPrice(item.getPrice()*itemList.get(position).getQuantity()));
        }
        return view;

    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
