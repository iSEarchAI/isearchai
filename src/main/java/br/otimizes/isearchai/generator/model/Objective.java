package br.otimizes.isearchai.generator.model;

public class Objective {
    private String name;
    private String type;
    private Process process;
    private Calculate calculate;
    private Boolean maximize;

    public Objective() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public Calculate getCalculate() {
        return calculate;
    }

    public void setCalculate(Calculate calculate) {
        this.calculate = calculate;
    }

    public Boolean getMaximize() {
        return maximize != null && maximize;
    }

    public void setMaximize(Boolean maximize) {
        this.maximize = maximize;
    }
}
