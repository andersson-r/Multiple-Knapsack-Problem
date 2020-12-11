package Model;

/**
 * A representation of the items, containing values and weights that will be used to fill the Bags with
 */
public class Item {
    int weight;
    int value;
    float benefit;

    public Item(int weight, int value) {
        this.weight = weight;
        this.value = value;

        benefit = value/weight;
    }

    public int getWeight() {
        return weight;
    }

    public int getValue() {
        return value;
    }

    public float getBenefit() {
        return benefit;
    }
}
