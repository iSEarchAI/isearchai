package br.otimizes.isearchai.learning.algorithms.options.nsgaii;

import br.otimizes.isearchai.learning.ml.basis.ArffExecution;
import br.otimizes.isearchai.learning.ml.clustering.DistributeUserEvaluation;
import br.otimizes.isearchai.learning.ml.subjective.EvaluationModels;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.functions.MultilayerPerceptron;

public class SubjectiveAnalyzeMLP implements SubjectiveAnalyzeOptions {
    private double momentum = 0.2;
    private double learningRate = 0.3;
    private String hiddenLayers;
    private int trainingTime = 2500;
    private ArffExecution scoreArffExecution;
    private ArffExecution architecturalArffExecution;

    public ArffExecution getScoreArffExecution() {
        return scoreArffExecution;
    }

    @Override
    public void setScoreArffExecution(ArffExecution scoreArffExecution) {
        this.scoreArffExecution = scoreArffExecution;
    }

    public ArffExecution getArchitecturalArffExecution() {
        return architecturalArffExecution;
    }

    public void setArchitecturalArffExecution(ArffExecution architecturalArffExecution) {
        this.architecturalArffExecution = architecturalArffExecution;
    }

    @Override
    public EvaluationModels getEvaluationModel() {
        return EvaluationModels.CROSS_VALIDATION;
    }

    @Override
    public int getNumFolds() {
        return 10;
    }

    @Override
    public DistributeUserEvaluation getDistributeUserEvaluation() {
        return DistributeUserEvaluation.ALL;
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


    public int getTrainingTime() {
        return trainingTime;
    }

    public void setTrainingTime(int trainingTime) {
        this.trainingTime = trainingTime;
    }


    public AbstractClassifier scoreAlgorithm() {
        MultilayerPerceptron scoreAlgorithm = new MultilayerPerceptron();
        scoreAlgorithm.setHiddenLayers(getHiddenLayers());
        scoreAlgorithm.setTrainingTime(getTrainingTime());
        scoreAlgorithm.setLearningRate(getLearningRate());
        scoreAlgorithm.setMomentum(getMomentum());
        return scoreAlgorithm;
    }

    public AbstractClassifier architecturalAlgorithm() {
        MultilayerPerceptron architecturalAlgorithm = new MultilayerPerceptron();
        architecturalAlgorithm.setHiddenLayers(getArchitecturalAlgorithmHiddenLayers());
        architecturalAlgorithm.setTrainingTime(getTrainingTime());
        architecturalAlgorithm.setLearningRate(getLearningRate());
        architecturalAlgorithm.setMomentum(getMomentum());
        return architecturalAlgorithm;
    }

}
