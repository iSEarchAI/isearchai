package br.otimizes.isearchai.learning.algorithms.options.nsgaii;

import br.otimizes.isearchai.learning.ml.basis.ArffExecution;
import br.otimizes.isearchai.learning.ml.clustering.DistributeUserEvaluation;
import br.otimizes.isearchai.learning.ml.subjective.EvaluationModels;
import weka.classifiers.AbstractClassifier;

/**
 * The interface Subjective analyze options.
 */
public interface SubjectiveAnalyzeOptions {
    /**
     * Score algorithm abstract classifier.
     *
     * @return the abstract classifier
     */
    AbstractClassifier scoreAlgorithm();

    /**
     * Architectural algorithm abstract classifier.
     *
     * @return the abstract classifier
     */
    AbstractClassifier architecturalAlgorithm();

    /**
     * Sets score arff execution.
     *
     * @param scoreArffExecution the score arff execution
     */
    void setScoreArffExecution(ArffExecution scoreArffExecution);

    /**
     * Sets architectural arff execution.
     *
     * @param architecturalArffExecution the architectural arff execution
     */
    void setArchitecturalArffExecution(ArffExecution architecturalArffExecution);

    /**
     * Gets evaluation model.
     *
     * @return the evaluation model
     */
    EvaluationModels getEvaluationModel();

    /**
     * Gets num folds.
     *
     * @return the num folds
     */
    int getNumFolds();

    /**
     * Gets distribute user evaluation.
     *
     * @return the distribute user evaluation
     */
    DistributeUserEvaluation getDistributeUserEvaluation();
}
