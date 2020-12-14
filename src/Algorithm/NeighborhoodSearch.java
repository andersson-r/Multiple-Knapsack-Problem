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
    ArrayList<Neighborhood> closestNeighboors;
    static final int NEIGHBOURS_BY_6 = 100;       //amount of each rotation type, actual neighbours will be 6x the amount
    static final int SEARCHES = 100;                  //amount of searches to perform before termination
    Greedy greedy;

    public NeighborhoodSearch() {
        Samples sample = new Samples();
        sample.generateRandomValues(2000, 5);
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

        //Amount of searches, outer loop
        for (int i = 0; i <= SEARCHES; i++) {
            closestNeighboors = new ArrayList<>();
            for (int j = 0; j < NEIGHBOURS_BY_6; j++) { //generate the neighborhood
                temp = new Neighborhood(items, bags);     //generate a neighbour for each rotation type (4 types currently)
                rotateBags1(temp.getBags());
                closestNeighboors.add(temp);

                temp = new Neighborhood(items, bags);
                rotateBags2(temp.getBags());
                closestNeighboors.add(temp);

                temp = new Neighborhood(items, bags);
                rotateBags3(temp.getBags());
                closestNeighboors.add(temp);

                temp = new Neighborhood(items, bags);
                rotateUnused1(temp.getBags(), temp.getUnusedItems());
                closestNeighboors.add(temp);

                temp = new Neighborhood(items, bags);
                rotateUnused2(temp.getBags(), temp.getUnusedItems());
                closestNeighboors.add(temp);

                temp = new Neighborhood(items, bags);
                rotateUnused3(temp.getBags(), temp.getUnusedItems());
                closestNeighboors.add(temp);

            }

            highestNeighbour=closestNeighboors.get(0);      //initial neighbour to compare to

            for (int j = 0; j < closestNeighboors.size(); j++) {    //After generating neighborhood, compare which has highest value

                // System.out.println("closest " + closestNeighboors.get(j).getTotalValue() + "vs " + highestNeighbour.getTotalValue());

                if (closestNeighboors.get(j).getTotalValue() >= highestNeighbour.getTotalValue()) {
                    highestNeighbour=closestNeighboors.get(j);

                    bags = highestNeighbour.getBags();
                    items = highestNeighbour.getUnusedItems();



                }
            }
            System.out.println("\nHighest neighbour in search nbr: " + i +"\n"+highestNeighbour.toString());

            if (highestNeighbour.getTotalValue()>globalOptima.getTotalValue()) {
                System.out.println(highestNeighbour.getTotalValue() + "is higher than global - " + globalOptima.getTotalValue());
                globalOptima=highestNeighbour;                      //set new global optima if the local optima is > than previous global

            }

            closestNeighboors.clear();


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
     * Moves an item from one bag to another by first removing
     * an item from the bag to the unused list in order to make
     * space for the item. If the desired item doesnt fit, we try
     * to put in an item from the unused bag.
     * @param bagFrom
     * @param bagTo
     * @param item
     * @return
     */
    private boolean moveFromBagToBag(Bag bagFrom, Bag bagTo, Item item){
        removeFromBagToUnused(bagTo, bagTo.getFirstItem(), bagTo.getItems());
        if(bagTo.availableSpace() >= item.getWeight()){
            bagTo.addItem(bagFrom.removeItem(item));
            return true;
        }
        addFromUnusedToBag(bagTo, bagTo.getItems());
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

    //THREE-ROTATIONS
    /**
     * Rotate 3 items between all bags, requires at lest 2 bags
     * if an item doesnt fit, it will just take the next one in list until it goes through the whole list
     * if no item fits, it skips.
     * @param bags
     */
    private boolean rotateBags3(Bag[] bags) {
        if(bags.length==2){
            Bag bag1 = bags[0], bag2 = bags[1];
            for(int i = 0; i<3;i++)
                moveFromBagToBag(bag1, bag2, bag1.getFirstItem());
            for(int i = 0; i<3;i++)
                moveFromBagToBag(bag2, bag1, bag2.getFirstItem());
        }else if(bags.length>=2){
            //Handle the first bag to swap with the last bag (in list)
            Bag bag1 = bags[0], bag2 = bags[bags.length-1];
            for(int i = 0; i<3;i++)
                moveFromBagToBag(bag1, bag2, bag1.getFirstItem());
            for(int i = 0; i<3;i++)
                moveFromBagToBag(bag2, bag1, bag2.getFirstItem());
            //Handle the rest of the bags
            for(int j = 1; j<bags.length; j++){
                bag1 = bags[j];
                bag2 = bags[j-1];
                for(int i = 0; i<3;i++)
                    moveFromBagToBag(bag1, bag2, bag1.getFirstItem());
                for(int i = 0; i<3;i++)
                    moveFromBagToBag(bag2, bag1, bag2.getFirstItem());
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
    private boolean rotateUnused3(Bag[] bags, ArrayList<Item> unused){
        for(int i = 0; i<bags.length; i++){
            for(int j=0; j<3; j++){
                removeFromBagToUnused(bags[i], bags[i].getFirstItem(), unused);
                addFromUnusedToBag(bags[i], unused);
            }
        }
        return false;
    }

    //TWO-ROTATIONS

    /**
     * Rotate 3 items between all bags, requires at lest 2 bags
     * if an item doesnt fit, it will just take the next one in list until it goes through the whole list
     * if no item fits, it skips.
     * @param bags
     */
    private boolean rotateBags2(Bag[] bags) {
        if(bags.length==2){
            Bag bag1 = bags[0], bag2 = bags[1];
            for(int i = 0; i<2;i++)
                moveFromBagToBag(bag1, bag2, bag1.getFirstItem());
            for(int i = 0; i<2;i++)
                moveFromBagToBag(bag2, bag1, bag2.getFirstItem());
        }else if(bags.length>=2){
            //Handle the first bag to swap with the last bag (in list)
            Bag bag1 = bags[0], bag2 = bags[bags.length-1];
            for(int i = 0; i<2;i++)
                moveFromBagToBag(bag1, bag2, bag1.getFirstItem());
            for(int i = 0; i<2;i++)
                moveFromBagToBag(bag2, bag1, bag2.getFirstItem());
            //Handle the rest of the bags
            for(int j = 1; j<bags.length; j++){
                bag1 = bags[j];
                bag2 = bags[j-1];
                for(int i = 0; i<2;i++)
                    moveFromBagToBag(bag1, bag2, bag1.getFirstItem());
                for(int i = 0; i<2;i++)
                    moveFromBagToBag(bag2, bag1, bag2.getFirstItem());
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
    private boolean rotateUnused2(Bag[] bags, ArrayList<Item> unused){
        for(int i = 0; i<bags.length; i++){
            for(int j=0; j<2; j++){
                removeFromBagToUnused(bags[i], bags[i].getFirstItem(), unused);
                addFromUnusedToBag(bags[i], unused);
            }
        }
        return false;
    }

    //ONE-ROTATIONS

    /**
     * Rotate 3 items between all bags, requires at lest 2 bags
     * if an item doesnt fit, it will just take the next one in list until it goes through the whole list
     * if no item fits, it skips.
     * @param bags
     */
    private boolean rotateBags1(Bag[] bags) {
        if(bags.length==2){
            Bag bag1 = bags[0], bag2 = bags[1];
            moveFromBagToBag(bag1, bag2, bag1.getFirstItem());
            moveFromBagToBag(bag2, bag1, bag2.getFirstItem());
        }else if(bags.length>=2){
            //Handle the first bag to swap with the last bag (in list)
            Bag bag1 = bags[0], bag2 = bags[bags.length-1];
            moveFromBagToBag(bag1, bag2, bag1.getFirstItem());
            moveFromBagToBag(bag2, bag1, bag2.getFirstItem());
            //Handle the rest of the bags
            for(int j = 1; j<bags.length; j++){
                bag1 = bags[j];
                bag2 = bags[j-1];
                moveFromBagToBag(bag1, bag2, bag1.getFirstItem());
                moveFromBagToBag(bag2, bag1, bag2.getFirstItem());
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
    private boolean rotateUnused1(Bag[] bags, ArrayList<Item> unused){
        for(int i = 0; i<bags.length; i++){
            removeFromBagToUnused(bags[i], bags[i].getFirstItem(), unused);
            addFromUnusedToBag(bags[i], unused);

        }
        return false;
    }


    //For testing purposes
    public static void main(String[] args) {
        NeighborhoodSearch ns = new NeighborhoodSearch();
        ns.search();
    }


}
