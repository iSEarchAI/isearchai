package br.otimizes.isearchai.learning.ml.subjective;

import br.otimizes.isearchai.learning.algorithms.options.nsgaii.SubjectiveAnalyzeOptions;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

import java.util.Random;

/**
 * Evaluation models
 */
public enum EvaluationModels implements EvaluationModelsAlgorithm {
    TRAINING_SET{
        @Override
        public double[] build(AbstractClassifier algorithm, Instances test, SubjectiveAnalyzeOptions options) throws Exception {
            Evaluation evaluation = new Evaluation(test);
            return evaluation.evaluateModel(algorithm, test);
        }
    }, SUPPLIED_TEST{
        @Override
        public double[] build(AbstractClassifier algorithm, Instances test, SubjectiveAnalyzeOptions options) throws Exception {
            Evaluation evaluation = new Evaluation(test);
            return null;
        }
    }, CROSS_VALIDATION{
        @Override
        public double[] build(AbstractClassifier algorithm, Instances test, SubjectiveAnalyzeOptions options) throws Exception {
            Evaluation evaluation = new Evaluation(test);
            evaluation.crossValidateModel(algorithm, test, options.getNumFolds(), new Random(1));
            return null;
        }
    }, PERCENTAGE_SPLIT{
        @Override
        public double[] build(AbstractClassifier algorithm, Instances test, SubjectiveAnalyzeOptions options) throws Exception {
            Evaluation evaluation = new Evaluation(test);

            return null;
        }
    }
}
