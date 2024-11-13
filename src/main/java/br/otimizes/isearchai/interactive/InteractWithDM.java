package br.otimizes.isearchai.interactive;

import br.otimizes.isearchai.core.MLElement;
import br.otimizes.isearchai.core.MLSolution;
import br.otimizes.isearchai.core.MLSolutionSet;
import br.otimizes.isearchai.learning.ml.subjective.SubjectiveAnalyzeAlgorithm;
import com.rits.cloning.Cloner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class containing DM interactivity procedures
 */
public class InteractWithDM<T extends MLSolutionSet<E, MLElement>, E extends MLSolution<MLElement, ?>> {
    private SubjectiveAnalyzeAlgorithm subjectiveAnalyzeAlgorithm = null;

    Boolean interactive = true;
    int generation;
    private InteractiveConfig config;

    public InteractWithDM() {
    }

    public InteractWithDM(InteractiveConfig interactiveConfig) {
        this.setInteractive(true);
        this.config = interactiveConfig;
    }

    @SuppressWarnings("unchecked")
    private Class<T> clazz(T resultFront) {
        return (Class<T>) resultFront.getClass();
    }


    public synchronized int interactWithDM(int generation, T solutionSet, int maxInteractions,
                                           int firstInteraction,
                                           int intervalInteraction, InteractiveFunction<T> interactiveFunction,
                                           int currentInteraction, HashSet<E> bestOfUserEvaluation) throws Exception {
        this.config = new InteractiveConfig();
        this.setGeneration(generation);
        this.config.setMaxInteractions(maxInteractions);
        this.config.setFirstInteraction(firstInteraction);
        this.config.setIntervalInteraction(intervalInteraction);
        this.config.setCurrentInteraction(currentInteraction);
        this.config.setInteractiveFunction(interactiveFunction);
        return this.interactWithDM(generation, solutionSet, maxInteractions, firstInteraction, intervalInteraction,
            interactiveFunction, currentInteraction, bestOfUserEvaluation);
    }

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    public void subjectiveAnalyzeAlgorithmEvaluate(T solutionSet) {
        SubjectiveAnalyzeAlgorithm subjectiveAnalyzeAlgorithm = getSubjectiveAnalyzeAlgorithm();
        if (getInteractive() && subjectiveAnalyzeAlgorithm != null && subjectiveAnalyzeAlgorithm.isTrained()) {
            try {
                subjectiveAnalyzeAlgorithm.evaluateSolutionSetScoreAndArchitecturalAlgorithm(solutionSet, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized int interactWithDMUpdatingInteraction(T solutionSet, HashSet<E> bestOfUserEvaluation, int generation) {
        this.config.setCurrentInteraction(interactWithDM(solutionSet, bestOfUserEvaluation, generation));
        return this.config.getCurrentInteraction();
    }

    public synchronized int interactWithDM(T solutionSet, HashSet<E> bestOfUserEvaluation, int generation) {
        setGeneration(generation);
        if (!getInteractive())
            return this.config.getCurrentInteraction();
        for (E solution : solutionSet) {
            solution.setEvaluation(0);
        }
        boolean isOnInteraction = (generation % this.config.getIntervalInteraction() == 0 && generation >= this.config.getFirstInteraction())
            || generation == this.config.getFirstInteraction();
        boolean inTrainingDuring = this.config.getCurrentInteraction() < this.config.getMaxInteractions() && isOnInteraction;
        if (inTrainingDuring) {
            Cloner cloner = new Cloner();
            List<E> solutions = cloner.shallowClone(solutionSet.getSolutions());
            T newS = getMlSolutionSet(solutionSet, solutions);
            solutionSet = (T) this.config.getInteractiveFunction().run(newS);
            if (subjectiveAnalyzeAlgorithm == null) {
                subjectiveAnalyzeAlgorithm = new SubjectiveAnalyzeAlgorithm(getMlSolutionSet(solutionSet, solutionSet.getSolutions()),
                    this.config.getOptions());
                subjectiveAnalyzeAlgorithm.run(null, false);
            } else {
                subjectiveAnalyzeAlgorithm.run(getMlSolutionSet(solutionSet, solutionSet.getSolutions()), false);
            }
            bestOfUserEvaluation.addAll(solutionSet.getSolutions().stream().filter(p -> (p.getEvaluation() >= 5
                    && p.getEvaluatedByUser()) || (p.containsElementsEvaluation() && p.getEvaluatedByUser()))
                .collect(Collectors.toList()));
            this.config.setCurrentInteraction(this.config.getCurrentInteraction() + 1);
        }

        boolean inTrainingAPosteriori = this.config.getCurrentInteraction() < this.config.getMaxInteractions() && Math.abs((this.config.getCurrentInteraction()
            * this.config.getIntervalInteraction()) + (this.config.getIntervalInteraction() / 2)) == generation && generation > this.config.getFirstInteraction();
        if (inTrainingAPosteriori) {
            subjectiveAnalyzeAlgorithm.run(getMlSolutionSet(solutionSet, solutionSet.getSolutions()), true);
        }

        if (subjectiveAnalyzeAlgorithm != null) {
            subjectiveAnalyzeAlgorithm.setTrained(!subjectiveAnalyzeAlgorithm.isTrained()
                && this.config.getCurrentInteraction() >= this.config.getMaxInteractions());
            boolean isTrainFinished = subjectiveAnalyzeAlgorithm.isTrained() &&
                this.config.getCurrentInteraction() >= this.config.getMaxInteractions() && isOnInteraction;
            if (isTrainFinished) {
                subjectiveAnalyzeAlgorithm.evaluateSolutionSetScoreAndArchitecturalAlgorithm(getMlSolutionSet(solutionSet, solutionSet.getSolutions()), true);
            }
        }
        return this.config.getCurrentInteraction();
    }

    private T getMlSolutionSet(T solutionSet, List<E> solutions) {
        Constructor<?> founded = Arrays.stream(clazz(solutionSet)
            .getConstructors()).filter(constructor -> constructor.getParameters().length == 0).findFirst().orElse(null);
        try {
            T newS = (T) founded.newInstance();
            newS.setSolutions(solutions);
            return newS;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public SubjectiveAnalyzeAlgorithm getSubjectiveAnalyzeAlgorithm() {
        return subjectiveAnalyzeAlgorithm;
    }

    public void setSubjectiveAnalyzeAlgorithm(SubjectiveAnalyzeAlgorithm subjectiveAnalyzeAlgorithm) {
        this.subjectiveAnalyzeAlgorithm = subjectiveAnalyzeAlgorithm;
    }


    public Boolean getInteractive() {
        return interactive != null && interactive;
    }

    public void setInteractive(Boolean interactive) {
        this.interactive = interactive;
    }
}
