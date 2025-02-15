package br.otimizes.isearchai.generator.model;

import br.otimizes.isearchai.learning.algorithms.options.nsgaii.MLNSGAIIRunner;

/**
 * The enum Search algorithm runner.
 */
public enum SearchAlgorithmRunner implements ISearchAlgorithmRunner {
    /**
     * The Nsgaii.
     */
    NSGAII {
        @Override
        public Class runner() {
            return MLNSGAIIRunner.class;
        }
    },
    NSGAIII {
        @Override
        public Class runner() {
            return MLNSGAIIRunner.class;
        }
    }
}
