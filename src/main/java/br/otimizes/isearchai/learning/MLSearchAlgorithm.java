package br.otimizes.isearchai.learning;

import java.util.HashSet;

public interface MLSearchAlgorithm<T extends MLSolutionSet<S, E>, S extends MLSolution<E>, E extends MLElement> {
    void trainModel(T solutionSet);

    int interactWithDM(T offspringPopulation, HashSet<S> bestOfUserEvaluation, int generation);
}
