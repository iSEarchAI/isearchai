package br.otimizes.isearchai.generator.model;

import java.util.List;

public class Generate {
    private List<Objective> objectives;
    private Item item;
    private Solution solution;

    public Generate() {
    }

    public List<Objective> getObjectives() {
        return objectives;
    }

    public void setObjectives(List<Objective> objectives) {
        this.objectives = objectives;
    }


    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Solution getSolution() {
        return solution;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }
}
