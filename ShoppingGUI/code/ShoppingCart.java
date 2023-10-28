package code;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private List<String> items = new ArrayList<>();
    private List<Integer> quantities = new ArrayList<>();
    private List<Double> prices = new ArrayList<>();

    public void add(String item, int quantity, double price) {
        items.add(item);
        quantities.add(quantity);
        prices.add(price);
    }

    public void remove(int index) {
        items.remove(index);
        quantities.remove(index);
        prices.remove(index);
    }

    public void clear() {
        items.clear();
        quantities.clear();
        prices.clear();
    }

    public double sum() {
        double total = 0;
        for (int i = 0; i < prices.size(); i++) {
            total += prices.get(i) * quantities.get(i);
        }
        return total;
    }

    public List<String> getItems() {
        return items;
    }
}
