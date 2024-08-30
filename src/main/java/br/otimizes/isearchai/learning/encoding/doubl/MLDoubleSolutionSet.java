package br.otimizes.isearchai.learning.encoding.doubl;

import br.otimizes.isearchai.core.MLSolutionSet;
import org.nautilus.core.encoding.NSolution;

import java.util.Iterator;
import java.util.List;

public class MLDoubleSolutionSet extends MLSolutionSet<MLDoubleSolution, MLDouble> {

    public MLDoubleSolutionSet() {
    }

    @Override
    public double[][] writeObjectivesAndElementsNumberToMatrix() {
        return this.solutions.stream().map(NSolution::getObjectives).toArray(double[][]::new);
    }

    @Override
    public double[] writeObjectivesFromElements(MLDouble MLElement, MLDoubleSolution MLSolution) {
        return new double[0];
    }

    @Override
    public double[] writeCharacteristicsFromElement(MLDouble MLElement, MLDoubleSolution MLSolution) {
        return new double[]{MLElement.doubleValue()};
    }

    @Override
    public List<MLDouble> getAllElementsFromSolution(MLDoubleSolution MLSolution) {
        return MLSolution.getElements();
    }

    @Override
    public Iterator<MLDoubleSolution> iterator() {
        return solutions.iterator();
    }
}
