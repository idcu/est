package ltd.idcu.est.collection.api;

public interface MutableIntCollection extends IntCollection {

    MutableIntCollection addItem(int element);

    MutableIntCollection addAllItems(int[] elements);

    MutableIntCollection removeItem(int element);

    MutableIntCollection removeAllItems(int[] elements);

    MutableIntCollection clear();

    MutableIntCollection sortItems();

    MutableIntCollection shuffleItems();

    MutableIntCollection shuffleItems(java.util.Random random);
}
