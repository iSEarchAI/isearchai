package br.otimizes.isearchai.interactive;

import br.otimizes.isearchai.learning.ml.MLElement;
import br.otimizes.isearchai.learning.ml.MLSolution;
import br.otimizes.isearchai.learning.ml.MLSolutionSet;

public class InteractiveConfig<T extends MLSolutionSet<E, MLElement>, E extends MLSolution<MLElement>> {
    private int maxInteractions;
    private int firstInteraction;
    private int intervalInteraction;
    private InteractiveFunction<T> interactiveFunction;

    public InteractiveConfig() {
    }

    public int getMaxInteractions() {
        return maxInteractions;
    }

    public InteractiveConfig setMaxInteractions(int maxInteractions) {
        this.maxInteractions = maxInteractions;
        return this;
    }

    public int getFirstInteraction() {
        return firstInteraction;
    }

    public InteractiveConfig setFirstInteraction(int firstInteraction) {
        this.firstInteraction = firstInteraction;
        return this;
    }

    public int getIntervalInteraction() {
        return intervalInteraction;
    }

    public InteractiveConfig setIntervalInteraction(int intervalInteraction) {
        this.intervalInteraction = intervalInteraction;
        return this;
    }

    public InteractiveFunction<T> getInteractiveFunction() {
        return interactiveFunction;
    }

    public InteractiveConfig setInteractiveFunction(InteractiveFunction<T> interactiveFunction) {
        this.interactiveFunction = interactiveFunction;
        return this;
    }
}
