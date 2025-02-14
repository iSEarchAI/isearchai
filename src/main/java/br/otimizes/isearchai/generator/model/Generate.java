package br.otimizes.isearchai.generator.model;

import java.util.List;

/**
 * The type Generate.
 * <p>
 * Specify how is the interactive search approach. Which objective functions there have, how is the solution,
 * the problem, which search algorithm must be used and how the interaction with user will happen.
 */
public class Generate {
    private List<Objective> objectives;
    private Element element;
    private Solution solution;
    private Problem problem;
    private SearchAlgorithm searchAlgorithm;
    private Interaction interaction;

    /**
     * Instantiates a new Generate.
     */
    public Generate() {
    }

    /**
     * Gets objectives.
     *
     * @return the objectives
     */
    public List<Objective> getObjectives() {
        return objectives;
    }

    /**
     * Sets objectives.
     *
     * @param objectives the objectives
     */
    public void setObjectives(List<Objective> objectives) {
        this.objectives = objectives;
    }


    /**
     * Gets element.
     *
     * @return the element
     */
    public Element getElement() {
        return element;
    }

    /**
     * Sets item.
     *
     * @param element the element
     */
    public void setItem(Element element) {
        this.element = element;
    }

    /**
     * Gets solution.
     *
     * @return the solution
     */
    public Solution getSolution() {
        return solution;
    }

    /**
     * Sets solution.
     *
     * @param solution the solution
     */
    public void setSolution(Solution solution) {
        this.solution = solution;
    }

    /**
     * Gets problem.
     *
     * @return the problem
     */
    public Problem getProblem() {
        return problem;
    }

    /**
     * Sets problem.
     *
     * @param problem the problem
     */
    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    /**
     * Gets search algorithm.
     *
     * @return the search algorithm
     */
    public SearchAlgorithm getSearchAlgorithm() {
        return searchAlgorithm;
    }

    /**
     * Sets search algorithm.
     *
     * @param searchAlgorithm the search algorithm
     */
    public void setSearchAlgorithm(SearchAlgorithm searchAlgorithm) {
        this.searchAlgorithm = searchAlgorithm;
    }


    /**
     * Sets element.
     *
     * @param element the element
     */
    public void setElement(Element element) {
        this.element = element;
    }

    /**
     * Gets interaction.
     *
     * @return the interaction
     */
    public Interaction getInteraction() {
        return interaction;
    }

    /**
     * Sets interaction.
     *
     * @param interaction the interaction
     */
    public void setInteraction(Interaction interaction) {
        this.interaction = interaction;
    }
}
