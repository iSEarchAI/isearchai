package br.otimizes.isearchai.interactive;

import br.otimizes.isearchai.learning.*;
import com.rits.cloning.Cloner;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class containing DM interactivity procedures
 */
public class InteractWithDM<T extends MLSolutionSet<E, MLElement>, E extends MLSolution<MLElement>> {
    private SubjectiveAnalyzeAlgorithm subjectiveAnalyzeAlgorithm = null;

    Boolean interactive = true;
    int currentInteraction;
    int generation;
    int maxInteractions;
    int firstInteraction;
    int intervalInteraction;
    InteractiveFunction<T> interactiveFunction;

    public InteractWithDM() {
    }

    @SuppressWarnings("unchecked")
    private Class<T> clazz(T resultFront) {
        return (Class<T>) resultFront.getClass();
    }


    public synchronized int interactWithDM(int generation, T solutionSet, int maxInteractions,
                                           int firstInteraction,
                                           int intervalInteraction, InteractiveFunction<T> interactiveFunction,
                                           int currentInteraction, HashSet<E> bestOfUserEvaluation) throws Exception {
        this.setGeneration(generation);
        this.setMaxInteractions(maxInteractions);
        this.setFirstInteraction(firstInteraction);
        this.setIntervalInteraction(intervalInteraction);
        this.setCurrentInteraction(currentInteraction);
        this.setInteractiveFunction(interactiveFunction);
        return this.interactWithDM(generation, solutionSet, maxInteractions, firstInteraction, intervalInteraction,
            interactiveFunction, currentInteraction, bestOfUserEvaluation);
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
        setCurrentInteraction(interactWithDM(solutionSet, bestOfUserEvaluation, generation));
        return getCurrentInteraction();
    }

    public synchronized int interactWithDM(T solutionSet, HashSet<E> bestOfUserEvaluation, int generation) {
        setGeneration(generation);
        if (!getInteractive())
            return getCurrentInteraction();
        for (E solution : solutionSet) {
            solution.setEvaluation(0);
        }
        boolean isOnInteraction = (generation % intervalInteraction == 0 && generation >= firstInteraction)
            || generation == firstInteraction;
        boolean inTrainingDuring = currentInteraction < maxInteractions && isOnInteraction;
        if (inTrainingDuring) {
            Cloner cloner = new Cloner();
            List<E> solutions = cloner.shallowClone(solutionSet.getSolutions());
            T newS = getMlSolutionSet(solutionSet, solutions);
            solutionSet = interactiveFunction.run(newS);
            if (subjectiveAnalyzeAlgorithm == null) {
                subjectiveAnalyzeAlgorithm = new SubjectiveAnalyzeAlgorithm(getMlSolutionSet(solutionSet, solutionSet.getSolutions()),
                    ClassifierAlgorithm.CLUSTERING_MLP);
                subjectiveAnalyzeAlgorithm.run(null, false);
            } else {
                subjectiveAnalyzeAlgorithm.run(getMlSolutionSet(solutionSet, solutionSet.getSolutions()), false);
            }
            bestOfUserEvaluation.addAll(solutionSet.getSolutions().stream().filter(p -> (p.getEvaluation() >= 5
                    && p.getEvaluatedByUser()) || (p.containsArchitecturalEvaluation() && p.getEvaluatedByUser()))
                .collect(Collectors.toList()));
            currentInteraction++;
        }

        boolean inTrainingAPosteriori = currentInteraction < maxInteractions && Math.abs((currentInteraction
            * intervalInteraction) + (intervalInteraction / 2)) == generation && generation > firstInteraction;
        if (inTrainingAPosteriori) {
            subjectiveAnalyzeAlgorithm.run(getMlSolutionSet(solutionSet, solutionSet.getSolutions()), true);
        }

        if (subjectiveAnalyzeAlgorithm != null) {
            subjectiveAnalyzeAlgorithm.setTrained(!subjectiveAnalyzeAlgorithm.isTrained()
                && currentInteraction >= maxInteractions);
            boolean isTrainFinished = subjectiveAnalyzeAlgorithm.isTrained() &&
                currentInteraction >= maxInteractions && isOnInteraction;
            if (isTrainFinished) {
                subjectiveAnalyzeAlgorithm.evaluateSolutionSetScoreAndArchitecturalAlgorithm(getMlSolutionSet(solutionSet, solutionSet.getSolutions()), true);
            }
        }
        return currentInteraction;
    }

    private T getMlSolutionSet(T solutionSet, List<E> solutions) {
        try {
            return getMlSolutionSetIn(solutionSet, 0, solutions);
        } catch (RuntimeException ex) {
            try {
                return getMlSolutionSetIn(solutionSet, 1, solutions);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private T getMlSolutionSetIn(T solutionSet, int x, List<E> solutions) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        T newS = (T) clazz(solutionSet).getConstructors()[x].newInstance(solutions.size());
        newS.setSolutions(solutions);
        return newS;
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

    public int getCurrentInteraction() {
        return currentInteraction;
    }

    public void setCurrentInteraction(int currentInteraction) {
        this.currentInteraction = currentInteraction;
    }

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    public int getMaxInteractions() {
        return maxInteractions;
    }

    public void setMaxInteractions(int maxInteractions) {
        this.maxInteractions = maxInteractions;
    }

    public int getFirstInteraction() {
        return firstInteraction;
    }

    public void setFirstInteraction(int firstInteraction) {
        this.firstInteraction = firstInteraction;
    }

    public int getIntervalInteraction() {
        return intervalInteraction;
    }

    public void setIntervalInteraction(int intervalInteraction) {
        this.intervalInteraction = intervalInteraction;
    }

    public InteractiveFunction<T> getInteractiveFunction() {
        return interactiveFunction;
    }

    public void setInteractiveFunction(InteractiveFunction<T> interactiveFunction) {
        this.interactiveFunction = interactiveFunction;
    }
}
