package Model;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Neighborhood consists of one solution with a set of bags and a set of unused items
 */
public class Neighborhood {
    ArrayList<Item> unusedItems;
    Bag[] bags;

    public Neighborhood(ArrayList<Item> unusedItems, Bag[] bags) {
        this.unusedItems = new ArrayList<>(unusedItems);
        this.bags = bags.clone();
    }

    public ArrayList<Item> getUnusedItems() {
        return unusedItems;
    }

    public Bag[] getBags() {
        return bags;
    }

    public int getTotalValue(){
        int res = 0;
        for(Bag b : bags){
            res+=b.getTotalValue();
        }
        return res;
    }

    public String toString(){
        return "Neighborhood with value: " + getTotalValue() + "\n"+"bags  " + bags[0].toString() + " . " + bags[1].toString();
    }
}
