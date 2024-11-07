package br.otimizes.isearchai.learning.ml.subjective;

import br.otimizes.isearchai.learning.algorithms.options.nsgaii.SubjectiveAnalyzeOptions;
import weka.classifiers.AbstractClassifier;
import weka.core.Instances;

public interface EvaluationModelsAlgorithm {
    double[] build(AbstractClassifier architectureEvaluation, Instances test, SubjectiveAnalyzeOptions options) throws Exception;
}
