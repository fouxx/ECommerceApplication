package helpers;

import classes.BookItem;
import classes.FruitItem;

/**
 * Created by fouxx on 2015-06-15.
 */
public class ShoppingCartConcreteVisitor implements ShoppingCartVisitor{

    @Override
    public int visit(BookItem book) {
        int price = book.getPrice();
        return price;
    }

    @Override
    public int visit(FruitItem fruit) {
        int price = fruit.getPrice();
        System.out.println(price);
        int weight = fruit.getWeight();
        System.out.println(weight);
        // Price is per 100 grams
        double f = (((double)price)*((double)weight))/10000;
        System.out.println(f);
        int actualPrice = (int) f;
        System.out.println(actualPrice);
        return actualPrice;
    }
}
