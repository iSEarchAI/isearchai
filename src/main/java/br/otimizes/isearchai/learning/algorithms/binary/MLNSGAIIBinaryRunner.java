package br.otimizes.isearchai.learning.algorithms.binary;

import br.otimizes.isearchai.learning.algorithms.options.nsgaii.MLNSGAIIRunner;
import br.otimizes.isearchai.learning.encoding.binary.MLBinarySolution;
import br.otimizes.isearchai.learning.encoding.binary.MLBinarySolutionSet;
import br.otimizes.isearchai.learning.algorithms.binary.crossover.MLSinglePointCrossover;
import br.otimizes.isearchai.learning.algorithms.binary.mutation.MLBitFilpMutation;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;

import java.util.List;

/**
 * The type Mlnsgaii binary runner.
 */
public abstract class MLNSGAIIBinaryRunner extends MLNSGAIIRunner<MLBinarySolution> {

    public MutationOperator<MLBinarySolution> getMutation() {
        return new MLBitFilpMutation(getMutationProbability());
    }

    public CrossoverOperator<MLBinarySolution> getCrossover() {
        return new MLSinglePointCrossover(getCrossoverProbability());
    }

    public SelectionOperator<List<MLBinarySolution>, MLBinarySolution> getSelectionOperator() {
        return new BinaryTournamentSelection<>(
            getDistanceComparator()
        );
    }

    public RankingAndCrowdingDistanceComparator<MLBinarySolution> getDistanceComparator() {
        return new RankingAndCrowdingDistanceComparator<>();
    }

    public MLBinarySolutionSet getSolutionSet() {
        return new MLBinarySolutionSet();
    }
}
