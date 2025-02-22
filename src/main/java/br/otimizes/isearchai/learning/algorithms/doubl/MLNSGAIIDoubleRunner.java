package br.otimizes.isearchai.learning.algorithms.doubl;

import br.otimizes.isearchai.learning.algorithms.doubl.crossover.MLSBXCrossover;
import br.otimizes.isearchai.learning.algorithms.doubl.mutation.MLSimpleRandomMutation;
import br.otimizes.isearchai.learning.algorithms.options.nsgaii.MLNSGAIIRunner;
import br.otimizes.isearchai.learning.encoding.doubl.MLDoubleSolution;
import br.otimizes.isearchai.learning.encoding.doubl.MLDoubleSolutionSet;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;

import java.util.List;

/**
 * The type Mlnsgaii double runner.
 */
public abstract class MLNSGAIIDoubleRunner extends MLNSGAIIRunner<MLDoubleSolution> {

    public MutationOperator<MLDoubleSolution> getMutation() {
        return new MLSimpleRandomMutation(getMutationProbability());
    }

    /**
     * Gets crossover distribution index.
     *
     * @return the crossover distribution index
     */
    public double getCrossoverDistributionIndex() {
        return 20.0;
    }

    public CrossoverOperator<MLDoubleSolution> getCrossover() {
        return new MLSBXCrossover(getCrossoverProbability(), getCrossoverDistributionIndex());
    }

    public SelectionOperator<List<MLDoubleSolution>, MLDoubleSolution> getSelectionOperator() {
        return new BinaryTournamentSelection<>(
            getDistanceComparator()
        );
    }

    public RankingAndCrowdingDistanceComparator<MLDoubleSolution> getDistanceComparator() {
        return new RankingAndCrowdingDistanceComparator<>();
    }

    public MLDoubleSolutionSet getSolutionSet() {
        return new MLDoubleSolutionSet();
    }
}
