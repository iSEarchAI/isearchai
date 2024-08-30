package br.otimizes.isearchai.generator.model;

import java.util.List;

public class Generate {
    private List<Objective> objectives;
    private Element element;
    private Solution solution;

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
}
