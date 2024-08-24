package br.otimizes.isearchai.learning.ml.subjective;

import br.otimizes.isearchai.learning.ml.basis.ArffExecution;
import br.otimizes.isearchai.learning.ml.interfaces.MLSolutionSet;
import br.otimizes.isearchai.learning.ml.clustering.DistributeUserEvaluation;
import br.otimizes.isearchai.learning.ml.interfaces.MLElement;
import br.otimizes.isearchai.learning.ml.interfaces.MLSolution;
import org.apache.commons.lang3.ArrayUtils;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
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

    private MLSolutionSet resultFront;
    private ClassifierAlgorithm algorithm;
    private int numObjectives;
    private ArffExecution scoreArffExecution;
    private AbstractClassifier scoreAlgorithm;
    private Evaluation scoreEvaluation;
    private ArffExecution architecturalArffExecution;
    private AbstractClassifier architecturalAlgorithm;
    private Evaluation architectureEvaluation;
    private int trainingTime = 2500;
    private DistributeUserEvaluation distributeUserEvaluation = DistributeUserEvaluation.ALL;
    private EvaluationModels evaluationModel = EvaluationModels.CROSS_VALIDATION;
    private double momentum = 0.2;
    private double learningRate = 0.3;
    private String hiddenLayers;
    private List<MLElement> evaluatedMLElements;
    private List<MLSolutionSet> interactions = new ArrayList<>();
    private boolean trained = false;
    public static int currentEvaluation = 0;
    List<MLElement> freezedMLElements = new ArrayList<>();
    List<MLElement> notFreezedMLElements = new ArrayList<>();

    public SubjectiveAnalyzeAlgorithm() {
    }

    public SubjectiveAnalyzeAlgorithm(MLSolutionSet resultFront, ClassifierAlgorithm algorithm, DistributeUserEvaluation distributeUserEvaluation) {
        this.distributeUserEvaluation = distributeUserEvaluation;
        this.resultFront = resultFront;
        this.algorithm = algorithm;
        distributeUserEvaluations(resultFront);
        this.scoreArffExecution = new ArffExecution(resultFront.writeObjectivesAndElementsNumberToMatrix(),
            resultFront.writeUserEvaluationsToMatrix(), null);
        this.architecturalArffExecution = new ArffExecution(resultFront.writeObjectivesAndAllElementsNumberToMatrix(),
            resultFront.writeElementsEvaluationsToMatrix(), null, true);
        this.numObjectives = this.resultFront.get(0).numberOfObjectives();
    }

    public SubjectiveAnalyzeAlgorithm(MLSolutionSet resultFront, ClassifierAlgorithm algorithm) {
        this.resultFront = resultFront;
        this.algorithm = algorithm;
        distributeUserEvaluations(resultFront);
        this.scoreArffExecution = new ArffExecution(resultFront.writeObjectivesAndElementsNumberToMatrix(),
            resultFront.writeUserEvaluationsToMatrix(), null);
        this.architecturalArffExecution = new ArffExecution(resultFront.writeObjectivesAndAllElementsNumberToMatrix(),
            resultFront.writeElementsEvaluationsToMatrix(), null, true);
        this.numObjectives = this.resultFront.get(0).numberOfObjectives();
    }

    private void distributeUserEvaluations(MLSolutionSet resultFront) {
        if (ClassifierAlgorithm.CLUSTERING_MLP.equals(this.algorithm)) {
            resultFront.distributeUserEvaluation(distributeUserEvaluation);
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
        long startsIn = new Date().getTime();
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


    private MultilayerPerceptron newScoreAlgorithm() {
        MultilayerPerceptron scoreAlgorithm = new MultilayerPerceptron();
        scoreAlgorithm.setHiddenLayers(getHiddenLayers());
        scoreAlgorithm.setTrainingTime(getTrainingTime());
        scoreAlgorithm.setLearningRate(getLearningRate());
        scoreAlgorithm.setMomentum(getMomentum());
        return scoreAlgorithm;
    }

    private MultilayerPerceptron newArchitecturalAlgorithm() {
        MultilayerPerceptron architecturalAlgorithm = new MultilayerPerceptron();
        architecturalAlgorithm.setHiddenLayers(getArchitecturalAlgorithmHiddenLayers());
        architecturalAlgorithm.setTrainingTime(getTrainingTime());
        architecturalAlgorithm.setLearningRate(getLearningRate());
        architecturalAlgorithm.setMomentum(getMomentum());
        return architecturalAlgorithm;
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
        data = ArrayUtils.addAll(data, new double[]{
            MLSolution.containsElementsEvaluation() ? 1 : 0,
            MLElement.isFreezeByDM() ? 1 : 0
        });
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
            architectureEvaluation = new Evaluation(test);
            switch (evaluationModel) {
                case TRAINING_SET:
                    architectureEvaluation.evaluateModel(architecturalAlgorithm, test);
                    break;
                case CROSS_VALIDATION:
                    architectureEvaluation.crossValidateModel(architecturalAlgorithm, test, 10, new Random(1));
                    break;
            }
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
            scoreEvaluation = new Evaluation(scoreArffExecution.getData());
            switch (evaluationModel) {
                case TRAINING_SET:
                    scoreEvaluation.evaluateModel(scoreAlgorithm, scoreArffExecution.getData());
                    break;
                case CROSS_VALIDATION:
                    scoreEvaluation.crossValidateModel(scoreAlgorithm, scoreArffExecution.getData(), 5, new Random(1));
                    break;
            }
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


    public MLSolutionSet getResultFront() {
        return resultFront;
    }

    public void setResultFront(MLSolutionSet resultFront) {
        this.resultFront = resultFront;
    }

    public ClassifierAlgorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(ClassifierAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public ArffExecution getScoreArffExecution() {
        return scoreArffExecution;
    }

    public void setScoreArffExecution(ArffExecution scoreArffExecution) {
        this.scoreArffExecution = scoreArffExecution;
    }

    public int getNumObjectives() {
        return numObjectives;
    }

    public void setNumObjectives(int numObjectives) {
        this.numObjectives = numObjectives;
    }


    public AbstractClassifier getScoreAlgorithm() {
        return scoreAlgorithm;
    }

    public void setScoreAlgorithm(AbstractClassifier scoreAlgorithm) {
        this.scoreAlgorithm = scoreAlgorithm;
    }

    public int getTrainingTime() {
        return trainingTime;
    }

    public void setTrainingTime(int trainingTime) {
        this.trainingTime = trainingTime;
    }

    public DistributeUserEvaluation getDistributeUserEvaluation() {
        return distributeUserEvaluation;
    }

    public void setDistributeUserEvaluation(DistributeUserEvaluation distributeUserEvaluation) {
        this.distributeUserEvaluation = distributeUserEvaluation;
    }

    public EvaluationModels getEvaluationModel() {
        return evaluationModel;
    }

    public void setEvaluationModel(EvaluationModels evaluationModel) {
        this.evaluationModel = evaluationModel;
    }

    public double getMomentum() {
        return momentum;
    }

    public void setMomentum(double momentum) {
        this.momentum = momentum;
    }

    public double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public String getHiddenLayers() {
        return hiddenLayers == null ? String.valueOf(Math.round(scoreArffExecution.getAttrIndices())) : hiddenLayers;
    }

    public String getArchitecturalAlgorithmHiddenLayers() {
        return hiddenLayers == null ? String.valueOf(Math.round(architecturalArffExecution.getAttrIndices())) : hiddenLayers;
    }

    public void setHiddenLayers(String hiddenLayers) {
        this.hiddenLayers = hiddenLayers;
    }

    public ArffExecution getArchitecturalArffExecution() {
        return architecturalArffExecution;
    }

    public void setArchitecturalArffExecution(ArffExecution architecturalArffExecution) {
        this.architecturalArffExecution = architecturalArffExecution;
    }

    public List<MLElement> getEvaluatedElements() {
        return evaluatedMLElements;
    }

    public void setEvaluatedElements(List<MLElement> evaluatedMLElements) {
        this.evaluatedMLElements = evaluatedMLElements;
    }

    public void addInteraction(MLSolutionSet offspringPopulation) {
        interactions.add(offspringPopulation);
    }

    public AbstractClassifier getArchitecturalAlgorithm() {
        return architecturalAlgorithm;
    }

    public void setArchitecturalAlgorithm(MultilayerPerceptron architecturalAlgorithm) {
        this.architecturalAlgorithm = architecturalAlgorithm;
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

    public Evaluation getScoreEvaluation() {
        return scoreEvaluation;
    }

    public void setScoreEvaluation(Evaluation scoreEvaluation) {
        this.scoreEvaluation = scoreEvaluation;
    }

    public Evaluation getArchitectureEvaluation() {
        return architectureEvaluation;
    }

    public void setArchitectureEvaluation(Evaluation architectureEvaluation) {
        this.architectureEvaluation = architectureEvaluation;
    }

    public List<MLElement> getFreezedElements() {
        return freezedMLElements;
    }

    public void setFreezedElements(List<MLElement> freezedMLElements) {
        this.freezedMLElements = freezedMLElements;
    }

    public List<MLElement> getNotFreezedElements() {
        return notFreezedMLElements;
    }

    public void setNotFreezedElements(List<MLElement> notFreezedMLElements) {
        this.notFreezedMLElements = notFreezedMLElements;
    }
}
