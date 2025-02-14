package br.otimizes.isearchai.interactive;

import br.otimizes.isearchai.core.MLElement;
import br.otimizes.isearchai.core.MLSolution;
import br.otimizes.isearchai.core.MLSolutionSet;
import br.otimizes.isearchai.learning.algorithms.options.nsgaii.SubjectiveAnalyzeOptions;

/**
 * The type Interactive config.
 *
 * @param <T> the type parameter
 * @param <E> the type parameter
 */
public class InteractiveConfig<T extends MLSolutionSet<E, MLElement>, E extends MLSolution<MLElement, ?>> {
    private int maxInteractions;
    private int firstInteraction;
    private int intervalInteraction;
    private InteractiveFunction<T> interactiveFunction;
    private int currentInteraction = 0;
    private SubjectiveAnalyzeOptions options;

    /**
     * Instantiates a new Interactive config.
     */
    public InteractiveConfig() {
    }

    /**
     * Gets max interactions.
     *
     * @return the max interactions
     */
    public int getMaxInteractions() {
        return maxInteractions;
    }

    /**
     * Sets max interactions.
     *
     * @param maxInteractions the max interactions
     * @return the max interactions
     */
    public InteractiveConfig setMaxInteractions(int maxInteractions) {
        this.maxInteractions = maxInteractions;
        return this;
    }

    /**
     * Gets first interaction.
     *
     * @return the first interaction
     */
    public int getFirstInteraction() {
        return firstInteraction;
    }

    /**
     * Sets first interaction.
     *
     * @param firstInteraction the first interaction
     * @return the first interaction
     */
    public InteractiveConfig setFirstInteraction(int firstInteraction) {
        this.firstInteraction = firstInteraction;
        return this;
    }

    /**
     * Gets interval interaction.
     *
     * @return the interval interaction
     */
    public int getIntervalInteraction() {
        return intervalInteraction;
    }

    /**
     * Sets interval interaction.
     *
     * @param intervalInteraction the interval interaction
     * @return the interval interaction
     */
    public InteractiveConfig setIntervalInteraction(int intervalInteraction) {
        this.intervalInteraction = intervalInteraction;
        return this;
    }

    /**
     * Gets interactive function.
     *
     * @return the interactive function
     */
    public InteractiveFunction<T> getInteractiveFunction() {
        return interactiveFunction;
    }

    /**
     * Sets interactive function.
     *
     * @param interactiveFunction the interactive function
     * @return the interactive function
     */
    public InteractiveConfig setInteractiveFunction(InteractiveFunction<T> interactiveFunction) {
        this.interactiveFunction = interactiveFunction;
        return this;
    }

    /**
     * Sets current interaction.
     *
     * @param value the value
     * @return the current interaction
     */
    public InteractiveConfig setCurrentInteraction(int value) {
        this.currentInteraction = value;
        return this;
    }

    /**
     * Gets current interaction.
     *
     * @return the current interaction
     */
    public int getCurrentInteraction() {
        return this.currentInteraction;
    }


    /**
     * Gets options.
     *
     * @return the options
     */
    public SubjectiveAnalyzeOptions getOptions() {
        return options;
    }

    /**
     * Sets options.
     *
     * @param options the options
     * @return the options
     */
    public InteractiveConfig setOptions(SubjectiveAnalyzeOptions options) {
        this.options = options;
        return this;
    }
}
