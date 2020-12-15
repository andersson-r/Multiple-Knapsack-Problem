package Algorithm;

import Model.Bag;
import Model.Item;
import Utils.Samples;

import java.util.ArrayList;

/**
 * Uses the item benefit calculation in order to generate a viable solution to the multiple knapsack problem.
 */
public class Greedy {
    private Bag[] bags;
    private ArrayList<Item> items = new ArrayList<Item>();
    private ArrayList<Item> unusedItems = new ArrayList<>();

    public Greedy(Item[] items, Bag[] bags) {

        this.bags = bags;


        for (int i = 0; i < items.length; i++) {

            this.items.add(items[i]);
        }
    }

    /*
       Selects "best" item to add based on calculated benefit (value/weight)
       If that item can't be added it is discarded.
       Solution is terminated when no items can be added or all items have already been added to the knapsacks.

     */
    public void solve() {


        while (items.size() != 0) {

            Item currentBest = items.get(0);
            for (Item item : items) {
                if (item.getBenefit() > currentBest.getBenefit()) {
                    currentBest = item;
                }
            }

            items.remove(currentBest);

            addToKnapsack(currentBest);

        }

    }

    /**
     *
     * Adds incoming item to the knapsack with the least amount of remaining space but enough space to fit the item
     * If item can not be added to any knapsack it is removed from the items list and added to the unused items list.
     *
     * @param item item to be added to knapsack or discarded
     */
    private void addToKnapsack(Item item) {

        Bag bestFit = null;

        for (int i = 0; i < bags.length; i++) {
            if (bags[i].availableSpace() >= item.getWeight()) {

                if (bestFit == null) {
                    bestFit = bags[i];

                } else if (bags[i].availableSpace() <= bestFit.availableSpace()) {
                    bestFit = bags[i];

                }
            }

        }

        if (bestFit != null) {
            bestFit.addItem(item);

        } else {
            unusedItems.add(item);

        }

        //Choose first available knapsack - Worse solution
        /*boolean found = false;
        int currentBag = 0;

        while (!found && currentBag < bags.length) {

            if (bags[currentBag].availableSpace() > item.getWeight()) {
                bags[currentBag].addItem(item);
                found = true;

            }

            currentBag++;

        }

        if(!found) {
            unusedItems.add(item);
        }*/


    }

    public void printResult() {

        System.out.println("Knapsacks: ");

        for (int i = 0; i < bags.length; i++) {
            System.out.println();

            System.out.println("Capacity " + bags[i].getCapacity());
            System.out.println("Space left " + bags[i].availableSpace());
            System.out.println("Number of items " + bags[i].getItems().size());

        }

    }

    public static void main(String[] args) {

        int nbrOfItems = 15;
        int nbrOfBags = 2;

        Bag[] bags;
        Item[] items;

        Samples samples = new Samples();

        samples.generateRandomValues(nbrOfItems, nbrOfBags);
        bags = samples.getBags();
        items = samples.getItems();

        Greedy greedy = new Greedy(items, bags);
        greedy.solve();
        greedy.printResult();


    }

    public Bag[] getBags() {
        return bags;
    }

    public ArrayList<Item> getUnusedItems() {
        return unusedItems;
    }
}
