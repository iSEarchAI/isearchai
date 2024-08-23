package org.nautilus.plugin.nrp.encoding.model;

import java.util.ArrayList;
import java.util.List;

import org.nautilus.core.util.Converter;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

public class Requirement {

    protected static JMetalRandom random = JMetalRandom.getInstance();

    public List<Task> items;

    public Requirement(List<Task> items) {
        this.items = items;
    }

    public static Requirement getRandom() {

        int numberOfTasks = JMetalRandom.getInstance().nextInt(1, 5);

        List<Task> items = new ArrayList<>();

        for(int i=0;i<numberOfTasks;i++) {
            items.add(Task.getRandom());
        }

        return new Requirement(items);
    }

     public double getCost() {

        double sum = 0.0;

        for (Task item : items) {
            sum += item.cost;
        }

        return sum;
    }

	 public double getProfit() {

        double sum = 0.0;

        for (Task item : items) {
            sum += item.profit;
        }

        return sum;
    }

	 public double getImportance() {

        double sum = 0.0;

        for (Task item : items) {
            sum += item.importance;
        }

        return sum;
    }

	

    public String toString() {
        return Converter.toJson(this);
    }
}
