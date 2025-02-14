package br.otimizes.isearchai.learning.algorithms.doubl.mutation;

import br.otimizes.isearchai.learning.encoding.doubl.MLDouble;
import br.otimizes.isearchai.learning.encoding.doubl.MLDoubleSolution;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.pseudorandom.RandomGenerator;

/**
 * The type Ml simple random mutation.
 */
public class MLSimpleRandomMutation implements MutationOperator<MLDoubleSolution> {
    private double mutationProbability;
    private RandomGenerator<Double> randomGenerator;

    /**
     * Constructor
     *
     * @param probability the probability
     */
    public MLSimpleRandomMutation(double probability) {
        this(probability, () -> JMetalRandom.getInstance().nextDouble());
    }

    /**
     * Constructor
     *
     * @param probability     the probability
     * @param randomGenerator the random generator
     */
    public MLSimpleRandomMutation(double probability, RandomGenerator<Double> randomGenerator) {
        if (probability < 0) {
            throw new JMetalException("Mutation probability is negative: " + mutationProbability);
        }

        this.mutationProbability = probability;
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
     * Sets mutation probability.
     *
     * @param mutationProbability the mutation probability
     */
    /* Setters */
    public void setMutationProbability(double mutationProbability) {
        this.mutationProbability = mutationProbability;
    }

    /**
     * Execute() method
     */
    @Override
    public MLDoubleSolution execute(MLDoubleSolution solution) throws JMetalException {
        if (null == solution) {
            throw new JMetalException("Null parameter");
        }

        doMutation(mutationProbability, solution);

        return solution;
    }

    /**
     * Implements the mutation operation
     */
    private void doMutation(double probability, MLDoubleSolution solution) {
        for (int i = 0; i < solution.getNumberOfVariables(); i++) {
            if (randomGenerator.getRandomValue() <= probability) {
                Double value = solution.getLowerBound(i) +
                    ((solution.getUpperBound(i) - solution.getLowerBound(i)) * randomGenerator.getRandomValue());

                solution.setVariableValue(i, new MLDouble(value));
            }
        }
    }
}
