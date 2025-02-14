package br.otimizes.isearchai.interactive;

import br.otimizes.isearchai.core.MLElement;
import br.otimizes.isearchai.core.MLSolution;
import br.otimizes.isearchai.core.MLSolutionSet;
import br.otimizes.isearchai.learning.ml.subjective.SubjectiveAnalyzeAlgorithm;

import java.util.HashSet;

/**
 * The type Interactive handler.
 *
 * @param <T> the type parameter
 * @param <E> the type parameter
 */
/*
 * Helper class which holds options related to interactivity.
 * @author Lucas
 */
public class InteractiveHandler<T extends MLSolutionSet<E, MLElement>, E extends MLSolution<MLElement, ?>> {

    /**
     * The type Interaction data.
     */
    public static class InteractionData {
        private int currentInteraction = 0;
        private HashSet<MLSolution<MLElement, ?>> bestOfUserEvaluation = new HashSet<>();

        /**
         * Gets current interaction.
         *
         * @return the current interaction
         */
        public int getCurrentInteraction() {
            return currentInteraction;
        }

        /**
         * Sets current interaction.
         *
         * @param currentInteraction the current interaction
         * @return the current interaction
         */
        public InteractionData setCurrentInteraction(int currentInteraction) {
            this.currentInteraction = currentInteraction;
            return this;
        }

        /**
         * Gets best of user evaluation.
         *
         * @return the best of user evaluation
         */
        public HashSet<MLSolution<MLElement, ?>> getBestOfUserEvaluation() {
            return bestOfUserEvaluation;
        }

        /**
         * Sets best of user evaluation.
         *
         * @param bestOfUserEvaluation the best of user evaluation
         * @return the best of user evaluation
         */
        public InteractionData setBestOfUserEvaluation(HashSet<MLSolution<MLElement, ?>> bestOfUserEvaluation) {
            this.bestOfUserEvaluation = bestOfUserEvaluation;
            return this;
        }
    }

    ;

    private InteractiveConfig config;
    private InteractionData data;
    private InteractWithDM interaction;

    private SubjectiveAnalyzeAlgorithm subjectiveAnalyzeAlgorithm;

    /**
     * Instantiates a new Interactive handler.
     */
    public InteractiveHandler() {
        this.data = new InteractionData();
        this.interaction = new InteractWithDM();
    }

    /**
     * Sets interactive config.
     *
     * @param config the config
     * @return the interactive config
     */
    public InteractiveHandler setInteractiveConfig(InteractiveConfig config) {
        this.config = config;
        return this;
    }

    /**
     * Check and interact boolean.
     *
     * @param generation          the generation
     * @param offspringPopulation the offspring population
     * @return the boolean
     * @throws Exception the exception
     */
    public boolean checkAndInteract(int generation, MLSolutionSet offspringPopulation) throws Exception {
        int currentInteraction = interaction.interactWithDM(
            generation,
            offspringPopulation,
            config.getMaxInteractions(),
            config.getFirstInteraction(),
            config.getIntervalInteraction(),
            config.getInteractiveFunction(),
            data.getCurrentInteraction(),
            data.getBestOfUserEvaluation());
        if (data.getCurrentInteraction() != currentInteraction) {
            data.setCurrentInteraction(currentInteraction);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Subjective analyze solution set.
     *
     * @param population the population
     * @throws Exception the exception
     */
    public void subjectiveAnalyzeSolutionSet(MLSolutionSet population) throws Exception {
        subjectiveAnalyzeAlgorithm = interaction.getSubjectiveAnalyzeAlgorithm();
        if (subjectiveAnalyzeAlgorithm != null && subjectiveAnalyzeAlgorithm.isTrained())
            subjectiveAnalyzeAlgorithm.evaluateSolutionSetScoreAndArchitecturalAlgorithm(population, false);
    }

    /**
     * Reset interaction data.
     */
    public void resetInteractionData() {
        data = new InteractionData();
    }

    /**
     * Gets subjective analyze algorithm.
     *
     * @return the subjective analyze algorithm
     */
    public SubjectiveAnalyzeAlgorithm getSubjectiveAnalyzeAlgorithm() {
        return subjectiveAnalyzeAlgorithm;
    }

    /**
     * Sets subjective analyze algorithm.
     *
     * @param subjectiveAnalyzeAlgorithm the subjective analyze algorithm
     */
    public void setSubjectiveAnalyzeAlgorithm(SubjectiveAnalyzeAlgorithm subjectiveAnalyzeAlgorithm) {
        this.subjectiveAnalyzeAlgorithm = subjectiveAnalyzeAlgorithm;
    }
}
