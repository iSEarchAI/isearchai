package br.otimizes.isearchai.interactive;

import br.otimizes.isearchai.core.MLElement;
import br.otimizes.isearchai.core.MLSolution;
import br.otimizes.isearchai.core.MLSolutionSet;
import br.otimizes.isearchai.learning.ml.subjective.SubjectiveAnalyzeAlgorithm;

import java.util.HashSet;

/*
 * Helper class which holds options related to interactivity.
 * @author Lucas
 */
public class InteractiveHandler<T extends MLSolutionSet<E, MLElement>, E extends MLSolution<MLElement>> {

    public static class InteractionData {
        private int currentInteraction = 0;
        private HashSet<MLSolution<MLElement>> bestOfUserEvaluation = new HashSet<>();

        public int getCurrentInteraction() {
            return currentInteraction;
        }

        public InteractionData setCurrentInteraction(int currentInteraction) {
            this.currentInteraction = currentInteraction;
            return this;
        }

        public HashSet<MLSolution<MLElement>> getBestOfUserEvaluation() {
            return bestOfUserEvaluation;
        }

        public InteractionData setBestOfUserEvaluation(HashSet<MLSolution<MLElement>> bestOfUserEvaluation) {
            this.bestOfUserEvaluation = bestOfUserEvaluation;
            return this;
        }
    }

    ;

    private InteractiveConfig config;
    private InteractionData data;
    private InteractWithDM interaction;

    private SubjectiveAnalyzeAlgorithm subjectiveAnalyzeAlgorithm;

    public InteractiveHandler() {
        this.data = new InteractionData();
        this.interaction = new InteractWithDM();
    }

    public InteractiveHandler setInteractiveConfig(InteractiveConfig config) {
        this.config = config;
        return this;
    }

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

    public void subjectiveAnalyzeSolutionSet(MLSolutionSet population) throws Exception {
        subjectiveAnalyzeAlgorithm = interaction.getSubjectiveAnalyzeAlgorithm();
        if (subjectiveAnalyzeAlgorithm != null && subjectiveAnalyzeAlgorithm.isTrained())
            subjectiveAnalyzeAlgorithm.evaluateSolutionSetScoreAndArchitecturalAlgorithm(population, false);
    }

    public void resetInteractionData() {
        data = new InteractionData();
    }

    public SubjectiveAnalyzeAlgorithm getSubjectiveAnalyzeAlgorithm() {
        return subjectiveAnalyzeAlgorithm;
    }

    public void setSubjectiveAnalyzeAlgorithm(SubjectiveAnalyzeAlgorithm subjectiveAnalyzeAlgorithm) {
        this.subjectiveAnalyzeAlgorithm = subjectiveAnalyzeAlgorithm;
    }
}
