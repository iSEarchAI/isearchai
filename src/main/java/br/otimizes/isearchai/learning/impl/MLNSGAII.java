package br.otimizes.isearchai.learning.impl;

import br.otimizes.isearchai.interactive.InteractWithDM;
import br.otimizes.isearchai.interactive.InteractiveConfig;
import br.otimizes.isearchai.learning.MLSolutionSet;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAII;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class MLNSGAII<S extends Solution<?>> extends NSGAII<S> {
    private InteractWithDM interaction = new InteractWithDM();
    private InteractiveConfig interactiveConfig;
    private MLSolutionSet solutionSet;

    HashSet<Solution> bestOfUserEvaluation = new HashSet<>();

    public MLNSGAII(Problem<S> problem, int maxEvaluations, int populationSize, int matingPoolSize, int offspringPopulationSize,
                    CrossoverOperator<S> crossoverOperator, MutationOperator<S> mutationOperator, SelectionOperator<List<S>, S>
                        selectionOperator, SolutionListEvaluator<S> evaluator, InteractiveConfig interactiveConfig, MLSolutionSet solutionSet) {
        super(problem, maxEvaluations, populationSize, matingPoolSize, offspringPopulationSize, crossoverOperator,
            mutationOperator, selectionOperator, evaluator);
        this.solutionSet = solutionSet;
        setInteractiveConfig(interactiveConfig);
    }

    private void setInteractiveConfig(InteractiveConfig interactiveConfig) {
        this.interactiveConfig = interactiveConfig;
        this.interaction.setMaxInteractions(interactiveConfig.getMaxInteractions());
        this.interaction.setFirstInteraction(interactiveConfig.getFirstInteraction());
        this.interaction.setIntervalInteraction(interactiveConfig.getIntervalInteraction());
        this.interaction.setInteractive(true);
        this.interaction.setCurrentInteraction(0);
        this.interaction.setInteractiveFunction(interactiveConfig.getInteractiveFunction());
    }

    public MLNSGAII(Problem<S> problem, int maxEvaluations, int populationSize, int matingPoolSize, int offspringPopulationSize,
                    CrossoverOperator<S> crossoverOperator, MutationOperator<S> mutationOperator, SelectionOperator<List<S>, S>
                        selectionOperator, Comparator<S> dominanceComparator, SolutionListEvaluator<S> evaluator, InteractiveConfig interactiveConfig, MLSolutionSet solutionSet) {
        super(problem, maxEvaluations, populationSize, matingPoolSize, offspringPopulationSize, crossoverOperator,
            mutationOperator, selectionOperator, dominanceComparator, evaluator);
        this.solutionSet = solutionSet;
        setInteractiveConfig(interactiveConfig);
    }


    private int getcurrentGeneration() {
        return this.evaluations / this.maxPopulationSize;
    }


    @Override
    protected List<S> replacement(List<S> population, List<S> offspringPopulation) {
        List<S> replacement = super.replacement(population, offspringPopulation);
        MLSolutionSet mlSolutionSet = solutionSet.newInstance(replacement);
        interaction.interactWithDM(mlSolutionSet, bestOfUserEvaluation, getcurrentGeneration());
        interaction.subjectiveAnalyzeAlgorithmEvaluate(mlSolutionSet);
        return replacement;
    }
}
