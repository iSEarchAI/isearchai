package br.otimizes.isearchai.learning.encoding.binary;

import br.otimizes.isearchai.learning.ml.MLSolutionSet;
import org.nautilus.core.encoding.NSolution;

import java.util.Iterator;
import java.util.List;

public class MLBinarySolutionSet extends MLSolutionSet<MLBinarySolution, MLBinarySet> {

    public MLBinarySolutionSet() {
    }

    @Override
    public double[][] writeObjectivesAndElementsNumberToMatrix() {
        return this.solutions.stream().map(NSolution::getObjectives).toArray(double[][]::new);
    }

    @Override
    public double[] writeObjectivesFromElements(MLBinarySet MLElement, MLBinarySolution MLSolution) {
        return new double[0];
    }

    @Override
    public double[] writeCharacteristicsFromElement(MLBinarySet MLElement, MLBinarySolution MLSolution) {
        return MLElement.stream().asDoubleStream().toArray();
    }

    @Override
    public List<MLBinarySet> getAllElementsFromSolution(MLBinarySolution MLSolution) {
        return MLSolution.getElements();
    }

    @Override
    public Iterator<MLBinarySolution> iterator() {
        return solutions.iterator();
    }
}
