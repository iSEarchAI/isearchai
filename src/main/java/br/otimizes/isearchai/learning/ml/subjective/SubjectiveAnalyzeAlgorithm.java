package br.otimizes.isearchai.learning.ml.subjective;

import br.otimizes.isearchai.core.MLElement;
import br.otimizes.isearchai.core.MLSolution;
import br.otimizes.isearchai.core.MLSolutionSet;
import br.otimizes.isearchai.learning.algorithms.options.nsgaii.SubjectiveAnalyzeOptions;
import br.otimizes.isearchai.learning.ml.basis.ArffExecution;
import br.otimizes.isearchai.learning.ml.clustering.DistributeUserEvaluation;
import org.apache.commons.lang3.ArrayUtils;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Evaluation;
import weka.core.DenseInstance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Subjective analyze algorithm by Freire and Bindewald (2020)
 */
public class SubjectiveAnalyzeAlgorithm {
    //https://stackoverflow.com/questions/28694971/using-neural-network-class-in-weka-in-java-code
    private static final long serialVersionUID = 1L;

    public SubjectiveAnalyzeOptions options;
    private MLSolutionSet resultFront;
    private ClassifierAlgorithm algorithm;
    private ArffExecution scoreArffExecution;
    private AbstractClassifier scoreAlgorithm;
    private ArffExecution architecturalArffExecution;
    private AbstractClassifier architecturalAlgorithm;
    private List<MLSolutionSet> interactions = new ArrayList<>();
    private boolean trained = false;
    public static int currentEvaluation = 0;
    List<MLElement> freezedMLElements = new ArrayList<>();
    List<MLElement> notFreezedMLElements = new ArrayList<>();

    public SubjectiveAnalyzeAlgorithm() {
    }

    public SubjectiveAnalyzeAlgorithm(MLSolutionSet resultFront, SubjectiveAnalyzeOptions options) {
        this.resultFront = resultFront;
        distributeUserEvaluations(resultFront);
        this.scoreArffExecution = new ArffExecution(resultFront.writeObjectivesAndElementsNumberToMatrix(),
            resultFront.writeUserEvaluationsToMatrix(), null);
        this.architecturalArffExecution = new ArffExecution(resultFront.writeObjectivesAndAllElementsNumberToMatrix(),
            resultFront.writeElementsEvaluationsToMatrix(), null, true);
        this.options = options;
        updateArffExecutionOptions();
    }

    private void updateArffExecutionOptions() {
        this.options.setScoreArffExecution(scoreArffExecution);
        this.options.setArchitecturalArffExecution(architecturalArffExecution);
    }

    private void distributeUserEvaluations(MLSolutionSet resultFront) {
        if (ClassifierAlgorithm.CLUSTERING_MLP.equals(this.algorithm)) {
            resultFront.distributeUserEvaluation(options.getDistributeUserEvaluation());
        }
    }

    /**
     * Execution Method
     *
     * @return Solution Set - Best performing cluster with another solutions (filteredSolutions)
     */
    public MLSolutionSet run(MLSolutionSet MLSolutionSet, boolean middle) {
        try {
            return getAlgorithm(MLSolutionSet, middle);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void run() throws Exception {
        run(null, false);
        for (MLSolutionSet interaction : interactions) {
            run(interaction, false);
        }
    }

    /**
     * Algorithm Execution Method
     *
     * @return Solution Set
     */
    public MLSolutionSet getAlgorithm(MLSolutionSet MLSolutionSet, boolean inOnMiddle) {
        currentEvaluation++;
        if (scoreAlgorithm == null) {
            scoreAlgorithm = newScoreAlgorithm();
            architecturalAlgorithm = newArchitecturalAlgorithm();
        }

        if (MLSolutionSet != null) {
            joinSolutionSet(MLSolutionSet, inOnMiddle);
        }
        setArffExecutionClassIndex(scoreArffExecution);
        setArffExecutionClassIndex(architecturalArffExecution);
        startAlgorithms(inOnMiddle);

        return resultFront;
    }


    private AbstractClassifier newScoreAlgorithm() {
        return options.scoreAlgorithm();
    }

    private AbstractClassifier newArchitecturalAlgorithm() {
        return options.architecturalAlgorithm();
    }

    private void joinSolutionSet(MLSolutionSet MLSolutionSet, boolean inOnMiddle) {
        if (!MLSolutionSet.hasUserEvaluation()) {
            evaluateSolutionSetScoreAlgorithm(MLSolutionSet);
        } else {
            distributeUserEvaluations(MLSolutionSet);
        }
        if (!inOnMiddle) {
            ArffExecution newArffArchitectureAlgorithm = new ArffExecution(resultFront.writeObjectivesAndAllElementsNumberToMatrix(),
                resultFront.writeElementsEvaluationsToMatrix(), null, true);
            if (newArffArchitectureAlgorithm.getData() != null) {
                newArffArchitectureAlgorithm.getData().setClassIndex(newArffArchitectureAlgorithm.getAttrIndices());
                if (architecturalArffExecution.getData() == null)
                    architecturalArffExecution = newArffArchitectureAlgorithm;
                else architecturalArffExecution.getData().addAll(newArffArchitectureAlgorithm.getData());
            }
        }
        ArffExecution newArffScoreAlgorithm = new ArffExecution(MLSolutionSet.writeObjectivesAndElementsNumberToMatrix(),
            MLSolutionSet.writeUserEvaluationsToMatrix(), null);
        newArffScoreAlgorithm.getData().setClassIndex(newArffScoreAlgorithm.getAttrIndices());
        resultFront.addAll(MLSolutionSet);
        scoreArffExecution.getData().addAll(newArffScoreAlgorithm.getData());
        updateArffExecutionOptions();
    }

    private void setArffExecutionClassIndex(ArffExecution subjectiveArffExecution) {
        if (subjectiveArffExecution.getData() != null)
            subjectiveArffExecution.getData().setClassIndex(subjectiveArffExecution.getAttrIndices());
    }


    private void startAlgorithms(boolean inOnMiddle) {
        Thread threadScoreAlgorithm = new Thread(this::buildScoreAlgorithm);
        threadScoreAlgorithm.start();
        Thread threadArchitecturalAlgorithm = null;
        if (!inOnMiddle) {
            threadArchitecturalAlgorithm = new Thread(this::buildArchitecturalAlgorithm);
            threadArchitecturalAlgorithm.start();
        }

        try {
            threadScoreAlgorithm.join();
            if (threadArchitecturalAlgorithm != null) {
                threadArchitecturalAlgorithm.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void evaluateSolutionSetScoreAlgorithm(MLSolutionSet MLSolutionSet) {
        for (int i = 0; i < MLSolutionSet.size(); i++) {
            try {
                MLSolutionSet.get(i).setEvaluation((int) scoreAlgorithm
                    .classifyInstance(new DenseInstance(1.0, MLSolutionSet.writeObjectivesAndElementsNumberEvaluationToMatrix()[i])));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Evaluate subjectively using Likert score and architectural elements evaluation
     *
     * @param MLSolutionSet solution set
     * @param subjective    assign or not the Likert score
     * @throws Exception default exception
     */
    public void evaluateSolutionSetScoreAndArchitecturalAlgorithm(MLSolutionSet MLSolutionSet, boolean subjective) {
        for (int i = 0; i < MLSolutionSet.size(); i++) {
            MLSolution MLSolution = MLSolutionSet.get(i);
            double[] solutionMatrix = MLSolutionSet.writeObjectivesAndElementsNumberEvaluationToMatrix()[i];
            if (subjective) {
                try {
                    MLSolution.setEvaluation((int) scoreAlgorithm.classifyInstance(new DenseInstance(1.0, solutionMatrix)));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            List<MLElement> elementsWithPackages = MLSolution.getElements();
            elementsWithPackages.parallelStream().forEach(element -> {
                double[] data = getDataSet(MLSolutionSet, MLSolution, element);
                if (architecturalArffExecution.getData() != null) {
                    DenseInstance denseInstance = new DenseInstance(1.0, data);
                    denseInstance.setDataset(architecturalArffExecution.getData());
                    try {
                        element.setFreezeFromDM(architecturalAlgorithm.classifyInstance(denseInstance) > 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (element.isFreezeByDM()) {
                        this.freezedMLElements.add(element);
                    } else {
                        this.notFreezedMLElements.add(element);
                    }
                }
            });
        }
    }

    private double[] getDataSet(MLSolutionSet MLSolutionSet, MLSolution MLSolution, MLElement MLElement) {
        double[] data = MLSolution.getObjectives();
        double[] objectives = MLSolutionSet.writeObjectivesFromElements(MLElement, MLSolution);
        double[] characteristics = MLSolutionSet.writeCharacteristicsFromElement(MLElement, MLSolution);

        data = ArrayUtils.addAll(data, characteristics);
        data = ArrayUtils.addAll(data, objectives);
        data = ArrayUtils.addAll(data, MLSolution.containsElementsEvaluation() ? 1 : 0,
            MLElement.isFreezeByDM() ? 1 : 0);
        return data;
    }

    private void buildArchitecturalAlgorithm() {
        if (architecturalArffExecution.getData() == null) return;
        try {
            Instances data = architecturalArffExecution.getData();
            int trainSize = Math.toIntExact(Math.round(data.numInstances() * getRatioTest()));
            int testSize = data.numInstances() - trainSize;
            Instances train = new Instances(data, 0, trainSize);
            Instances test = new Instances(data, trainSize, testSize);
            architecturalAlgorithm.buildClassifier(train);
            options.getEvaluationModel().build(architecturalAlgorithm, test, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Double getRatioTest() {
        return 0.66;
    }

    private void buildScoreAlgorithm() {
        if (scoreArffExecution.getData() == null) return;
        try {
            scoreAlgorithm.buildClassifier(scoreArffExecution.getData());
            options.getEvaluationModel().build(scoreAlgorithm, scoreArffExecution.getData(), options);
            logSubjectiveModelEvaluation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logSubjectiveModelEvaluation() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public ClassifierAlgorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(ClassifierAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public List<MLSolutionSet> getInteractions() {
        return interactions;
    }

    public void setInteractions(List<MLSolutionSet> interactions) {
        this.interactions = interactions;
    }

    public boolean isTrained() {
        return trained;
    }

    public void setTrained(boolean trained) {
        this.trained = trained;
    }
}
