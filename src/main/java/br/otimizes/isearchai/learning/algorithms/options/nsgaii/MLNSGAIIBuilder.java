package br.otimizes.isearchai.learning.algorithms.options.nsgaii;

import br.otimizes.isearchai.interactive.InteractiveConfig;
import br.otimizes.isearchai.core.MLSolutionSet;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.comparator.DominanceComparator;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

import java.util.Comparator;
import java.util.List;

public class MLNSGAIIBuilder<S extends Solution<?>> implements AlgorithmBuilder<MLNSGAII<S>> {
    // no access modifier means access from classes within the same package
    private Problem<S> problem;
    private int maxEvaluations;
    private int populationSize;
    protected int matingPoolSize;
    protected int offspringPopulationSize;

    private CrossoverOperator<S> crossoverOperator;
    private MutationOperator<S> mutationOperator;
    private SelectionOperator<List<S>, S> selectionOperator;
    private SolutionListEvaluator<S> evaluator;
    private Comparator<S> dominanceComparator;
    private InteractiveConfig interactiveConfig;
    private MLSolutionSet solutionSet;

    public MLNSGAIIBuilder() {
        maxEvaluations = 25000;
        selectionOperator = new BinaryTournamentSelection<S>(new RankingAndCrowdingDistanceComparator<S>());
        evaluator = new SequentialSolutionListEvaluator<S>();
        dominanceComparator = new DominanceComparator<>();
    }

    /**
     * Builder constructor
     */
    public MLNSGAIIBuilder(Problem<S> problem, CrossoverOperator<S> crossoverOperator,
                           MutationOperator<S> mutationOperator, int populationSize, InteractiveConfig interactiveConfig, MLSolutionSet solutionSet) {
        this.problem = problem;
        maxEvaluations = 25000;
        this.populationSize = populationSize;
        matingPoolSize = populationSize;
        offspringPopulationSize = populationSize;
        this.crossoverOperator = crossoverOperator;
        this.mutationOperator = mutationOperator;
        selectionOperator = new BinaryTournamentSelection<S>(new RankingAndCrowdingDistanceComparator<S>());
        evaluator = new SequentialSolutionListEvaluator<S>();
        dominanceComparator = new DominanceComparator<>();
        this.interactiveConfig = interactiveConfig;
        this.solutionSet = solutionSet;
    }


    public MLNSGAIIBuilder<S> setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
        this.matingPoolSize = populationSize;
        this.offspringPopulationSize = populationSize;

        return this;
    }

    public MLNSGAIIBuilder<S> setCrossoverOperator(CrossoverOperator<S> crossoverOperator) {
        this.crossoverOperator = crossoverOperator;

        return this;
    }

    public MLNSGAIIBuilder<S> setMutationOperator(MutationOperator<S> mutationOperator) {
        this.mutationOperator = mutationOperator;

        return this;
    }

    public MLNSGAIIBuilder<S> setSelectionOperator(SelectionOperator<List<S>, S> selectionOperator) {
        this.selectionOperator = selectionOperator;

        return this;
    }

    public MLNSGAIIBuilder<S> setSolutionListEvaluator(SolutionListEvaluator<S> evaluator) {
        this.evaluator = evaluator;

        return this;
    }

    public MLNSGAIIBuilder<S> setMaxEvaluations(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;

        return this;
    }

    public MLNSGAIIBuilder<S> setProblem(Problem<S> problem) {
        this.problem = problem;

        return this;
    }

    public MLNSGAIIBuilder<S> setInteractiveConfig(InteractiveConfig interactiveConfig) {
        this.interactiveConfig = interactiveConfig;

        return this;
    }

    public MLNSGAIIBuilder<S> setSolutionSet(MLSolutionSet solutionSet) {
        this.solutionSet = solutionSet;

        return this;
    }

    public SolutionListEvaluator<S> getEvaluator() {
        return evaluator;
    }

    public Problem<S> getProblem() {
        return problem;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public CrossoverOperator<S> getCrossoverOperator() {
        return crossoverOperator;
    }

    public MutationOperator<S> getMutationOperator() {
        return mutationOperator;
    }

    public SelectionOperator<List<S>, S> getSelectionOperator() {
        return selectionOperator;
    }

    public MLNSGAII<S> build() {
        return new MLNSGAII<S>(problem, maxEvaluations, populationSize, matingPoolSize, offspringPopulationSize,
            crossoverOperator,
            mutationOperator, selectionOperator, dominanceComparator, evaluator, interactiveConfig, solutionSet);
    }
}

