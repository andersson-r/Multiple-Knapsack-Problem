package Model;

/**
 * A representation of the items, containing values and weights that will be used to fill the Bags with
 */
public class Item {
    int weight;
    int value;

    public Item(int weight, int value) {
        this.weight = weight;
        this.value = value;
    }

    public int getWeight() {
        return weight;
    }

    public int getValue() {
        return value;
    }
}
