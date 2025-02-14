package br.otimizes.isearchai.learning.encoding.doubl;

import br.otimizes.isearchai.core.MLSolution;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import org.nautilus.core.encoding.NSolution;
import org.nautilus.core.encoding.solution.NDoubleSolution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * The type Ml double solution.
 */
public class MLDoubleSolution extends NSolution<MLDouble> implements MLSolution<MLDouble, MLDouble> {

    private int userEvaluation;
    private Boolean clusterNoise;
    /**
     * The Evaluated by user.
     */
    public Boolean evaluatedByUser;
    private Double clusterId;

    private static final long serialVersionUID = 8545681239007623730L;

    /**
     * The Lower bounds.
     */
    protected List<Double> lowerBounds;

    /**
     * The Upper bounds.
     */
    protected List<Double> upperBounds;

    /**
     * Instantiates a new Ml double solution.
     */
    public MLDoubleSolution() {
        super();

        this.lowerBounds = new ArrayList<>();
        this.upperBounds = new ArrayList<>();
    }

    /**
     * Constructor
     *
     * @param numberOfObjectives The number of objectives
     * @param numberOfVariables  The number of variables
     * @param lowerBounds        the lower bounds
     * @param upperBounds        the upper bounds
     */
    public MLDoubleSolution(
        int numberOfObjectives,
        int numberOfVariables,
        List<Double> lowerBounds,
        List<Double> upperBounds) {
        super(numberOfObjectives, numberOfVariables);

        Preconditions.checkNotNull(lowerBounds, "The lower bounds should not be null");
        Preconditions.checkNotNull(upperBounds, "The upper bounds should not be null");
        Preconditions.checkArgument(lowerBounds.size() == numberOfVariables, "The lower bounds shound have the same number of variables");
        Preconditions.checkArgument(upperBounds.size() == numberOfVariables, "The upper bounds shound have the same number of variables");

        this.lowerBounds = lowerBounds;
        this.upperBounds = upperBounds;

        initializeDoubleVariables();
    }

    /**
     * Instantiates a new Ml double solution.
     *
     * @param numberOfObjectives the number of objectives
     * @param numberOfVariables  the number of variables
     */
    public MLDoubleSolution(int numberOfObjectives, int numberOfVariables) {
        this(numberOfObjectives,
            numberOfVariables,
            Collections.nCopies(numberOfVariables, 0.0),
            Collections.nCopies(numberOfVariables, 1.0));
    }

    /**
     * Copy constructor
     *
     * @param solution the solution
     */
    public MLDoubleSolution(NDoubleSolution solution) {
        super(solution.getNumberOfObjectives(), solution.getNumberOfVariables());

        setLowerBounds(new ArrayList<>(solution.getLowerBounds()));
        setUpperBounds(new ArrayList<>(solution.getUpperBounds()));

        for (int i = 0; i < solution.getNumberOfObjectives(); i++) {
            setObjective(i, solution.getObjective(i));
        }

        for (int i = 0; i < solution.getNumberOfVariables(); i++) {
            setVariableValue(i, new MLDouble(solution.getVariableValue(i)));
        }

        setAttributes(new HashMap<Object, Object>(solution.getAttributes()));
    }

    /**
     * Initialize double variables.
     */
    protected void initializeDoubleVariables() {

        for (int i = 0; i < getNumberOfVariables(); i++) {
            double v = JMetalRandom.getInstance().nextDouble(getLowerBound(i), getUpperBound(i));
            setVariableValue(i, new MLDouble(v));
        }
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
     * Gets upper bound.
     *
     * @param index the index
     * @return the upper bound
     */
    public Double getUpperBound(int index) {
        return upperBounds.get(index);
    }

    @Override
    public MLDoubleSolution copy() {
//        return new MLDoubleSolution(this.getNumberOfObjectives(), this.getNumberOfVariables());
        return null;
    }

    /**
     * Gets lower bounds.
     *
     * @return the lower bounds
     */
    public List<Double> getLowerBounds() {
        return lowerBounds;
    }

    /**
     * Sets lower bounds.
     *
     * @param lowerBounds the lower bounds
     */
    public void setLowerBounds(List<Double> lowerBounds) {
        this.lowerBounds = lowerBounds;
    }

    /**
     * Gets upper bounds.
     *
     * @return the upper bounds
     */
    public List<Double> getUpperBounds() {
        return upperBounds;
    }

    /**
     * Sets upper bounds.
     *
     * @param upperBounds the upper bounds
     */
    public void setUpperBounds(List<Double> upperBounds) {
        this.upperBounds = upperBounds;
    }

    /**
     * Sets lower bound.
     *
     * @param index the index
     * @param value the value
     */
    public void setLowerBound(int index, Double value) {
        this.lowerBounds.set(index, value);
    }

    /**
     * Sets upper bound.
     *
     * @param index the index
     * @param value the value
     */
    public void setUpperBound(int index, Double value) {
        this.upperBounds.set(index, value);
    }

    @Override
    public String getVariableValueString(int index) {
        return getVariableValue(index).toString();
    }

    public String toString() {
        return new Gson().toJson(this);
    }

    @Override
    public int numberOfObjectives() {
        return objectives.length;
    }

    @Override
    public void setClusterId(Double assignment) {
        this.clusterId = assignment;
    }

    @Override
    public void setClusterNoise(Boolean b) {
        this.clusterNoise = b;
    }

    @Override
    public Double getClusterId() {
        return this.clusterId;
    }

    @Override
    public void setEvaluation(int i) {
        this.userEvaluation = i;
    }

    @Override
    public List<MLDouble> getElements() {
        return this.variables;
    }

    @Override
    public boolean containsElementsEvaluation() {
        return this.variables.stream().anyMatch(MLDouble::isFreezeByDM);
    }

    @Override
    public int getEvaluation() {
        return this.userEvaluation;
    }

    @Override
    public boolean getEvaluatedByUser() {
        return evaluatedByUser != null && evaluatedByUser;
    }
}
