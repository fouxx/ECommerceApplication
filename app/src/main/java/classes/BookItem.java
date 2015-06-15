package classes;

import helpers.Acceptable;
import helpers.ShoppingCartVisitor;

/**
 * Created by fouxx on 2015-06-15.
 */
public class BookItem extends Item implements Acceptable {
    private String author;

    public BookItem(int ID, String name, int price, String author){
        super(ID, name, price, "book");
        this.setAuthor(author);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public int accept(ShoppingCartVisitor visitor) {
        return visitor.visit(this);
    }
}
