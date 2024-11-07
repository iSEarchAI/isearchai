package br.otimizes.isearchai.learning.algorithms.options.nsgaii;

import br.otimizes.isearchai.learning.ml.basis.ArffExecution;
import br.otimizes.isearchai.learning.ml.clustering.DistributeUserEvaluation;
import br.otimizes.isearchai.learning.ml.subjective.EvaluationModels;
import weka.classifiers.AbstractClassifier;

public interface SubjectiveAnalyzeOptions {
    AbstractClassifier scoreAlgorithm();
    AbstractClassifier architecturalAlgorithm();
    void setScoreArffExecution(ArffExecution scoreArffExecution);
    void setArchitecturalArffExecution(ArffExecution architecturalArffExecution);
    EvaluationModels getEvaluationModel();

    int getNumFolds();
    DistributeUserEvaluation getDistributeUserEvaluation();
}
