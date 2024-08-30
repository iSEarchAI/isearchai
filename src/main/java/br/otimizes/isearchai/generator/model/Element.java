package br.otimizes.isearchai.generator.model;

import java.util.List;

public class Element {
    private String name;
    public List<String> objectives;

    public Element() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getObjectives() {
        return objectives;
    }

    public void setObjectives(List<String> objectives) {
        this.objectives = objectives;
    }
}
