package br.otimizes.isearchai.learning.encoding.integer;

import br.otimizes.isearchai.learning.ml.interfaces.MLSolution;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import org.nautilus.core.encoding.NSolution;
import org.nautilus.core.encoding.solution.NIntegerSolution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MLIntegerSolution extends NSolution<MLInteger> implements MLSolution<MLInteger> {

    private int userEvaluation;
    private Boolean clusterNoise;
    public Boolean evaluatedByUser;
    private Double clusterId;

    private static final long serialVersionUID = 8545681239007623730L;

    protected List<Integer> lowerBounds;

    protected List<Integer> upperBounds;

    public MLIntegerSolution() {
        super();

        this.lowerBounds = new ArrayList<>();
        this.upperBounds = new ArrayList<>();
    }

    /**
     * Constructor
     *
     * @param numberOfObjectives The number of objectives
     * @param numberOfVariables  The number of variables
     */
    public MLIntegerSolution(
        int numberOfObjectives,
        int numberOfVariables,
        List<Integer> lowerBounds,
        List<Integer> upperBounds) {
        super(numberOfObjectives, numberOfVariables);

        Preconditions.checkNotNull(lowerBounds, "The lower bounds should not be null");
        Preconditions.checkNotNull(upperBounds, "The upper bounds should not be null");
        Preconditions.checkArgument(lowerBounds.size() == numberOfVariables, "The lower bounds shound have the same number of variables");
        Preconditions.checkArgument(upperBounds.size() == numberOfVariables, "The upper bounds shound have the same number of variables");

        this.lowerBounds = lowerBounds;
        this.upperBounds = upperBounds;

        initializeDoubleVariables();
    }

    public MLIntegerSolution(int numberOfObjectives, int numberOfVariables) {
        this(numberOfObjectives,
            numberOfVariables,
            Collections.nCopies(numberOfVariables, 0),
            Collections.nCopies(numberOfVariables, 1));
    }

    /**
     * Copy constructor
     */
    public MLIntegerSolution(NIntegerSolution solution) {
        super(solution.getNumberOfObjectives(), solution.getNumberOfVariables());

        setLowerBounds(new ArrayList<>(solution.getLowerBounds()));
        setUpperBounds(new ArrayList<>(solution.getUpperBounds()));

        for (int i = 0; i < solution.getNumberOfObjectives(); i++) {
            setObjective(i, solution.getObjective(i));
        }

        for (int i = 0; i < solution.getNumberOfVariables(); i++) {
            setVariableValue(i, new MLInteger(solution.getVariableValue(i)));
        }

        setAttributes(new HashMap<Object, Object>(solution.getAttributes()));
    }

    protected void initializeDoubleVariables() {

        for (int i = 0; i < getNumberOfVariables(); i++) {
            int v = JMetalRandom.getInstance().nextInt(getLowerBound(i), getUpperBound(i));
            setVariableValue(i, new MLInteger(v));
        }
    }

    public Integer getLowerBound(int index) {
        return lowerBounds.get(index);
    }

    public Integer getUpperBound(int index) {
        return upperBounds.get(index);
    }

    @Override
    public MLIntegerSolution copy() {
//        return new MLDoubleSolution(this.getNumberOfObjectives(), this.getNumberOfVariables());
        return null;
    }

    public List<Integer> getLowerBounds() {
        return lowerBounds;
    }

    public void setLowerBounds(List<Integer> lowerBounds) {
        this.lowerBounds = lowerBounds;
    }

    public List<Integer> getUpperBounds() {
        return upperBounds;
    }

    public void setUpperBounds(List<Integer> upperBounds) {
        this.upperBounds = upperBounds;
    }

    public void setLowerBound(int index, Integer value) {
        this.lowerBounds.set(index, value);
    }

    public void setUpperBound(int index, Integer value) {
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
    public List<MLInteger> getElements() {
        return this.variables;
    }

    @Override
    public boolean containsElementsEvaluation() {
        return this.variables.stream().anyMatch(MLInteger::isFreezeByDM);
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
