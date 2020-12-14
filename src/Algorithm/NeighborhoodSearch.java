package Algorithm;

import Model.Bag;
import Model.Item;
import Model.Neighborhood;
import Utils.Samples;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Performs a Neighborhood search on a given problem set of Items and Bags(Knapsacks) by starting with a Greedy search,
 * then performs three types of rotations (1-step, 2-step and 3-step) to generate equally many neighbours. Each Neighbour is then
 * represented by a Neighborhood class, which will be evaluated. Each rotation type is also equally divided into rotating
 * between bags(knapsacks) and rotating between a bag and the unused-items list.
 */
public class NeighborhoodSearch {
    Bag[] bags;
    ArrayList<Item> items;                          //Dynamic so we can use the list for storing an retrieving unused items
    Neighborhood globalOptima;
    ArrayList<Neighborhood> closestNeighbours;
    static final int SEARCHES = 100;                  //amount of searches to perform before termination
    Greedy greedy;

    public NeighborhoodSearch() {
        Samples sample = new Samples();
        sample.generateRandomValues(100, 2);
        this.bags = sample.getBags();
        this.items = new ArrayList<>(Arrays.asList(sample.getItems()));
    }

    /**
     * Main method of the neighborhood search
     */
    public void search(){
        greedy = new Greedy(items.toArray(new Item[items.size()]), bags);
        greedy.solve();                           //Starting solution solved using greedy algorithm
        System.out.println("Nbr of unused items from greedy solution: " + greedy.getUnusedItems().size());
        Neighborhood initial = new Neighborhood(greedy.getUnusedItems(), bags); //Starting point
        globalOptima = initial;                    //First solution has to be the best one so far
        items = greedy.getUnusedItems();

        Neighborhood highestNeighbour = null;
        Neighborhood temp;

        System.out.println("before: " + initial.toString());

        //Amount of searches, outer loop
        for (int i = 0; i <= SEARCHES; i++) {
            closestNeighbours = new ArrayList<>();          //generate the neighborhood

            temp = new Neighborhood(items, bags);     //generate a neighbour for each rotation type
            rotateBags(temp.getBags(),1);
            closestNeighbours.add(temp);

            temp = new Neighborhood(items, bags);
            rotateBags(temp.getBags(),2);
            closestNeighbours.add(temp);

            temp = new Neighborhood(items, bags);
            rotateBags(temp.getBags(),3);
            closestNeighbours.add(temp);

            temp = new Neighborhood(items, bags);
            rotateUnused(temp.getBags(), temp.getUnusedItems(), 1);
            closestNeighbours.add(temp);

            temp = new Neighborhood(items, bags);
            rotateUnused(temp.getBags(), temp.getUnusedItems(), 2);
            closestNeighbours.add(temp);

            temp = new Neighborhood(items, bags);
            rotateUnused(temp.getBags(), temp.getUnusedItems(), 3);
            closestNeighbours.add(temp);


            highestNeighbour= closestNeighbours.get(0);      //initial neighbour to compare to

            for (int j = 0; j < closestNeighbours.size(); j++) {    //After generating neighborhood, compare which has highest value

                // System.out.println("closest " + closestNeighboors.get(j).getTotalValue() + "vs " + highestNeighbour.getTotalValue());

                if (closestNeighbours.get(j).getTotalValue() >= highestNeighbour.getTotalValue()) {
                    highestNeighbour= closestNeighbours.get(j);

                    bags = highestNeighbour.getBags();
                    items = highestNeighbour.getUnusedItems();


                }
            }
            System.out.println("\nHighest neighbour in search nbr: " + i +"\n"+highestNeighbour.toString());

            if (highestNeighbour.getTotalValue()>globalOptima.getTotalValue()) {
                System.out.println(highestNeighbour.getTotalValue() + "is higher than global - " + globalOptima.getTotalValue());
                globalOptima=highestNeighbour;                      //set new global optima if the local optima is > than previous global

            }

            closestNeighbours.clear();


        }
        System.out.println("\n\n\n----------------------------------------------------------");
        System.out.println("initial result:\n" + initial.toString());
        System.out.println("after neighborhoodsearch:\n" + globalOptima.toString());


    }




    //Utils - maybe move to a new class
    /**
     * Removes an item from a bag and puts it at the end of unused items-list
     * @param bag
     * @param item
     */
    private void removeFromBagToUnused(Bag bag, Item item, ArrayList<Item> unused){
        unused.add(bag.removeItem(item));
    }

    /**
     * Swaps items between bags by first attempting 30 times to remove
     * an random item in the first bag to insert it to the second bag by
     * also removing a random item from that bag (2nd bag). If 30 attempts
     * passed and no swap is made, method ends. If a swap is made, we try
     * to take the removed item from the 2nd bag and insert in the first bag
     * (in order to swap between the two bags) if that cant be made due to lack of
     * space, we will attempt to fit an item from the unused items-list to the first bag.
     * @param bagFrom
     * @param bagTo
     * @return
     */
    private boolean moveFromBagToBag(Bag bagFrom, Bag bagTo){
        for (int i = 0; i < 30; i++) {
            Item itemToAdd = bagFrom.getRandomItem();
            Item itemToRemove = bagTo.getRandomItem();
            Item temp = null;
            if ((bagTo.availableSpace() + itemToRemove.getWeight()) >= itemToAdd.getWeight()) {
                temp = bagTo.removeItem(itemToRemove);
                bagTo.addItem(itemToAdd);
                bagFrom.removeItem(itemToAdd);
                //try to move the item removed from first bag to the second bag
                if(bagFrom.availableSpace()>=temp.getWeight())
                    bagFrom.addItem(temp);
                else {
                    bagTo.getItems().add(temp); //we have to put it into the unused list if we cant fit it into the other bag
                    addFromUnusedToBag(bagFrom, bagFrom.getItems());
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Adds the first item of the unused-items list to a specified bag. If it doesnt fit,
     * it tries the whole list.
     * @param bag
     * @return
     */
    private boolean addFromUnusedToBag(Bag bag, ArrayList<Item> unused){
        for (int i = 0; i < unused.size(); i++) {
            if(bag.availableSpace()>=unused.get(i).getWeight()){
                bag.addItem(unused.remove(i));
                return true;
            }
        }
        return false;
    }

    //ROTATIONS
    /**
     * Rotate x items between all bags, requires at lest 2 bags
     * if an item doesnt fit, it will just take the next one in list until it goes through the whole list
     * if no item fits, it skips.
     * @param bags
     */
    private boolean rotateBags(Bag[] bags, int times) {
        if(bags.length==2){
            Bag bag1 = bags[0], bag2 = bags[1];
            for(int i = 0; i<times;i++)
                moveFromBagToBag(bag1, bag2);
        }else if(bags.length>=2){
            //Handle the first bag to swap with the last bag (in list)
            Bag bag1 = bags[0], bag2 = bags[bags.length-1];
            for(int i = 0; i<times;i++)
                moveFromBagToBag(bag1, bag2);
            //Handle the rest of the bags
            for(int j = 1; j<bags.length; j++){
                bag1 = bags[j];
                bag2 = bags[j-1];
                for(int i = 0; i<times;i++)
                    moveFromBagToBag(bag1, bag2);
            }

        }
        return false;
    }

    /**
     * Removes first item in bag and places it last on the unused-items list, then take
     * the first item in the unused-items list, if the items doesnt fit it just skips
     * @param bags
     * @return
     */
    private boolean rotateUnused(Bag[] bags, ArrayList<Item> unused, int times){
        for(int i = 0; i<bags.length; i++){
            for(int j=0; j<times; j++){
                removeFromBagToUnused(bags[i], bags[i].getRandomItem(), unused);
                addFromUnusedToBag(bags[i], unused);
            }
        }
        return false;
    }



    //For testing purposes
    public static void main(String[] args) {
        NeighborhoodSearch ns = new NeighborhoodSearch();
        ns.search();
    }


}
