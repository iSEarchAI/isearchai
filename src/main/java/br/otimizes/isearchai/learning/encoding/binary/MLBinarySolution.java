package br.otimizes.isearchai.learning.encoding.binary;

import br.otimizes.isearchai.learning.ml.interfaces.MLSolution;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import org.nautilus.core.encoding.NSolution;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MLBinarySolution extends NSolution<MLBinarySet> implements MLSolution<MLBinarySet> {
    private int userEvaluation;
    private Boolean clusterNoise;
    public Boolean evaluatedByUser;
    private Double clusterId;

    protected List<Integer> bitsPerVariable;

    public MLBinarySolution() {
        super();

        this.bitsPerVariable = new ArrayList<>();
    }

    /**
     * Copy constructor
     */
    public MLBinarySolution(MLBinarySolution solution) {
        super(solution.getNumberOfObjectives(), solution.getNumberOfVariables());

        for (int i = 0; i < solution.getNumberOfObjectives(); i++) {
            setObjective(i, solution.getObjective(i));
        }

        for (int i = 0; i < solution.getNumberOfVariables(); i++) {
            setVariableValue(i, (MLBinarySet) solution.getVariableValue(i).clone());
        }

        setBitsPerVariable(new ArrayList<>(solution.getBitsPerVariable()));
        setAttributes(new HashMap<Object, Object>(solution.getAttributes()));
    }

    /**
     * Constructor
     *
     * @param numberOfObjectives The number of objectives
     * @param numberOfVariables  The number of variables
     * @param bitsPerVariable
     */
    public MLBinarySolution(int numberOfObjectives, int numberOfVariables, List<Integer> bitsPerVariable) {
        super(numberOfObjectives, numberOfVariables);

        Preconditions.checkNotNull(bitsPerVariable, "The bits per variable should not be null");
        Preconditions.checkArgument(bitsPerVariable.size() == numberOfVariables, "The bits per variable shound have the same number of variables");

        this.bitsPerVariable = bitsPerVariable;

        initializeBinaryVariables();
    }

    /**
     * Constructor
     *
     * @param numberOfObjectives The number of objectives
     * @param numberOfVariables  The number of variables
     * @param bitsForVariables   The number of bits for all variables
     */
    public MLBinarySolution(int numberOfObjectives, int numberOfVariables, int bitsForVariables) {
        this(numberOfObjectives, numberOfVariables, Collections.nCopies(numberOfVariables, bitsForVariables));
    }

    @Override
    public Solution<MLBinarySet> copy() {
        return new MLBinarySolution(this);
    }

    public int getNumberOfBits(int index) {
        return getVariableValue(index).getBinarySetLength();
    }

    public int getTotalNumberOfBits() {

        int sum = 0;

        for (int i = 0; i < getNumberOfVariables(); i++) {
            sum += getNumberOfBits(i);
        }

        return sum;
    }

    public List<Integer> getBitsPerVariable() {
        return bitsPerVariable;
    }

    public void setBitsPerVariable(List<Integer> bitsPerVariable) {
        this.bitsPerVariable = bitsPerVariable;
    }

    protected void initializeBinaryVariables() {

        for (int i = 0; i < getNumberOfVariables(); i++) {
            setVariableValue(i, createNewBitSet(bitsPerVariable.get(i)));
        }
    }

    protected MLBinarySet createNewBitSet(int numberOfBits) {

        MLBinarySet bitSet = new MLBinarySet(numberOfBits);

        for (int i = 0; i < numberOfBits; i++) {

            if (JMetalRandom.getInstance().nextDouble() < 0.5) {
                bitSet.set(i);
            } else {
                bitSet.clear(i);
            }
        }

        return bitSet;
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
    public List<MLBinarySet> getElements() {
        return this.variables;
    }

    @Override
    public boolean containsElementsEvaluation() {
        return this.variables.stream().anyMatch(MLBinarySet::isFreezeByDM);
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
