package br.otimizes.isearchai.learning;

import br.otimizes.isearchai.core.MLSolution;
import br.otimizes.isearchai.core.MLSolutionSet;

public class InteractionRunner<S extends MLSolution<?, ?>> {
    public MLSolutionSet<S, ?> getInteraction(MLSolutionSet solutionSet) {
        System.out.println("Interacting...");
        MLSolutionSet<S, ?> solutions = solutionSet;
        for (S solution : solutions) {
            if (solution.getObjective(0) < .2) {
                solution.setEvaluation(4);
            } else if (solution.getObjective(1) < .4) {
                solution.setEvaluation(3);
            } else {
                solution.setEvaluation(2);
            }
        }
        return solutions;
    }


    public int getMaxInteractions() {
        return 3;
    }

    public int getIntervalInteraction() {
        return 3;
    }

    public int getFirstInteraction() {
        return 3;
    }
}
