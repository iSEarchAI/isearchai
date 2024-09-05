package br.otimizes.isearchai.generator.model;

import br.otimizes.isearchai.learning.algorithms.options.nsgaii.MLNSGAIIRunner;

public enum SearchAlgorithmRunner implements ISearchAlgorithmRunner {
    NSGAII {
        @Override
        public Class runner() {
            return MLNSGAIIRunner.class;
        }
    }
}
