package br.otimizes.isearchai.core;

import org.uma.jmetal.solution.Solution;

import java.util.List;

public interface MLSolution<T extends MLElement> extends Solution<T> {
    double[] getObjectives();

    int numberOfObjectives();

    double getObjective(int i);

    void setClusterId(Double assignment);

    void setClusterNoise(Boolean b);

    Double getClusterId();

    void setEvaluation(int i);

    List<T> getElements();

    boolean containsElementsEvaluation();

    int getEvaluation();

    boolean getEvaluatedByUser();
}
