package org.nautilus.plugin.nrp.encoding.objective;

import org.nautilus.core.model.Instance;
import org.nautilus.core.objective.AbstractObjective;
import org.nautilus.plugin.nrp.encoding.instance.TXTInstance;
import org.uma.jmetal.solution.Solution;

public class Size extends AbstractObjective {

	protected int sum;

	@Override
	public void beforeProcess(Instance instanceData, Solution<?> sol) {
		this.sum = 0;
	}

	@Override
	public void process(Instance instanceData, Solution<?> sol, int i) {

		TXTInstance instance = (TXTInstance) instanceData;

		sum += 1;
	}

	@Override
	public double calculate(Instance instanceData, Solution<?> sol) {

		TXTInstance instance = (TXTInstance) instanceData;

		return (double) sum / (double) instance.getSumOfRequirement();
	}

	public boolean isMaximize() {
		return false;
	}

	@Override
	public String getName() {
		return "Size";
	}

	@Override
	public String getGroupName() {
		return "General";
	}
}
