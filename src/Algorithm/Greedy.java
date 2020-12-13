package Algorithm;

import Model.Bag;
import Model.Item;
import Utils.Samples;

import java.util.ArrayList;

public class Greedy {
    private Bag[] bags;
    private ArrayList<Item> items = new ArrayList<Item>();

    public Greedy(Item[] items, Bag[] bags) {
        for(int i = 0; i < bags.length; i++) {
            System.out.println("Bag nbr "+ i + " cap: " + bags[i].getCapacity());
            System.out.println();
        }

        this.bags = bags;

        for (int i = 0; i < items.length; i++) {
            System.out.println("item nbr " + i + " weight: " + items[i].getWeight() + " value: " + items[i].getValue());

            this.items.add(items[i]);
        }
    }

    public void solve() {

        /*
        Selects "best" item to add based on calculated benefit (value/weight)
        If that item can't be added it is discarded.
        Solution is terminated when no items can be added or all items have already been added to the knapsacks.

         */
        while(items.size() != 0) {

            Item currentBest = items.get(0);
            for (Item item : items) {
                if (item.getBenefit() > currentBest.getBenefit()) {
                    currentBest = item;
                }
            }

            items.remove(currentBest);

            System.out.println("Currentbest W: " + currentBest.getWeight() + " V: " + currentBest.getValue() + " B: " + currentBest.getBenefit());
            addToKnapsack(currentBest);

        }

    }

    private void addToKnapsack(Item item) {

        //Choose first available knapsack
        /*boolean found = false;
        int currentBag = 0;

        while (!found) {

            if (bags[currentBag].getCurrentSize() > item.getWeight()) {
                bags[currentBag].setItems(item);
                found = true;

            }

        }
*/

        //Choose knapsack with least amount of remaining weight
        Bag bestFit = null;

        for (int i = 0; i < bags.length; i++) {
            if (bags[i].getCurrentSize() > item.getWeight()) {

                if (bestFit == null) {
                    bestFit = bags[i];

                } else if (bags[i].getCurrentSize() < bestFit.getCurrentSize()) {
                    bestFit = bags[i];

                }
            }

        }

        if(bestFit != null) {
            bestFit.addItem(item);

        }


    }

    public void printResult() {

        System.out.println("Knapsacks: ");

        for(int i = 0; i< bags.length; i++) {
            System.out.println();

            System.out.println("Capacity " + bags[i].getCapacity());
            System.out.println("Space left " + bags[i].getCurrentSize());
            System.out.println("Number of items " + bags[i].getItems().size());

        }

    }

    public static void main(String[] args) {

        Bag[] bags;
        Item[] items;

        Samples samples = new Samples();

        samples.generateRandomValues(15, 2);
        bags = samples.getBags();
        items = samples.getItems();

        Greedy greedy = new Greedy(items, bags);
        greedy.solve();
        greedy.printResult();




    }

    public Bag[] getBags() {
        return bags;
    }

    public ArrayList<Item> getItems() {
        return items;
    }
}
