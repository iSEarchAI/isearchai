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

/**
 * The type Mlnsgaii builder.
 *
 * @param <S> the type parameter
 */
public class MLNSGAIIBuilder<S extends Solution<?>> implements AlgorithmBuilder<MLNSGAII<S>> {
    // no access modifier means access from classes within the same package
    private Problem<S> problem;
    private int maxEvaluations;
    private int populationSize;
    /**
     * The Mating pool size.
     */
    protected int matingPoolSize;
    /**
     * The Offspring population size.
     */
    protected int offspringPopulationSize;

    private CrossoverOperator<S> crossoverOperator;
    private MutationOperator<S> mutationOperator;
    private SelectionOperator<List<S>, S> selectionOperator;
    private SolutionListEvaluator<S> evaluator;
    private Comparator<S> dominanceComparator;
    private InteractiveConfig interactiveConfig;
    private MLSolutionSet solutionSet;

    /**
     * Instantiates a new Mlnsgaii builder.
     */
    public MLNSGAIIBuilder() {
        maxEvaluations = 25000;
        selectionOperator = new BinaryTournamentSelection<S>(new RankingAndCrowdingDistanceComparator<S>());
        evaluator = new SequentialSolutionListEvaluator<S>();
        dominanceComparator = new DominanceComparator<>();
    }

    /**
     * Builder constructor
     *
     * @param problem           the problem
     * @param crossoverOperator the crossover operator
     * @param mutationOperator  the mutation operator
     * @param populationSize    the population size
     * @param interactiveConfig the interactive config
     * @param solutionSet       the solution set
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


    /**
     * Sets population size.
     *
     * @param populationSize the population size
     * @return the population size
     */
    public MLNSGAIIBuilder<S> setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
        this.matingPoolSize = populationSize;
        this.offspringPopulationSize = populationSize;

        return this;
    }

    /**
     * Sets crossover operator.
     *
     * @param crossoverOperator the crossover operator
     * @return the crossover operator
     */
    public MLNSGAIIBuilder<S> setCrossoverOperator(CrossoverOperator<S> crossoverOperator) {
        this.crossoverOperator = crossoverOperator;

        return this;
    }

    /**
     * Sets mutation operator.
     *
     * @param mutationOperator the mutation operator
     * @return the mutation operator
     */
    public MLNSGAIIBuilder<S> setMutationOperator(MutationOperator<S> mutationOperator) {
        this.mutationOperator = mutationOperator;

        return this;
    }

    /**
     * Sets selection operator.
     *
     * @param selectionOperator the selection operator
     * @return the selection operator
     */
    public MLNSGAIIBuilder<S> setSelectionOperator(SelectionOperator<List<S>, S> selectionOperator) {
        this.selectionOperator = selectionOperator;

        return this;
    }

    /**
     * Sets solution list evaluator.
     *
     * @param evaluator the evaluator
     * @return the solution list evaluator
     */
    public MLNSGAIIBuilder<S> setSolutionListEvaluator(SolutionListEvaluator<S> evaluator) {
        this.evaluator = evaluator;

        return this;
    }

    /**
     * Sets max evaluations.
     *
     * @param maxEvaluations the max evaluations
     * @return the max evaluations
     */
    public MLNSGAIIBuilder<S> setMaxEvaluations(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;

        return this;
    }

    /**
     * Sets problem.
     *
     * @param problem the problem
     * @return the problem
     */
    public MLNSGAIIBuilder<S> setProblem(Problem<S> problem) {
        this.problem = problem;

        return this;
    }

    /**
     * Sets interactive config.
     *
     * @param interactiveConfig the interactive config
     * @return the interactive config
     */
    public MLNSGAIIBuilder<S> setInteractiveConfig(InteractiveConfig interactiveConfig) {
        this.interactiveConfig = interactiveConfig;

        return this;
    }

    /**
     * Sets solution set.
     *
     * @param solutionSet the solution set
     * @return the solution set
     */
    public MLNSGAIIBuilder<S> setSolutionSet(MLSolutionSet solutionSet) {
        this.solutionSet = solutionSet;

        return this;
    }

    /**
     * Gets evaluator.
     *
     * @return the evaluator
     */
    public SolutionListEvaluator<S> getEvaluator() {
        return evaluator;
    }

    /**
     * Gets problem.
     *
     * @return the problem
     */
    public Problem<S> getProblem() {
        return problem;
    }

    /**
     * Gets population size.
     *
     * @return the population size
     */
    public int getPopulationSize() {
        return populationSize;
    }

    /**
     * Gets crossover operator.
     *
     * @return the crossover operator
     */
    public CrossoverOperator<S> getCrossoverOperator() {
        return crossoverOperator;
    }

    /**
     * Gets mutation operator.
     *
     * @return the mutation operator
     */
    public MutationOperator<S> getMutationOperator() {
        return mutationOperator;
    }

    /**
     * Gets selection operator.
     *
     * @return the selection operator
     */
    public SelectionOperator<List<S>, S> getSelectionOperator() {
        return selectionOperator;
    }

    public MLNSGAII<S> build() {
        return new MLNSGAII<S>(problem, maxEvaluations, populationSize, matingPoolSize, offspringPopulationSize,
            crossoverOperator,
            mutationOperator, selectionOperator, dominanceComparator, evaluator, interactiveConfig, solutionSet);
    }
}

