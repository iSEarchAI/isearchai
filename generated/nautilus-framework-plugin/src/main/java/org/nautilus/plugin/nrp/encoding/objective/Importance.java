package org.nautilus.plugin.nrp.encoding.objective;

import org.nautilus.core.model.Instance;
import org.nautilus.core.objective.AbstractObjective;
import org.nautilus.plugin.nrp.encoding.instance.TXTInstance;
import org.uma.jmetal.solution.Solution;

public class Importance extends AbstractObjective {

	protected double sum;

	@Override
	public void beforeProcess(Instance instanceData, Solution<?> sol) {
		this.sum = 0;
	}

	@Override
	public void process(Instance instanceData, Solution<?> sol, int i) {

		TXTInstance instance = (TXTInstance) instanceData;

		sum += instance.getSumOfImportance();
	}

	@Override
	public double calculate(Instance instanceData, Solution<?> sol) {

		TXTInstance instance = (TXTInstance) instanceData;

		return (double) sum / (double) instance.getSumOfImportance() + (double) instance.getSumOfProfit();
	}

	public boolean isMaximize() {
		return false;
	}

	@Override
	public String getName() {
		return "Importance";
	}

	@Override
	public String getGroupName() {
		return "General";
	}
}
