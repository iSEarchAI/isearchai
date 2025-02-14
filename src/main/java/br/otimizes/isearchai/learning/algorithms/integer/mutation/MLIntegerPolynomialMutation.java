package br.otimizes.isearchai.learning.algorithms.integer.mutation;

import br.otimizes.isearchai.learning.encoding.integer.MLInteger;
import br.otimizes.isearchai.learning.encoding.integer.MLIntegerSolution;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.solution.util.RepairDoubleSolution;
import org.uma.jmetal.solution.util.RepairDoubleSolutionAtBounds;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.pseudorandom.RandomGenerator;

/**
 * The type Ml integer polynomial mutation.
 */
public class MLIntegerPolynomialMutation implements MutationOperator<MLIntegerSolution> {
    private static final double DEFAULT_PROBABILITY = 0.01;
    private static final double DEFAULT_DISTRIBUTION_INDEX = 20.0;

    private double distributionIndex;
    private double mutationProbability;
    private RepairDoubleSolution solutionRepair;

    private RandomGenerator<Double> randomGenerator;

    /**
     * Constructor
     */
    public MLIntegerPolynomialMutation() {
        this(DEFAULT_PROBABILITY, DEFAULT_DISTRIBUTION_INDEX);
    }

    /**
     * Constructor
     *
     * @param problem           the problem
     * @param distributionIndex the distribution index
     */
    public MLIntegerPolynomialMutation(IntegerProblem problem, double distributionIndex) {
        this(1.0 / problem.getNumberOfVariables(), distributionIndex);
    }

    /**
     * Constructor
     *
     * @param mutationProbability the mutation probability
     * @param distributionIndex   the distribution index
     */
    public MLIntegerPolynomialMutation(double mutationProbability, double distributionIndex) {
        this(mutationProbability, distributionIndex, new RepairDoubleSolutionAtBounds());
    }

    /**
     * Constructor
     *
     * @param mutationProbability the mutation probability
     * @param distributionIndex   the distribution index
     * @param solutionRepair      the solution repair
     */
    public MLIntegerPolynomialMutation(double mutationProbability, double distributionIndex,
                                       RepairDoubleSolution solutionRepair) {
        this(mutationProbability, distributionIndex, solutionRepair, () -> JMetalRandom.getInstance().nextDouble());
    }

    /**
     * Constructor
     *
     * @param mutationProbability the mutation probability
     * @param distributionIndex   the distribution index
     * @param solutionRepair      the solution repair
     * @param randomGenerator     the random generator
     */
    public MLIntegerPolynomialMutation(double mutationProbability, double distributionIndex,
                                       RepairDoubleSolution solutionRepair, RandomGenerator<Double> randomGenerator) {
        if (mutationProbability < 0) {
            throw new JMetalException("Mutation probability is negative: " + mutationProbability);
        } else if (distributionIndex < 0) {
            throw new JMetalException("Distribution index is negative: " + distributionIndex);
        }
        this.mutationProbability = mutationProbability;
        this.distributionIndex = distributionIndex;
        this.solutionRepair = solutionRepair;

        this.randomGenerator = randomGenerator;
    }

    /**
     * Gets mutation probability.
     *
     * @return the mutation probability
     */
    /* Getters */
    public double getMutationProbability() {
        return mutationProbability;
    }

    /**
     * Gets distribution index.
     *
     * @return the distribution index
     */
    public double getDistributionIndex() {
        return distributionIndex;
    }

    /**
     * Sets distribution index.
     *
     * @param distributionIndex the distribution index
     */
    /* Setters */
    public void setDistributionIndex(double distributionIndex) {
        this.distributionIndex = distributionIndex;
    }

    /**
     * Sets mutation probability.
     *
     * @param mutationProbability the mutation probability
     */
    public void setMutationProbability(double mutationProbability) {
        this.mutationProbability = mutationProbability;
    }

    @Override
    public MLIntegerSolution execute(MLIntegerSolution solution) throws JMetalException {
        if (null == solution) {
            throw new JMetalException("Null parameter");
        }

        doMutation(mutationProbability, solution);
        return solution;
    }

    /**
     * Perform the mutation operation
     */
    private void doMutation(double probability, MLIntegerSolution solution) {
        Double rnd, delta1, delta2, mutPow, deltaq;
        double y, yl, yu, val, xy;

        for (int i = 0; i < solution.getNumberOfVariables(); i++) {
            if (randomGenerator.getRandomValue() <= probability) {
                y = solution.getVariableValue(i).doubleValue();
                yl = (double) solution.getLowerBound(i);
                yu = (double) solution.getUpperBound(i);
                if (yl == yu) {
                    y = yl;
                } else {
                    delta1 = (y - yl) / (yu - yl);
                    delta2 = (yu - y) / (yu - yl);
                    rnd = randomGenerator.getRandomValue();
                    mutPow = 1.0 / (distributionIndex + 1.0);
                    if (rnd <= 0.5) {
                        xy = 1.0 - delta1;
                        val = 2.0 * rnd + (1.0 - 2.0 * rnd) * (Math.pow(xy, distributionIndex + 1.0));
                        deltaq = Math.pow(val, mutPow) - 1.0;
                    } else {
                        xy = 1.0 - delta2;
                        val = 2.0 * (1.0 - rnd) + 2.0 * (rnd - 0.5) * (Math.pow(xy, distributionIndex + 1.0));
                        deltaq = 1.0 - Math.pow(val, mutPow);
                    }
                    y = y + deltaq * (yu - yl);
                    y = solutionRepair.repairSolutionVariableValue(y, yl, yu);
                }
                solution.setVariableValue(i, new MLInteger((int) y));
            }
        }
    }
}
