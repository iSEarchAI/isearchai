package br.otimizes.isearchai.generator.model;

/**
 * The type Objective.
 */
public class Objective {
    private String name;
    private String type;
    private Process process;
    private Calculate calculate;
    private Boolean maximize;

    /**
     * Instantiates a new Objective.
     */
    public Objective() {
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
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets process.
     *
     * @return the process
     */
    public Process getProcess() {
        return process;
    }

    /**
     * Sets process.
     *
     * @param process the process
     */
    public void setProcess(Process process) {
        this.process = process;
    }

    /**
     * Gets calculate.
     *
     * @return the calculate
     */
    public Calculate getCalculate() {
        return calculate;
    }

    /**
     * Sets calculate.
     *
     * @param calculate the calculate
     */
    public void setCalculate(Calculate calculate) {
        this.calculate = calculate;
    }

    /**
     * Gets maximize.
     *
     * @return the maximize
     */
    public Boolean getMaximize() {
        return maximize != null && maximize;
    }

    /**
     * Sets maximize.
     *
     * @param maximize the maximize
     */
    public void setMaximize(Boolean maximize) {
        this.maximize = maximize;
    }
}
