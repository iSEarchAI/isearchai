package br.otimizes.isearchai.learning.encoding.doubl;

import org.nautilus.core.encoding.NProblem;
import org.nautilus.core.model.Instance;
import org.nautilus.core.objective.AbstractObjective;

import java.util.List;

/**
 * The type Ml double problem.
 */
public class MLDoubleProblem extends NProblem<MLDoubleSolution> {

    private static final long serialVersionUID = 1234593199794358192L;

    private List<Double> lowerBounds;

    private List<Double> upperBounds;

    /**
     * Instantiates a new Ml double problem.
     *
     * @param instance   the instance
     * @param objectives the objectives
     */
    public MLDoubleProblem(Instance instance, List<AbstractObjective> objectives) {
        super(instance, objectives);
    }

    /**
     * Gets upper bound.
     *
     * @param index the index
     * @return the upper bound
     */
    public Double getUpperBound(int index) {
        return upperBounds.get(index);
    }

    /**
     * Gets lower bound.
     *
     * @param index the index
     * @return the lower bound
     */
    public Double getLowerBound(int index) {
        return lowerBounds.get(index);
    }

    /**
     * Sets lower bounds.
     *
     * @param lowerBounds the lower bounds
     */
    protected void setLowerBounds(List<Double> lowerBounds) {
        this.lowerBounds = lowerBounds;
    }

    /**
     * Sets upper bounds.
     *
     * @param upperBounds the upper bounds
     */
    protected void setUpperBounds(List<Double> upperBounds) {
        this.upperBounds = upperBounds;
    }

    /**
     * Gets lower bounds.
     *
     * @return the lower bounds
     */
    protected List<Double> getLowerBounds() {
        return lowerBounds;
    }

    /**
     * Gets upper bounds.
     *
     * @return the upper bounds
     */
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
