package Utils;

import Model.Bag;
import Model.Item;

import java.util.Random;

/**
 * Used for creating a defined problem space containing bags and items for the Multiple Knapsack Problem
 * The sample object will represent the problem space with get-methods to obtain items and bags
 */
public class Samples {
    Item[] items;
    Bag[] bags;

    public Samples() {
    }

    /**
     * Generate random values between 1-30 for items (Weights and values),
     * random values between 10 and 30 for bags (capacity).
     * Items and bags can be obtained through the corresponding get-methods.
     * @param nbrOfItems - number of items in the problem space
     * @param nbrOfBags - number of bags in the problem space
     */
    public void generateRandomValues(int nbrOfItems, int nbrOfBags){
        Random random = new Random();
        items = new Item[nbrOfItems];
        bags = new Bag[nbrOfBags];
        for(int i = 0; i<items.length; i++){
            items[i] = new Item(random.nextInt(30)+1, random.nextInt(30)+1);
        }
        for(int i = 0; i<bags.length;i++){
            bags[i] = new Bag(random.nextInt(21)+10);
        }
    }

    public Item[] getItems() {
        return items;
    }

    public Bag[] getBags() {
        return bags;
    }

//    public static void main(String[] args) {
//        Samples s = new Samples();
//        s.generateRandomValues(3, 1);
//        System.out.println(s.getItems()[0].toString());
//        s.getBags()[0].setItems(s.getItems()[0]);
//        System.out.println(s.getBags()[0].toString());
//        s.getBags()[0].removeItem(s.getItems()[0]);
//        System.out.println(s.getBags()[0].toString());
//    }
}
