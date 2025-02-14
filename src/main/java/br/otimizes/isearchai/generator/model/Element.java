package br.otimizes.isearchai.generator.model;

import java.util.List;

/**
 * The type Element.
 * <p>
 * In general, every Solution is composed of elements.
 */
public class Element {
    private String name;
    /**
     * The Objectives.
     */
    public List<String> objectives;

    /**
     * Instantiates a new Element.
     */
    public Element() {
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets objectives.
     *
     * @return the objectives
     */
    public List<String> getObjectives() {
        return objectives;
    }

    /**
     * Sets objectives.
     *
     * @param objectives the objectives
     */
    public void setObjectives(List<String> objectives) {
        this.objectives = objectives;
    }
}
