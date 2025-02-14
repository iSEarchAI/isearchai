package br.otimizes.isearchai.learning.algorithms.binary.mutation;

import br.otimizes.isearchai.learning.encoding.binary.MLBinarySolution;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.pseudorandom.RandomGenerator;

/**
 * The type Ml bit filp mutation.
 */
public class MLBitFilpMutation implements MutationOperator<MLBinarySolution> {
    private double mutationProbability;
    private RandomGenerator<Double> randomGenerator;

    /**
     * Constructor
     *
     * @param mutationProbability the mutation probability
     */
    public MLBitFilpMutation(double mutationProbability) {
        this(mutationProbability, () -> JMetalRandom.getInstance().nextDouble());
    }

    /**
     * Constructor
     *
     * @param mutationProbability the mutation probability
     * @param randomGenerator     the random generator
     */
    public MLBitFilpMutation(double mutationProbability, RandomGenerator<Double> randomGenerator) {
        if (mutationProbability < 0) {
            throw new JMetalException("Mutation probability is negative: " + mutationProbability);
        }
        this.mutationProbability = mutationProbability;
        this.randomGenerator = randomGenerator;
    }

    /**
     * Gets mutation probability.
     *
     * @return the mutation probability
     */
    /* Getter */
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
    public MLBinarySolution execute(MLBinarySolution solution) {
        if (null == solution) {
            throw new JMetalException("Null parameter");
        }

        doMutation(mutationProbability, solution);
        return solution;
    }

    /**
     * Perform the mutation operation
     *
     * @param probability Mutation setProbability
     * @param solution    The solution to mutate
     */
    public void doMutation(double probability, MLBinarySolution solution) {
        for (int i = 0; i < solution.getNumberOfVariables(); i++) {
            for (int j = 0; j < solution.getVariableValue(i).getBinarySetLength(); j++) {
                if (randomGenerator.getRandomValue() <= probability) {
                    solution.getVariableValue(i).flip(j);
                }
            }
        }
    }
}
