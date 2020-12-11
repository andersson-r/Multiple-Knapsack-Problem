package Algorithm;

import Model.Bag;
import Model.Item;
import Utils.Samples;

public class NeighborhoodSearch {
    Bag[] bags;
    Item[] items;


    public NeighborhoodSearch() {
        Samples sample = new Samples();
        sample.generateRandomValues(100, 2);
        this.bags = sample.getBags();
        this.items = sample.getItems();
    }
}
