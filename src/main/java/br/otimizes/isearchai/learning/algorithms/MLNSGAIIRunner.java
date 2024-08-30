package br.otimizes.isearchai.learning.algorithms;

import br.otimizes.isearchai.interactive.InteractiveConfig;
import br.otimizes.isearchai.core.MLSolution;
import br.otimizes.isearchai.core.MLSolutionSet;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.util.AlgorithmRunner;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public abstract class MLNSGAIIRunner<S extends MLSolution<?>> {

    public List<S> population;

    public MLNSGAIIRunner() {
    }

    public MLNSGAIIRunner run() {
        preBuild();
        Algorithm<List<S>> algorithm = build();
        postBuild(algorithm);

        execute(algorithm);
        List<S> population = getResult(algorithm);
        postGetResult(population);
        return this;
    }

    public void postGetResult(List<S> population) {
        for (S solution : population) {
            System.out.println(Arrays.toString(solution.getObjectives()) + " - Evaluation: " + solution.getEvaluation());
        }
        System.out.println("Done");
    }

    public List<S> getResult(Algorithm<List<S>> algorithm) {
        return algorithm.getResult();
    }

    public void execute(Algorithm<List<S>> algorithm) {
        new AlgorithmRunner.Executor(algorithm).execute();
    }

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

    public abstract <T extends MLSolutionSet<?, ?>> T getSolutionSet();

    public InteractiveConfig getInteractiveConfig() {
        return getInteractiveFunction()
            .setFirstInteraction(getFirstInteraction())
            .setIntervalInteraction(getIntervalInteraction()).setMaxInteractions(getMaxInteractions());
    }

    public abstract Problem<S> getProblem();

    public void postBuild(Algorithm<List<S>> algorithm) {
        System.out.println("Optimizing...");
    }

    public void preBuild() {

    }

    public InteractiveConfig getInteractiveFunction() {
        return new InteractiveConfig().setInteractiveFunction(this::getInteraction);
    }

    public MLSolutionSet<S, ?> getInteraction(MLSolutionSet solutionSet) {
        System.out.println("Interacting...");
        MLSolutionSet<S, ?> solutions = solutionSet;
        for (S solution : solutions) {
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

    public abstract SelectionOperator<List<S>, S> getSelectionOperator();

    public abstract Comparator<S> getDistanceComparator();

    public abstract MutationOperator<S> getMutation();

    public double getMutationProbability() {
        return 0.005;
    }

    public abstract CrossoverOperator<S> getCrossover();

    public double getCrossoverProbability() {
        return 0.9;
    }
}
