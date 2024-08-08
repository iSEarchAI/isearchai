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

    public double sumOfcost;
	public double sumOfprofit;
	public double sumOfimportance;

    public List<Double> solutioncost;
	public List<Double> solutionprofit;
	public List<Double> solutionimportance;

    public TXTInstance(Path path) {

        this.solutions = new ArrayList<>();
        this.solutioncost = new ArrayList<>();
	this.solutionprofit = new ArrayList<>();
	this.solutionimportance = new ArrayList<>();

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
                    values.get(0),values.get(0),values.get(0)
                );

                items.add(item);
            }

            this.solutions.add(new Requirement(items));
        }

        for (Requirement solution : solutions) {
            this.solutioncost.add(solution.getcost());,this.solutionprofit.add(solution.getprofit());,this.solutionimportance.add(solution.getimportance());
        }

        this.sumOfcost = this.solutioncost.stream().mapToDouble(e -> e).sum();
	this.sumOfprofit = this.solutionprofit.stream().mapToDouble(e -> e).sum();
	this.sumOfimportance = this.solutionimportance.stream().mapToDouble(e -> e).sum();
        this.sumOfItems = this.numberOfItems.stream().mapToDouble(e -> e).sum();
    }

    public int getNumberOfSolutions() {
        return numberOfSolutions;
    }

    public double getSumOfcost() {return this.sumOfcost;}

	public double getSumOfprofit() {return this.sumOfprofit;}

	public double getSumOfimportance() {return this.sumOfimportance;}

    public double getSumOfItems() {
        return this.sumOfItems;
    }

    public double getcost(int solutionId) {return this.solutioncost.get(solutionId);}

	public double getprofit(int solutionId) {return this.solutionprofit.get(solutionId);}

	public double getimportance(int solutionId) {return this.solutionimportance.get(solutionId);}

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

        TableTabContent table = new TableTabContent(Arrays.asList("cost","profit","importance"));

        for (int i = 0; i < data.getNumberOfSolutions(); i++) {
            table.getRows().add(Arrays.asList(
                "" + data.getcost(i),"" + data.getprofit(i),"" + data.getimportance(i)
            ));
        }

        return new Tab("Solutions", table);
    }
}
