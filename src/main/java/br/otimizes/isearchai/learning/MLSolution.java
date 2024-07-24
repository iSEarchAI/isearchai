package br.otimizes.isearchai.learning;

import java.io.Serializable;
import java.util.List;

public interface MLSolution<T extends MLElement> {
    public double[] getObjectives();

    public int numberOfObjectives();

    public double getObjective(int i);

    public void setClusterId(Double assignment);

    public String getSolutionName();

    public void setClusterNoise(Boolean b);

    public Double getClusterId();

    public void setEvaluation(int i);

    public List<T> getElements();

    public boolean containsArchitecturalEvaluation();

    public int getEvaluation();

    public List<T> getFreezedElements();

    public List<T> getAllElements();

    public List<T> findElementByNumberId(Double id);
    public <E extends Serializable> E getProblem();
}
