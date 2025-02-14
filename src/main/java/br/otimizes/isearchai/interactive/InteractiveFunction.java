package br.otimizes.isearchai.interactive;

import br.otimizes.isearchai.core.MLSolutionSet;

/**
 * Interface that allows to adapt the method of interaction with the user
 *
 * @param <T> the type parameter
 */
public interface InteractiveFunction<T extends MLSolutionSet> {

    /**
     * Run t.
     *
     * @param solutionSet the solution set
     * @return the t
     */
    T run(T solutionSet);
}
