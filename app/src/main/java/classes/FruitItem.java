package classes;

import helpers.Acceptable;
import helpers.ShoppingCartVisitor;

/**
 * Created by fouxx on 2015-06-15.
 */
public class FruitItem extends Item implements Acceptable {
    private int weight;

    public FruitItem(int ID, String name, int price, int pricePer100GRams){
        super(ID, name, price, "fruit");
        this.setWeight(pricePer100GRams);
    }



    @Override
    public int accept(ShoppingCartVisitor visitor) {
        return visitor.visit(this);
    }

    public String getFormattedWeight(){
        return weight/100 + "." + ((weight%100 < 10)?"0":"") + weight%100 + "g";
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
