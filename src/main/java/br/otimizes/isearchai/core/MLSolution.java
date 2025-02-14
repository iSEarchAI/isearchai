package br.otimizes.isearchai.core;

import org.uma.jmetal.solution.Solution;

import java.util.List;

/**
 * The interface Ml solution.
 *
 * @param <T> the type parameter
 * @param <S> the type parameter
 */
public interface MLSolution<T extends MLElement, S> extends Solution<S> {
    double[] getObjectives();

    /**
     * Number of objectives int.
     *
     * @return the int
     */
    int numberOfObjectives();

    double getObjective(int i);

    /**
     * Sets cluster id.
     *
     * @param assignment the assignment
     */
    void setClusterId(Double assignment);

    /**
     * Sets cluster noise.
     *
     * @param b the b
     */
    void setClusterNoise(Boolean b);

    /**
     * Gets cluster id.
     *
     * @return the cluster id
     */
    Double getClusterId();

    /**
     * Sets evaluation.
     *
     * @param i the
     */
    void setEvaluation(int i);

    /**
     * Gets elements.
     *
     * @return the elements
     */
    List<T> getElements();

    /**
     * Contains elements evaluation boolean.
     *
     * @return the boolean
     */
    boolean containsElementsEvaluation();

    /**
     * Gets evaluation.
     *
     * @return the evaluation
     */
    int getEvaluation();

    /**
     * Gets evaluated by user.
     *
     * @return the evaluated by user
     */
    boolean getEvaluatedByUser();
}
