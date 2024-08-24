package br.otimizes.isearchai.learning.encoding.integer;

import org.nautilus.core.encoding.NProblem;
import org.nautilus.core.model.Instance;
import org.nautilus.core.objective.AbstractObjective;

import java.util.List;

public class MLIntegerProblem extends NProblem<MLIntegerSolution> {

    private static final long serialVersionUID = 1234593199794358192L;

    private List<Integer> lowerBounds;

    private List<Integer> upperBounds;

    public MLIntegerProblem(Instance instance, List<AbstractObjective> objectives) {
        super(instance, objectives);
    }

    public Integer getUpperBound(int index) {
        return upperBounds.get(index);
    }

    public Integer getLowerBound(int index) {
        return lowerBounds.get(index);
    }

    protected void setLowerBounds(List<Integer> lowerBounds) {
        this.lowerBounds = lowerBounds;
    }

    protected void setUpperBounds(List<Integer> upperBounds) {
        this.upperBounds = upperBounds;
    }

    protected List<Integer> getLowerBounds() {
        return lowerBounds;
    }

    protected List<Integer> getUpperBounds() {
        return upperBounds;
    }

    @Override
    public void evaluate(MLIntegerSolution solution) {
        for (int i = 0; i < objectives.size(); i++) {
            solution.setObjective(i, objectives.get(i).evaluate(instance, solution));
        }
    }

    @Override
    public MLIntegerSolution createSolution() {
        return new MLIntegerSolution(
            getNumberOfObjectives(),
            getNumberOfVariables(),
            getLowerBounds(),
            getUpperBounds()
        );
    }
}
