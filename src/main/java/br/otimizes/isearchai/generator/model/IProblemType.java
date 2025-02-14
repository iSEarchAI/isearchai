package br.otimizes.isearchai.generator.model;

import java.util.List;

/**
 * The interface Problem type.
 */
public interface IProblemType {
    /**
     * Gets package.
     *
     * @return the package
     */
    String getPackage();

    /**
     * Gets solution.
     *
     * @return the solution
     */
    Class getSolution();

    /**
     * Gets solution set.
     *
     * @return the solution set
     */
    Class getSolutionSet();

    /**
     * Gets problem.
     *
     * @return the problem
     */
    Class getProblem();

    /**
     * Gets nautilus solution.
     *
     * @return the nautilus solution
     */
    Class getNautilusSolution();

    /**
     * Gets variables as list body.
     *
     * @return the variables as list body
     */
    String getVariablesAsListBody();

    /**
     * Gets txt instance body.
     *
     * @return the txt instance body
     */
    String getTXTInstanceBody();

    /**
     * Gets body.
     *
     * @return the body
     */
    String getBody();

    /**
     * Gets runners.
     *
     * @return the runners
     */
    List<Class> getRunners();
}
