package br.otimizes.isearchai.learning.encoding.binary;

import br.otimizes.isearchai.core.MLSolutionSet;
import org.nautilus.core.encoding.NSolution;

import java.util.Iterator;
import java.util.List;

/**
 * The type Ml binary solution set.
 */
public class MLBinarySolutionSet extends MLSolutionSet<MLBinarySolution, MLBinarySet> {

    /**
     * Instantiates a new Ml binary solution set.
     */
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
