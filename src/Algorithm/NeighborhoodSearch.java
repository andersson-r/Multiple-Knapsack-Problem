package Algorithm;

import Model.Bag;
import Model.Item;
import Model.Neighborhood;
import Utils.Samples;

import java.util.ArrayList;
import java.util.Arrays;

public class NeighborhoodSearch {
    Bag[] bags;
    ArrayList<Item> items;                  //Dynamic so we can use the list for storing an retrieving unused items
    Neighborhood globalOptima;
    ArrayList<Neighborhood> closestNeighboors;

    Greedy greedy;

    public NeighborhoodSearch() {
        Samples sample = new Samples();
        sample.generateRandomValues(100, 2);
        this.bags = sample.getBags();
        this.items = new ArrayList<Item>(Arrays.asList(sample.getItems()));
    }

    /**
     * Main method of the neighborhood search
     */
    public void search(){
        greedy = new Greedy(items.toArray(new Item[items.size()]), bags);
        greedy.solve();                                             //Starting solution solved using greedy algorithm
        Neighborhood initial = new Neighborhood(greedy.getItems(), greedy.getBags()); //Starting point
        globalOptima = initial;     //First solution has to be the best one so far

        closestNeighboors = generateNeighbours();
        System.out.println(initial.toString());
        rotateBags3(initial.getBags());
        System.out.println(initial.toString());
    }









    private ArrayList<Neighborhood> generateNeighbours(){
        ArrayList<Neighborhood> res = new ArrayList<>();
        //DO STUFF, ADD CRITERIA FOR GENERATING NEIGHBOURS

        return res;
    }





    //Utils - maybe move to a new class
    /**
     * Removes an item from a bag and puts it at the end of unused items-list
     * @param bag
     * @param item
     */
    private void removeFromBagToUnused(Bag bag, Item item){
        items.add(bag.removeItem(item));
    }
    /**
     * Moves an item from one bag to another
     * @param bagFrom
     * @param bagTo
     * @param item
     * @return
     */
    private boolean removeFromBagToBag(Bag bagFrom, Bag bagTo, Item item){
        if(bagTo.availableSpace() >= item.getWeight()){
            System.out.println("available space = "+ bagTo.availableSpace() + ", item weight: "+item.getWeight());
            bagTo.addItem(bagFrom.removeItem(item));
            return true;
        }
        return false;
    }

    /**
     * Adds the first item of the unused-items list to a specified bag.
     * @param bag
     * @return
     */
    private boolean addFromUnusedToBag(Bag bag){
        if(bag.availableSpace()>=items.get(0).getWeight()){
            bag.addItem(items.remove(0));
            return true;
        }
        return false;
    }

    /**
     * Rotate 3 items between all bags, requires at lest 2 bags
     * @param bags
     */
    private boolean rotateBags3(Bag[] bags) {
        if(bags.length==2){
            Bag bag1 = bags[0], bag2 = bags[1];
            for(int i = 0; i<3;i++)
                removeFromBagToBag(bag1, bag2, bag1.getFirstItem());
            for(int i = 0; i<3;i++)
                removeFromBagToBag(bag2, bag1, bag2.getFirstItem());
        }else if(bags.length>=2){
            //Handle the first bag to swap with the last bag (in list)
            Bag bag1 = bags[0], bag2 = bags[bags.length-1];
            for(int i = 0; i<3;i++)
                removeFromBagToBag(bag1, bag2, bag1.getFirstItem());
            for(int i = 0; i<3;i++)
                removeFromBagToBag(bag2, bag1, bag2.getFirstItem());
            //Handle the rest of the bags
            for(int j = 1; j<bags.length; j++){
                bag1 = bags[j];
                bag2 = bags[j-1];
                for(int i = 0; i<3;i++)
                    removeFromBagToBag(bag1, bag2, bag1.getFirstItem());
                for(int i = 0; i<3;i++)
                    removeFromBagToBag(bag2, bag1, bag2.getFirstItem());
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
