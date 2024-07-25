package br.otimizes.isearchai.learning;

import org.apache.commons.lang3.ArrayUtils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public abstract class MLSolutionSet<T extends MLSolution<E>, E extends MLElement> implements Serializable, Iterable<T> {

    protected List<T> solutions = new ArrayList<>();

    /**
     * Copies the objectives and Elements Number of the solution set to a matrix
     * Objectives, nrClasses, nrConcerns, nrInterfaces, nrPackages, nrVariationPoints, nrVariants, nrVariabilities, nrConcerns,
     * nrAbstractions, nrAggregation, nrAssociations, nrCompositions, nrDependencies, nrGeneralizations, nrRealizations, nrUsage
     *
     * @return matrix containing the objectives
     */
    public double[][] writeObjectivesAndElementsNumberToMatrix() {
        return null;
    }

    /**
     * Copies the objectives and Elements Number of the solution set to a matrix
     * Objectives, nrClasses, nrConcerns, nrInterfaces, nrPackages, nrVariationPoints, nrVariants, nrVariabilities, nrConcerns,
     * nrAbstractions, nrAggregations, nrAssociations, nrCompositions, nrDependencies, nrGeneralizations, nrRealizations, nrUsage
     *
     * @return matrix containing the objectives
     */
    public double[][] writeObjectivesAndArchitecturalElementsNumberToMatrix() {
        return reduceThreeDimensionalArray(getSolutionsWithArchitecturalEvaluations().stream()
            .map(this::writeObjectiveWithAllElementsFromSolution).toArray(double[][][]::new));
    }

    /**
     * Copies the objectives and All Elements of a specific set to a matrix
     *
     * @param solution specific solution
     * @return Matrix with values
     */
    private double[][] writeObjectiveWithAllElementsFromSolution(T solution) {
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
    public double[] writeArchitecturalEvaluationsToMatrix() {
        double[][] doubles = getSolutionsWithArchitecturalEvaluations().stream().map(solution -> {
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
    public double[][] writeAllElementsFromSolution(T solution) {
        List<E> allElementsFromSolution = getAllElementsFromSolution(solution);
        return allElementsFromSolution.stream().map(s -> this.writeCharacteristicsFromElement(s, solution)).toArray(double[][]::new);
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
        for (T solution : solutions) {
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
    public List<T> getSolutionsWithArchitecturalEvaluations() {
        return solutions.stream().filter(MLSolution::containsArchitecturalEvaluation).collect(Collectors.toList());
    }

    /**
     * Get architectural elements evaluated in a cluster
     *
     * @param clusterId cluster id
     * @return list of elements
     */

    /**
     * Get solutions with architectural elements evaluated in a cluster
     *
     * @param clusterId cluster id
     * @return list of solutions with architectural elements
     */
    public List<T> getSolutionWithArchitecturalElementsEvaluatedByClusterId(Double clusterId) {
        return getSolutionsWithArchitecturalEvaluations().stream()
            .filter(solution -> clusterId.equals(solution.getClusterId())).collect(Collectors.toList());
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
            List<T> solutionsList_ = solutions;
            if (DistributeUserEvaluation.MIDDLE.equals(distributeUserEvaluation))
                solutionsList_ = solutionsList_.subList(0, Math.abs(solutionsList_.size() / 2));
            for (T solution : solutionsList_) {
                if (solution.getEvaluation() == 0) {
                    int media = Math.abs(getMedia(clusterIds.get(solution.getClusterId())));
                    solution.setEvaluation(media);
                }
            }
        }
    }

    /**
     * freeze solutions according the clusters in a solution
     *
     * @param solution specific solution
     * @return solution with elements
     */
    private T freezeArchitecturalElementsAccordingCluster(T solution) {
        if (!solution.containsArchitecturalEvaluation()) {
            List<T> solutions
                = getSolutionWithArchitecturalElementsEvaluatedByClusterId(solution.getClusterId());
            if (solutions.size() > 0) {
                solution = solutions.get(0);
            }
        }
        return solution;
    }

    /**
     * Freeze the architectural elements according the solution
     *
     * @param solution solution with elements
     */
    public abstract void freezeArchitecturalElementsAccordingSolution(T solution);

    /**
     * Find elements with a id
     *
     * @param id hash id
     * @return filtered elements
     */
    public abstract List<E> findElementWithNumberId(Double id);

    public double[] getNormalizedSolution(int i) {
        T solution = solutions.get(i);
        T max = getMax();
        T min = getMin();
        double[] doubles = new double[solution.getObjectives().length];
        if (solutions.size() == 1) return doubles;
        for (int j = 0; j < solution.getObjectives().length; j++) {
            doubles[j] = (max.getObjective(j) - min.getObjective(j)) == 0 ? 0 :
                (solution.getObjective(j) - min.getObjective(j)) / (max.getObjective(j) - min.getObjective(j));
            if (doubles[j] == -0.0) doubles[j] = 0.0;
        }
        return doubles;
    }

    public T getMin() {
        T solution = solutions.get(0);
        for (int i = 0; i < solution.getObjectives().length; i++) {
            for (T otherMLSolution : solutions) {
                if (otherMLSolution.getObjective(i) <= solution.getObjective(i)) {
                    solution = otherMLSolution;
                }
            }
        }
        return solution;
    }

    public T getMax() {
        T solution = solutions.get(0);
        for (int i = 0; i < solution.getObjectives().length; i++) {
            for (T otherMLSolution : solutions) {
                if (otherMLSolution.getObjective(i) >= solution.getObjective(i)) {
                    solution = otherMLSolution;
                }
            }
        }
        return solution;
    }

    public T get(int i) {
        return this.solutions.get(i);
    }

    public List<T> getSolutions() {
        return solutions;
    }

    public void setSolutions(List<T> MLSolutions) {
        this.solutions = MLSolutions;
    }

    public int size() {
        return this.solutions.size();
    }

    public void remove(Integer integer) {
        this.solutions.remove(integer);
    }

    public void addAll(MLSolutionSet<T, E> MLSolutionSet) {
        this.solutions.addAll(MLSolutionSet.getSolutions());
    }


    public abstract double[] writeObjectiveFromElementsAndObjectives(E MLElement, T MLSolution);

    public abstract double[] writeCharacteristicsFromElement(E MLElement, T MLSolution);

    public abstract List<E> getAllElementsFromSolution(T MLSolution);

    public abstract double[][] writeObjectivesToMatrix();

    public abstract List<E> getArchitecturalElementsEvaluatedByClusterId(Double clusterId);

    public boolean add(T solution) {
        return this.solutions.add(solution);
    }
}