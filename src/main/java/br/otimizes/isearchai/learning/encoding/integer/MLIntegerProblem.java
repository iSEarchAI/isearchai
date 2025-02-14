package br.otimizes.isearchai.learning.encoding.integer;

import org.nautilus.core.encoding.NProblem;
import org.nautilus.core.model.Instance;
import org.nautilus.core.objective.AbstractObjective;

import java.util.List;

/**
 * The type Ml integer problem.
 */
public class MLIntegerProblem extends NProblem<MLIntegerSolution> {

    private static final long serialVersionUID = 1234593199794358192L;

    private List<Integer> lowerBounds;

    private List<Integer> upperBounds;

    /**
     * Instantiates a new Ml integer problem.
     *
     * @param instance   the instance
     * @param objectives the objectives
     */
    public MLIntegerProblem(Instance instance, List<AbstractObjective> objectives) {
        super(instance, objectives);
    }

    /**
     * Gets upper bound.
     *
     * @param index the index
     * @return the upper bound
     */
    public Integer getUpperBound(int index) {
        return upperBounds.get(index);
    }

    /**
     * Gets lower bound.
     *
     * @param index the index
     * @return the lower bound
     */
    public Integer getLowerBound(int index) {
        return lowerBounds.get(index);
    }

    /**
     * Sets lower bounds.
     *
     * @param lowerBounds the lower bounds
     */
    protected void setLowerBounds(List<Integer> lowerBounds) {
        this.lowerBounds = lowerBounds;
    }

    /**
     * Sets upper bounds.
     *
     * @param upperBounds the upper bounds
     */
    protected void setUpperBounds(List<Integer> upperBounds) {
        this.upperBounds = upperBounds;
    }

    /**
     * Gets lower bounds.
     *
     * @return the lower bounds
     */
    protected List<Integer> getLowerBounds() {
        return lowerBounds;
    }

    /**
     * Gets upper bounds.
     *
     * @return the upper bounds
     */
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
