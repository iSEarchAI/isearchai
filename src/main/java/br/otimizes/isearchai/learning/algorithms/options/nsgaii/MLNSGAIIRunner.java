package br.otimizes.isearchai.learning.algorithms.options.nsgaii;

import br.otimizes.isearchai.interactive.InteractiveConfig;
import br.otimizes.isearchai.core.MLSolution;
import br.otimizes.isearchai.core.MLSolutionSet;
import br.otimizes.isearchai.learning.InteractionRunner;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.util.AlgorithmRunner;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * The type Mlnsgaii runner.
 *
 * @param <S> the type parameter
 */
public abstract class MLNSGAIIRunner<S extends MLSolution<?, ?>> {

    /**
     * The Population.
     */
    public List<S> population;

    /**
     * Instantiates a new Mlnsgaii runner.
     */
    public MLNSGAIIRunner() {
    }

    /**
     * Run mlnsgaii runner.
     *
     * @return the mlnsgaii runner
     */
    public MLNSGAIIRunner run() {
        preBuild();
        Algorithm<List<S>> algorithm = build();
        postBuild(algorithm);

        execute(algorithm);
        List<S> population = getResult(algorithm);
        postGetResult(population);
        return this;
    }

    /**
     * Post get result.
     *
     * @param population the population
     */
    public void postGetResult(List<S> population) {
        for (S solution : population) {
            System.out.println(Arrays.toString(solution.getObjectives()) + " - Evaluation: " + solution.getEvaluation());
        }
        System.out.println("Done");
    }

    /**
     * Gets result.
     *
     * @param algorithm the algorithm
     * @return the result
     */
    public List<S> getResult(Algorithm<List<S>> algorithm) {
        return algorithm.getResult();
    }

    /**
     * Execute.
     *
     * @param algorithm the algorithm
     */
    public void execute(Algorithm<List<S>> algorithm) {
        new AlgorithmRunner.Executor(algorithm).execute();
    }

    /**
     * Build algorithm.
     *
     * @return the algorithm
     */
    public Algorithm<List<S>> build() {
        Algorithm<List<S>> algorithm = new MLNSGAIIBuilder<S>()
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

    /**
     * Gets solution set.
     *
     * @param <T> the type parameter
     * @return the solution set
     */
    public abstract <T extends MLSolutionSet<?, ?>> T getSolutionSet();

    /**
     * Gets interactive config.
     *
     * @return the interactive config
     */
    public InteractiveConfig getInteractiveConfig() {
        return getInteractiveFunction()
            .setFirstInteraction(this.getInteraction().getFirstInteraction())
            .setOptions(getSubjectiveAnalyzeOptions())
            .setIntervalInteraction(this.getInteraction().getIntervalInteraction())
            .setMaxInteractions(this.getInteraction().getMaxInteractions());
    }

    /**
     * Gets subjective analyze options.
     *
     * @return the subjective analyze options
     */
    public SubjectiveAnalyzeOptions getSubjectiveAnalyzeOptions() {
        return new SubjectiveAnalyzeMLP();
    }

    /**
     * Gets problem.
     *
     * @return the problem
     */
    public abstract Problem<S> getProblem();

    /**
     * Post build.
     *
     * @param algorithm the algorithm
     */
    public void postBuild(Algorithm<List<S>> algorithm) {
        System.out.println("Optimizing...");
    }

    /**
     * Pre build.
     */
    public void preBuild() {

    }

    /**
     * Gets interactive function.
     *
     * @return the interactive function
     */
    public InteractiveConfig getInteractiveFunction() {
        return new InteractiveConfig().setInteractiveFunction(s -> this.getInteraction().getInteraction(s));
    }

    /**
     * Gets interaction.
     *
     * @return the interaction
     */
    public InteractionRunner<S> getInteraction() {
        return new InteractionRunner<>();
    }

    /**
     * Gets max evaluations.
     *
     * @return the max evaluations
     */
    public int getMaxEvaluations() {
        return 10000;
    }

    /**
     * Gets population size.
     *
     * @return the population size
     */
    public int getPopulationSize() {
        return 100;
    }

    /**
     * Gets selection operator.
     *
     * @return the selection operator
     */
    public abstract SelectionOperator<List<S>, S> getSelectionOperator();

    /**
     * Gets distance comparator.
     *
     * @return the distance comparator
     */
    public abstract Comparator<S> getDistanceComparator();

    /**
     * Gets mutation.
     *
     * @return the mutation
     */
    public abstract MutationOperator<S> getMutation();

    /**
     * Gets mutation probability.
     *
     * @return the mutation probability
     */
    public double getMutationProbability() {
        return 0.005;
    }

    /**
     * Gets crossover.
     *
     * @return the crossover
     */
    public abstract CrossoverOperator<S> getCrossover();

    /**
     * Gets crossover probability.
     *
     * @return the crossover probability
     */
    public double getCrossoverProbability() {
        return 0.9;
    }
}
