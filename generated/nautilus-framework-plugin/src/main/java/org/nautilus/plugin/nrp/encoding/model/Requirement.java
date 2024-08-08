package org.nautilus.plugin.nrp.encoding.instance;

import org.nautilus.core.gui.Tab;
import org.nautilus.core.gui.TableTabContent;
import org.nautilus.core.model.Instance;
import org.nautilus.core.util.InstanceReader;
import org.nautilus.plugin.nrp.encoding.model.Requirement;
import org.nautilus.plugin.nrp.encoding.model.Task;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TXTInstance extends Instance {

    protected int numberOfSolutions;

    protected double sumOfItems;

    protected List<Integer> numberOfItems;

    protected List<Requirement> solutions;

    protected double sumOfCost;

    protected double sumOfProfit;

    protected double sumOfImportance;


    protected List<Double> requirementCost;

    protected List<Double> requirementProfit;

    protected List<Double> requirementImportance;

    public TXTInstance(Path path) {

        this.solutions = new ArrayList<>();
        this.requirementCost = new ArrayList<>();
        this.requirementProfit = new ArrayList<>();
        this.requirementImportance = new ArrayList<>();

        InstanceReader reader = new InstanceReader(path, " ");

        reader.ignoreLine();
        this.numberOfSolutions = reader.readIntegerValue();

        reader.ignoreLine();
        this.numberOfItems = reader.readIntegerValues();

        for (int i = 0; i < numberOfSolutions; i++) {

            reader.ignoreLine();

            List<Task> items = new ArrayList<>();

            for (int j = 0; j < numberOfItems.get(i); j++) {

                List<Double> values = reader.readDoubleValues();

                Task item = new Task(
                    values.get(0),
                    values.get(1),
                    values.get(2)
                );

                items.add(item);
            }

            this.solutions.add(new Requirement(items));
        }

        for (Requirement solution : solutions) {
            this.requirementCost.add(solution.getCost());
            this.requirementProfit.add(solution.getProfit());
            this.requirementImportance.add(solution.getImportance());
        }

        this.sumOfCost = this.requirementCost.stream().mapToDouble(e -> e).sum();
        this.sumOfProfit = this.requirementProfit.stream().mapToDouble(e -> e).sum();
        this.sumOfImportance = this.requirementImportance.stream().mapToDouble(e -> e).sum();
        this.sumOfItems = this.numberOfItems.stream().mapToDouble(e -> e).sum();
    }

    public int getNumberOfSolutions() {
        return numberOfSolutions;
    }

    public double getSumOfCost() {
        return this.sumOfCost;
    }

    public double getSumOfProfit() {
        return this.sumOfProfit;
    }

    public double getSumOfImportance() {
        return this.sumOfImportance;
    }

    public double getSumOfItems() {
        return this.sumOfItems;
    }

    public double getCost(int solutionId) {
        return this.requirementCost.get(solutionId);
    }

    public double getProfit(int solutionId) {
        return this.requirementProfit.get(solutionId);
    }

    public double getImportance(int solutionId) {
        return this.requirementImportance.get(solutionId);
    }

    public List<Task> getTasks(int solutionId) {
        return this.solutions.get(solutionId).items;
    }

    public Requirement getRequirement(int index) {
        return this.solutions.get(index);
    }

    @Override
    public List<Tab> getTabs(Instance data) {

        TXTInstance c = (TXTInstance) data;

        List<Tab> tabs = new ArrayList<>();

        tabs.add(getSolutionsTab(c));

        return tabs;
    }

    protected Tab getSolutionsTab(TXTInstance data) {

        TableTabContent table = new TableTabContent(Arrays.asList("Cost", "Profit", "Importance"));

        for (int i = 0; i < data.getNumberOfSolutions(); i++) {
            table.getRows().add(Arrays.asList(
                "" + data.getCost(i),
                "" + data.getProfit(i),
                "" + data.getImportance(i)
            ));
        }

        return new Tab("Solutions", table);
    }
}
