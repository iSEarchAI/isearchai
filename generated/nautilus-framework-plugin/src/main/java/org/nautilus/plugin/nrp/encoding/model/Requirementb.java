package org.nautilus.plugin.nrp.encoding.model;

import java.util.ArrayList;
import java.util.List;

import org.nautilus.core.util.Converter;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

public class Requirementb {

    protected static JMetalRandom random = JMetalRandom.getInstance();

    public List<Taska> items;

    public Requirementb(List<Taska> items) {
        this.items = items;
    }

    public static Requirementb getRandom() {

        int numberOfTasks = JMetalRandom.getInstance().nextInt(1, 5);

        List<Task> items = new ArrayList<>();

        for(int i=0;i<numberOfTasks;i++) {
            items.add(Task.getRandom());
        }

        return new Requirementb(items);
    }

     public double getCost() {

        double sum = 0.0;

        for (Taska item : items) {
            sum += item.cost;
        }

        return sum;
    }

	 public double getProfit() {

        double sum = 0.0;

        for (Taska item : items) {
            sum += item.profit;
        }

        return sum;
    }

	 public double getImportance() {

        double sum = 0.0;

        for (Taska item : items) {
            sum += item.importance;
        }

        return sum;
    }

	

    public String toString() {
        return Converter.toJson(this);
    }
}
