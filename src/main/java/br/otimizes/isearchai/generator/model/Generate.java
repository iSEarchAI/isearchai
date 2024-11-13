package br.otimizes.isearchai.generator.model;

import java.util.List;

public class Generate {
    private List<Objective> objectives;
    private Element element;
    private Solution solution;
    private Problem problem;
    private SearchAlgorithm searchAlgorithm;
    private Interaction interaction;

    public Generate() {
    }

    public List<Objective> getObjectives() {
        return objectives;
    }

    public void setObjectives(List<Objective> objectives) {
        this.objectives = objectives;
    }


    public Element getElement() {
        return element;
    }

    public void setItem(Element element) {
        this.element = element;
    }

    public Solution getSolution() {
        return solution;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public SearchAlgorithm getSearchAlgorithm() {
        return searchAlgorithm;
    }

    public void setSearchAlgorithm(SearchAlgorithm searchAlgorithm) {
        this.searchAlgorithm = searchAlgorithm;
    }


    public void setElement(Element element) {
        this.element = element;
    }

    public Interaction getInteraction() {
        return interaction;
    }

    public void setInteraction(Interaction interaction) {
        this.interaction = interaction;
    }
}
