package designpatterns.project.ecommerceapplication;

import java.util.Comparator;

import classes.Item;

/**
 * Created by fouxx on 2015-06-14.
 */
class NameDescComparator implements Comparator<Item> {
    @Override
    public int compare(Item item1, Item item2) {
        return item2.getName().compareTo(item1.getName());
    }
}

class PriceDescComparator implements Comparator<Item>{
    @Override
    public int compare(Item item1, Item item2) {

        return item2.getPrice()-item1.getPrice();
    }
}

class NameAscComparator implements Comparator<Item> {
    @Override
    public int compare(Item item1, Item item2) {
        return item1.getName().compareTo(item2.getName());
    }
}

class PriceAscComparator implements Comparator<Item>{
    @Override
    public int compare(Item item1, Item item2) {
        return item1.getPrice()-item2.getPrice();
    }
}

public class ComparatorFactory{
    public Comparator<Item> getComparator(String order){
        switch (order){
            case "Name ascending":
                return new NameAscComparator();
            case "Name descending":
                return new NameDescComparator();
            case "Price ascending":
                return new PriceAscComparator();
            case "Price descending":
                return new PriceDescComparator();
            default:
                return new NameAscComparator();
        }
    }
}