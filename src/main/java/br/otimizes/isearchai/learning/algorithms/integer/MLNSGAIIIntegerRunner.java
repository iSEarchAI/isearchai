package br.otimizes.isearchai.learning.algorithms.integer;

import br.otimizes.isearchai.learning.algorithms.options.nsgaii.MLNSGAIIRunner;
import br.otimizes.isearchai.learning.encoding.integer.MLIntegerSolution;
import br.otimizes.isearchai.learning.encoding.integer.MLIntegerSolutionSet;
import br.otimizes.isearchai.learning.algorithms.integer.crossover.MLIntegerSBXCrossover;
import br.otimizes.isearchai.learning.algorithms.integer.mutation.MLIntegerPolynomialMutation;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;

import java.util.List;

/**
 * The type Mlnsgaii integer runner.
 */
public abstract class MLNSGAIIIntegerRunner extends MLNSGAIIRunner<MLIntegerSolution> {

    public MutationOperator<MLIntegerSolution> getMutation() {
        return new MLIntegerPolynomialMutation(getMutationProbability(), getMutationDistributionIndex());
    }

    /**
     * Gets mutation distribution index.
     *
     * @return the mutation distribution index
     */
    public double getMutationDistributionIndex() {
        return 20.0;
    }

    /**
     * Gets crossover distribution index.
     *
     * @return the crossover distribution index
     */
    public double getCrossoverDistributionIndex() {
        return 20.0;
    }

    public CrossoverOperator<MLIntegerSolution> getCrossover() {
        return new MLIntegerSBXCrossover(getCrossoverProbability(), getCrossoverDistributionIndex());
    }

    public SelectionOperator<List<MLIntegerSolution>, MLIntegerSolution> getSelectionOperator() {
        return new BinaryTournamentSelection<>(
            getDistanceComparator()
        );
    }

    public RankingAndCrowdingDistanceComparator<MLIntegerSolution> getDistanceComparator() {
        return new RankingAndCrowdingDistanceComparator<>();
    }

    public MLIntegerSolutionSet getSolutionSet() {
        return new MLIntegerSolutionSet();
    }
}
