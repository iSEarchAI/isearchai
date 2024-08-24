package br.otimizes.isearchai.learning.ml.interfaces;

import java.util.List;

public interface MLSolution<T extends MLElement> {
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
