package br.otimizes.isearchai.learning.encoding.doubl;

import org.nautilus.core.encoding.NProblem;
import org.nautilus.core.model.Instance;
import org.nautilus.core.objective.AbstractObjective;

import java.util.List;

public class MLDoubleProblem extends NProblem<MLDoubleSolution> {

    private static final long serialVersionUID = 1234593199794358192L;

    private List<Double> lowerBounds;

    private List<Double> upperBounds;

    public MLDoubleProblem(Instance instance, List<AbstractObjective> objectives) {
        super(instance, objectives);
    }

    public Double getUpperBound(int index) {
        return upperBounds.get(index);
    }

    public Double getLowerBound(int index) {
        return lowerBounds.get(index);
    }

    protected void setLowerBounds(List<Double> lowerBounds) {
        this.lowerBounds = lowerBounds;
    }

    protected void setUpperBounds(List<Double> upperBounds) {
        this.upperBounds = upperBounds;
    }

    protected List<Double> getLowerBounds() {
        return lowerBounds;
    }

    protected List<Double> getUpperBounds() {
        return upperBounds;
    }

    @Override
    public void evaluate(MLDoubleSolution solution) {
        for (int i = 0; i < objectives.size(); i++) {
            solution.setObjective(i, objectives.get(i).evaluate(instance, solution));
        }
    }

    @Override
    public MLDoubleSolution createSolution() {
        return new MLDoubleSolution(
            getNumberOfObjectives(),
            getNumberOfVariables(),
            getLowerBounds(),
            getUpperBounds()
        );
    }
}
