package Model;

import java.util.ArrayList;

/**
 * A representation of the Bag/Knapsack that we will fill with items
 */
public class Bag {
    int capacity;
    int currentSize;
    int totalValue;
    ArrayList<Item> items;

    public Bag(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(int currentSize) {
        this.currentSize = currentSize;
    }

    public int getTotalValue() {
        return totalValue;
    }

    public void incrementTotalValue(int value){
        this.totalValue+=value;
    }

    public void decrementTotalValue(int value){
        this.totalValue-=value;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(Item item) {
        items.add(item);
    }

    public Item removeItem(int index) {
        return items.remove(index);
    }

}
