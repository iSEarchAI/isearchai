package br.otimizes.isearchai.core;

import java.util.HashSet;

public interface MLSearchAlgorithm<T extends MLSolutionSet<S, E>, S extends MLSolution<E, ?>, E extends MLElement> {

    /**
     * Train the model
     *
     * ISEARCHAI::EXAMPLE::{interaction.subjectiveAnalyzeAlgorithmEvaluate(solutionSet);}
     * @param solutionSet
     */
    void trainModel(T solutionSet);

    /**
     * Interact with DM
     *
     * ISEARCHAI::EXAMPLE::{return interaction.interactWithDMUpdatingInteraction(offspringPopulation, bestOfUserEvaluation, generation);}
     * @param offspringPopulation
     * @param bestOfUserEvaluation
     * @param generation
     * @return
     */
    int interactWithDM(T offspringPopulation, HashSet<S> bestOfUserEvaluation, int generation);
}
