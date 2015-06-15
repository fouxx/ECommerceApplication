package helpers;

import classes.BookItem;
import classes.FruitItem;

/**
 * Created by fouxx on 2015-06-15.
 */
public interface ShoppingCartVisitor {
    int visit(BookItem book);
    int visit(FruitItem fruit);
}
