package br.otimizes.isearchai.learning.encoding.integer;

import br.otimizes.isearchai.core.MLSolutionSet;
import org.nautilus.core.encoding.NSolution;

import java.util.Iterator;
import java.util.List;

/**
 * The type Ml integer solution set.
 */
public class MLIntegerSolutionSet extends MLSolutionSet<MLIntegerSolution, MLInteger> {

    /**
     * Instantiates a new Ml integer solution set.
     */
    public MLIntegerSolutionSet() {
    }

    @Override
    public double[][] writeObjectivesAndElementsNumberToMatrix() {
        return this.solutions.stream().map(NSolution::getObjectives).toArray(double[][]::new);
    }

    @Override
    public double[] writeObjectivesFromElements(MLInteger MLElement, MLIntegerSolution MLSolution) {
        return new double[0];
    }

    @Override
    public double[] writeCharacteristicsFromElement(MLInteger MLElement, MLIntegerSolution MLSolution) {
        return new double[]{MLElement.doubleValue()};
    }

    @Override
    public List<MLInteger> getAllElementsFromSolution(MLIntegerSolution MLSolution) {
        return MLSolution.getElements();
    }

    @Override
    public Iterator<MLIntegerSolution> iterator() {
        return solutions.iterator();
    }
}
