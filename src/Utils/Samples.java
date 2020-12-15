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

    private static int LOWEST_ITEM_WEIGHT = 5;
    private static int ITEM_WEIGHT_VARIATION = 30;

    private static int LOWEST_ITEM_VALUE = 1;
    private static int ITEM_VALUE_VARIATION = 30;
    
    private static int LOWEST_BAG_CAPACITY = 30;
    private static int BAG_CAPACITY_VARIATION = 70;


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
            items[i] = new Item(random.nextInt(ITEM_WEIGHT_VARIATION)+LOWEST_ITEM_WEIGHT, random.nextInt(ITEM_VALUE_VARIATION)+LOWEST_ITEM_VALUE);
        }
        for(int i = 0; i<bags.length;i++){
            bags[i] = new Bag(random.nextInt(BAG_CAPACITY_VARIATION)+ LOWEST_BAG_CAPACITY);
        }
    }

    public Item[] getItems() {
        return items;
    }

    public Bag[] getBags() {
        return bags;
    }

}
