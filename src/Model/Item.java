package Model;

import java.util.Objects;

/**
 * A representation of the items, containing values and weights that will be used to fill the Bags with
 */
public class Item implements Comparable<Item>{
    int weight;
    int value;
    float benefit;

    public Item(int weight, int value) {
        this.weight = weight;
        this.value = value;

        benefit = (float) value / (float) weight;
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

    @Override
    public int compareTo(Item o) {
        return 0;
    }


    @Override
    public int hashCode() {
        return Objects.hash(weight,value);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        Item other = (Item)o;
        if (!Objects.equals(this.weight, other.getWeight())) {
            return false;
        }
        if (!Objects.equals(this.value, other.getValue())) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "Item(w: "+this.weight +", v: "+this.value +", hash: "+this.hashCode()+")";
    }
}
