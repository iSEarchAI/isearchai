package br.otimizes.isearchai.core;

import java.util.HashSet;

/**
 * The interface Ml search algorithm.
 *
 * @param <T> the type parameter
 * @param <S> the type parameter
 * @param <E> the type parameter
 */
public interface MLSearchAlgorithm<T extends MLSolutionSet<S, E>, S extends MLSolution<E, ?>, E extends MLElement> {

    /**
     * Train the model
     * <p>
     * ISEARCHAI::EXAMPLE::{interaction.subjectiveAnalyzeAlgorithmEvaluate(solutionSet);}
     *
     * @param solutionSet the solution set
     */
    void trainModel(T solutionSet);

    /**
     * Interact with DM
     * <p>
     * ISEARCHAI::EXAMPLE::{return interaction.interactWithDMUpdatingInteraction(offspringPopulation, bestOfUserEvaluation, generation);}
     *
     * @param offspringPopulation  the offspring population
     * @param bestOfUserEvaluation the best of user evaluation
     * @param generation           the generation
     * @return int
     */
    int interactWithDM(T offspringPopulation, HashSet<S> bestOfUserEvaluation, int generation);
}
