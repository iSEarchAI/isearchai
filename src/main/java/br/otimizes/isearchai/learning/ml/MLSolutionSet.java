package br.otimizes.isearchai.learning.ml;

import org.apache.commons.lang3.ArrayUtils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public abstract class MLSolutionSet<S extends MLSolution<E>, E extends MLElement> implements Serializable, Iterable<S> {

    protected List<S> solutions = new ArrayList<>();

    /**
     * Copies the objectives and Elements Number of the solution set to a matrix
     * Objectives, nrClasses, nrConcerns, nrInterfaces, nrPackages, nrVariationPoints, nrVariants, nrVariabilities, nrConcerns,
     * nrAbstractions, nrAggregation, nrAssociations, nrCompositions, nrDependencies, nrGeneralizations, nrRealizations, nrUsage
     *
     * @return matrix containing the objectives
     */
    public abstract double[][] writeObjectivesAndElementsNumberToMatrix();

    /**
     * Copies the objectives and Elements Number of the solution set to a matrix
     * Objectives, nrClasses, nrConcerns, nrInterfaces, nrPackages, nrVariationPoints, nrVariants, nrVariabilities, nrConcerns,
     * nrAbstractions, nrAggregations, nrAssociations, nrCompositions, nrDependencies, nrGeneralizations, nrRealizations, nrUsage
     *
     * @return matrix containing the objectives
     */
    public double[][] writeObjectivesAndAllElementsNumberToMatrix() {
        return reduceThreeDimensionalArray(getSolutionsWithElementsEvaluations().stream()
            .map(this::writeObjectiveWithAllElementsFromSolution).toArray(double[][][]::new));
    }

    /**
     * Copies the objectives and All Elements of a specific set to a matrix
     *
     * @param solution specific solution
     * @return Matrix with values
     */
    private double[][] writeObjectiveWithAllElementsFromSolution(S solution) {
        double[] objectives = solution.getObjectives();
        double[][] values = writeAllElementsFromSolution(solution);
        double[][] newValues = new double[values.length][];
        int i = 0;
        for (double[] value : values) {
            double[] newArray = new double[objectives.length + value.length];
            System.arraycopy(objectives, 0, newArray, 0, objectives.length);
            System.arraycopy(value, 0, newArray, objectives.length, value.length);
            newValues[i] = newArray;
            i++;
        }
        return newValues;
    }

    /**
     * Generate the Solution from elements and get the objective values
     *
     * @param element  specific element to add in solution
     * @param solution specific solution
     * @return list of objectives
     */

    /**
     * Copies the objectives and Elements Number of the solution set to a matrix
     * Objectives, nrClasses, nrConcerns, nrInterfaces, nrPackages, nrVariationPoints, nrVariants, nrVariabilities, nrConcerns,
     * nrAbstractions, nrAggregations, nrAssociations, nrCompositions, nrDependencies, nrGeneralizations, nrRealizations, nrUsage
     *
     * @return A matrix containing the objectives
     */
    public double[] writeElementsEvaluationsToMatrix() {
        double[][] doubles = getSolutionsWithElementsEvaluations().stream().map(solution -> {
            List<E> allElementsFromSolution = getAllElementsFromSolution(solution);
            return allElementsFromSolution.stream().mapToDouble(MLElement -> MLElement.isFreezeByDM() ? 1.0 : 0.0).toArray();
        }).toArray(double[][]::new);
        return reduceBiDimensionalArray(doubles);
    }

    /**
     * Reduce one dimensional in three dimensional array
     *
     * @param treeDimensionalArray array of objectives
     * @return bi-dimensional array of objectives
     */
    public double[][] reduceThreeDimensionalArray(double[][][] treeDimensionalArray) {
        if (treeDimensionalArray.length <= 0) return new double[][]{};
        double[][] twoDimensionalArray = treeDimensionalArray[0];
        for (int i = 1; i < treeDimensionalArray.length; i++) {
            twoDimensionalArray = (double[][]) ArrayUtils.addAll(twoDimensionalArray, treeDimensionalArray[i]);
        }
        return twoDimensionalArray;
    }

    /**
     * Reduce one dimension from bi dimensional array
     *
     * @param biDimensionalArray array of objectives
     * @return one-dimensional array of objectives
     */
    public double[] reduceBiDimensionalArray(double[][] biDimensionalArray) {
        if (biDimensionalArray.length <= 0) return new double[]{};
        double[] oneDimensionalArray = biDimensionalArray[0];
        for (int i = 1; i < biDimensionalArray.length; i++) {
            oneDimensionalArray = ArrayUtils.addAll(oneDimensionalArray, biDimensionalArray[i]);
        }
        return oneDimensionalArray;
    }

    /**
     * Get all elements from solutions
     *
     * @param solution specific solution
     * @return array of elements
     */
    public double[][] writeAllElementsFromSolution(S solution) {
        List<E> allElementsFromSolution = getAllElementsFromSolution(solution);
        return allElementsFromSolution.stream().map(s -> this.writeCharacteristicsFromElement(s, solution)).toArray(double[][]::new);
    }


    public MLSolutionSet newInstance(List solutions) {
        try {
            MLSolutionSet clone = this.getClass().newInstance();
            clone.setSolutions(solutions);
            return clone;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get characteristics from element in a solution (number id, element type, nr of classes, interfaces, attrs and methods, objectives, user evaluation)
     *
     * @param element  specific in a solution
     * @param solution specific solution
     * @return array of characteristics
     */

    /**
     * Get all elements from solution
     *
     * @param solution specific solution
     * @return list of elements
     */

    /**
     * Method to get a string of objectives and elements number. Used to create CSV files
     *
     * @param startFrom Number of objectives
     * @return List of elements. If startFrom > 0, then specify the objectives number
     */
    public String toStringObjectivesAndElementsNumber(int startFrom) {
        return Arrays.stream(writeObjectivesAndElementsNumberToMatrix()).map(p -> Arrays
            .asList(ArrayUtils.toObject(Arrays.copyOfRange(p, startFrom, p.length))).toString()
            .replace("]", "\n").replace("[", "").replaceAll("\\.0", "")
            .replaceAll(" ", "")).collect(Collectors.joining());
    }

    /**
     * Create a list from objectives. Used to create CSV Files
     *
     * @param interaction interaction Number
     * @return list of objectives
     */
    public String toStringObjectives(String interaction) {
        return Arrays.stream(writeObjectivesToMatrix()).map(p -> Arrays.asList(ArrayUtils.toObject(p))
            .toString().replace("]", interaction + "," + interaction + "\n").replace(",", "|")
            .replace("[", interaction + "," + interaction + ",").replaceAll("\\.0", "")
            .replaceAll(" ", "")).collect(Collectors.joining());
    }


    /**
     * Get objectives and elements number with evaluation
     *
     * @return array of objectives with elements and user evaluation
     */
    public double[][] writeObjectivesAndElementsNumberEvaluationToMatrix() {
        double[][] doubles = writeObjectivesAndElementsNumberToMatrix();
        for (int i = 0; i < doubles.length; i++) {
            doubles[i] = Arrays.copyOf(doubles[i], doubles[i].length + 1);
            doubles[i][doubles[i].length - 1] = solutions.get(i).getEvaluation();
        }
        return doubles;
    }

    /**
     * Get user evaluations list
     *
     * @return array of user evaluations
     */
    public double[] writeUserEvaluationsToMatrix() {
        double[] doubles = new double[solutions.size()];
        for (int i = 0; i < solutions.size(); i++) {
            doubles[i] = solutions.get(i).getEvaluation();
        }
        return doubles;
    }

    /**
     * Verify if has user evaluation
     *
     * @return true if has. false if has'nt
     */
    public boolean hasUserEvaluation() {
        double[] doubles = writeUserEvaluationsToMatrix();
        for (double aDouble : doubles) {
            if (aDouble > 0) return true;
        }
        return false;
    }

    /**
     * Get list of cluster ids
     *
     * @return array of cluster ids
     */
    public Map<Double, Set<Integer>> getClusterIds() {
        Map<Double, Set<Integer>> clusters = new HashMap<>();
        for (S solution : solutions) {
            if (solution.getClusterId() != null) {
                Set<Integer> clusterId = clusters.getOrDefault(solution.getClusterId(), new HashSet<>());
                clusterId.add(solution.getEvaluation());
                clusters.put(solution.getClusterId(), clusterId);
            }
        }
        return clusters;
    }

    /**
     * Get average of values
     *
     * @param values values
     * @return average of values
     */
    public int getMedia(Set<Integer> values) {
        if (values == null) return 0;
        values = values.stream().filter(v -> v > 0).collect(Collectors.toSet());
        if (values.size() == 0) return 0;
        if (values.size() == 1) return values.stream().findFirst().get();
        int soma = 0;
        for (Integer value : values) {
            soma += value;
        }
        return soma / values.size();
    }

    /**
     * Get solutions that have architectural evaluations
     *
     * @return solutions with architectural evaluations
     */
    public List<S> getSolutionsWithElementsEvaluations() {
        return solutions.stream().filter(MLSolution::containsElementsEvaluation).collect(Collectors.toList());
    }

    /**
     * Generalize the evaluatios in a cluster (see approaches in Bindewald, 2020)
     *
     * @param distributeUserEvaluation Approach
     */
    public void distributeUserEvaluation(DistributeUserEvaluation distributeUserEvaluation) {
        if (DistributeUserEvaluation.NONE.equals(distributeUserEvaluation)) return;
        Map<Double, Set<Integer>> clusterIds = getClusterIds();
        if (hasUserEvaluation() && clusterIds.size() > 0) {
            List<S> solutionsList_ = solutions;
            if (DistributeUserEvaluation.MIDDLE.equals(distributeUserEvaluation))
                solutionsList_ = solutionsList_.subList(0, Math.abs(solutionsList_.size() / 2));
            for (S solution : solutionsList_) {
                if (solution.getEvaluation() == 0) {
                    int media = Math.abs(getMedia(clusterIds.get(solution.getClusterId())));
                    solution.setEvaluation(media);
                }
            }
        }
    }

    public S getMin() {
        S solution = solutions.get(0);
        for (int i = 0; i < solution.getObjectives().length; i++) {
            for (S otherMLSolution : solutions) {
                if (otherMLSolution.getObjective(i) <= solution.getObjective(i)) {
                    solution = otherMLSolution;
                }
            }
        }
        return solution;
    }

    public S getMax() {
        S solution = solutions.get(0);
        for (int i = 0; i < solution.getObjectives().length; i++) {
            for (S otherMLSolution : solutions) {
                if (otherMLSolution.getObjective(i) >= solution.getObjective(i)) {
                    solution = otherMLSolution;
                }
            }
        }
        return solution;
    }

    public S get(int i) {
        return this.solutions.get(i);
    }

    public List<S> getSolutions() {
        return solutions;
    }

    public void setSolutions(List<S> MLSolutions) {
        this.solutions = MLSolutions;
    }

    public int size() {
        return this.solutions.size();
    }

    public void remove(Integer integer) {
        this.solutions.remove(integer);
    }

    public void addAll(MLSolutionSet<S, E> MLSolutionSet) {
        this.solutions.addAll(MLSolutionSet.getSolutions());
    }

    /**
     * Copies the objectives of the solution set to a matrix
     *
     * @return A matrix containing the objectives
     */
    public double[][] writeObjectivesToMatrix() {
        if (this.size() == 0) {
            return null;
        }
        double[][] objectives;
        objectives = new double[size()][get(0).numberOfObjectives()];
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < get(0).numberOfObjectives(); j++) {
                objectives[i][j] = get(i).getObjective(j);
            }
        }
        return objectives;
    }

    public boolean add(S solution) {
        return this.solutions.add(solution);
    }

    public abstract double[] writeObjectivesFromElements(E MLElement, S MLSolution);

    public abstract double[] writeCharacteristicsFromElement(E MLElement, S MLSolution);

    public abstract List<E> getAllElementsFromSolution(S MLSolution);


    @Override
    public Iterator<S> iterator() {
        return solutions.iterator();
    }
}
