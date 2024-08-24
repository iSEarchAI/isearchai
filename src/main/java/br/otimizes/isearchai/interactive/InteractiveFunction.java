package br.otimizes.isearchai.interactive;

import br.otimizes.isearchai.learning.ml.MLSolutionSet;

/**
 * Interface that allows to adapt the method of interaction with the user
 */
public interface InteractiveFunction<T extends MLSolutionSet> {

    T run(T solutionSet);
}
