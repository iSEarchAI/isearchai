package br.otimizes.isearchai.learning.algorithms.integer;

import br.otimizes.isearchai.learning.algorithms.MLNSGAIIRunner;
import br.otimizes.isearchai.learning.encoding.integer.MLIntegerSolution;
import br.otimizes.isearchai.learning.encoding.integer.MLIntegerSolutionSet;
import br.otimizes.isearchai.learning.encoding.integer.crossover.MLIntegerSBXCrossover;
import br.otimizes.isearchai.learning.encoding.integer.mutation.MLIntegerPolynomialMutation;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;

import java.util.List;

public abstract class MLNSGAIIIntegerRunner extends MLNSGAIIRunner<MLIntegerSolution> {

    public MutationOperator<MLIntegerSolution> getMutation() {
        return new MLIntegerPolynomialMutation(getMutationProbability(), getMutationDistributionIndex());
    }

    public double getMutationDistributionIndex() {
        return 20.0;
    }

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
