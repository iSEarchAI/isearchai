package br.otimizes.isearchai.interactive;

import br.otimizes.isearchai.core.MLElement;
import br.otimizes.isearchai.core.MLSolution;
import br.otimizes.isearchai.core.MLSolutionSet;

public class InteractiveConfig<T extends MLSolutionSet<E, MLElement>, E extends MLSolution<MLElement, ?>> {
    private int maxInteractions;
    private int firstInteraction;
    private int intervalInteraction;
    private InteractiveFunction<T> interactiveFunction;
    private int currentInteraction = 0;

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

    public InteractiveConfig setCurrentInteraction(int value) {
        this.currentInteraction = value;
        return this;
    }

    public int getCurrentInteraction() {
        return this.currentInteraction;
    }
}
