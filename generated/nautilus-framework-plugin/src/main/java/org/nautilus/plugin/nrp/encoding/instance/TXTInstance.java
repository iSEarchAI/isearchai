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

    protected int sumOfSolution;

    protected double sumOfItem;

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
        this.sumOfSolution = reader.readIntegerValue();

        reader.ignoreLine();
        this.numberOfItems = reader.readIntegerValues();

        for (int i = 0; i < sumOfSolution; i++) {

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
            this.solutioncost.add(solution.getCost());
			this.solutionprofit.add(solution.getProfit());
			this.solutionimportance.add(solution.getImportance());
			
        }

        this.sumOfcost = this.solutioncost.stream().mapToDouble(e -> e).sum();
	this.sumOfprofit = this.solutionprofit.stream().mapToDouble(e -> e).sum();
	this.sumOfimportance = this.solutionimportance.stream().mapToDouble(e -> e).sum();
        this.sumOfItem = this.numberOfItems.stream().mapToDouble(e -> e).sum();
    }

    public int getSumOfSolution() {
        return sumOfSolution;
    }

    public double getSumOfCost() {return this.sumOfcost;}

	public double getSumOfProfit() {return this.sumOfprofit;}

	public double getSumOfImportance() {return this.sumOfimportance;}

    public double getSumOfItem() {
        return this.sumOfItem;
    }

    public double getcost(int solutionId) {return this.solutioncost.get(solutionId);}

	public double getprofit(int solutionId) {return this.solutionprofit.get(solutionId);}

	public double getimportance(int solutionId) {return this.solutionimportance.get(solutionId);}

    public List<Task> getTasks(int solutionId) {
        return this.solutions.get(solutionId).items;
    }

    public Requirement getSolution(int index) {
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

        for (int i = 0; i < data.getSumOfSolution(); i++) {
            table.getRows().add(Arrays.asList(
                "" + data.getcost(i),"" + data.getprofit(i),"" + data.getimportance(i)
            ));
        }

        return new Tab("Solutions", table);
    }
}
