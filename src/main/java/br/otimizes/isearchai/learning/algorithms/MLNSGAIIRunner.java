package br.otimizes.isearchai.learning.algorithms;

import br.otimizes.isearchai.interactive.InteractiveConfig;
import br.otimizes.isearchai.learning.ml.interfaces.MLSolutionSet;
import br.otimizes.isearchai.learning.encoding.binary.*;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;

import java.util.Arrays;
import java.util.List;

public abstract class MLNSGAIIRunner {

    public List<MLBinarySolution> population;

    public MLNSGAIIRunner() {
    }

    public MLNSGAIIRunner run() {
        preBuild();
        Algorithm<List<MLBinarySolution>> algorithm = build();
        postBuild(algorithm);

        execute(algorithm);
        List<MLBinarySolution> population = getResult(algorithm);
        postGetResult(population);
        return this;
    }

    public static void postGetResult(List<MLBinarySolution> population) {
        for (MLBinarySolution solution : population) {
            System.out.println(Arrays.toString(solution.getObjectives()) + " - Evaluation: " + solution.getEvaluation());
        }
        System.out.println("Done");
    }

    public List<MLBinarySolution> getResult(Algorithm<List<MLBinarySolution>> algorithm) {
        return algorithm.getResult();
    }

    public void execute(Algorithm<List<MLBinarySolution>> algorithm) {
        new AlgorithmRunner.Executor(algorithm).execute();
    }

    public Algorithm<List<MLBinarySolution>> build() {
        Algorithm<List<MLBinarySolution>> algorithm = new MLNSGAIIBuilder<MLBinarySolution>()
            .setProblem(getProblem())
            .setInteractiveConfig(getInteractiveConfig())
            .setSolutionSet(getSolutionSet())
            .setCrossoverOperator(getCrossover())
            .setMutationOperator(getMutation())
            .setPopulationSize(getPopulationSize())
            .setSelectionOperator(getSelectionOperator())
            .setMaxEvaluations(getMaxEvaluations())
            .build();
        return algorithm;
    }

    public MLBinarySolutionSet getSolutionSet() {
        return new MLBinarySolutionSet();
    }

    public InteractiveConfig getInteractiveConfig() {
        return getInteractiveFunction()
            .setFirstInteraction(getFirstInteraction())
            .setIntervalInteraction(getIntervalInteraction()).setMaxInteractions(getMaxInteractions());
    }

    public abstract Problem<MLBinarySolution> getProblem();

    public void postBuild(Algorithm<List<MLBinarySolution>> algorithm) {
        System.out.println("Optimizing...");
    }

    public void preBuild() {

    }

    public InteractiveConfig getInteractiveFunction() {
        return new InteractiveConfig().setInteractiveFunction(this::getInteraction);
    }

    public MLSolutionSet<MLBinarySolution, MLBinarySet> getInteraction(MLSolutionSet solutionSet) {
        System.out.println("Interacting...");
        MLSolutionSet<MLBinarySolution, MLBinarySet> solutions = solutionSet;
        for (MLBinarySolution solution : solutions) {
            if (solution.getObjective(0) < .2) {
                solution.setEvaluation(4);
            } else if (solution.getObjective(1) < .4) {
                solution.setEvaluation(3);
            } else {
                solution.setEvaluation(2);
            }
        }
        return solutions;
    }

    public int getMaxEvaluations() {
        return 10000;
    }

    public int getPopulationSize() {
        return 100;
    }

    public int getMaxInteractions() {
        return 3;
    }

    public int getIntervalInteraction() {
        return 3;
    }

    public int getFirstInteraction() {
        return 3;
    }

    public BinaryTournamentSelection<MLBinarySolution> getSelectionOperator() {
        return new BinaryTournamentSelection<>(
            getDistanceComparator()
        );
    }

    public RankingAndCrowdingDistanceComparator<MLBinarySolution> getDistanceComparator() {
        return new RankingAndCrowdingDistanceComparator<>();
    }

    public MutationOperator<MLBinarySolution> getMutation() {
        return new MLBitFilpMutation(getMutationProbability());
    }

    public double getMutationProbability() {
        return 0.005;
    }

    public CrossoverOperator<MLBinarySolution> getCrossover() {
        return new MLSinglePointCrossover(getCrossoverProbability());
    }

    public double getCrossoverProbability() {
        return 0.9;
    }
}
