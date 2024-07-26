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

    public InteractWithDM() {
    }

    @SuppressWarnings("unchecked")
    private Class<T> clazz(T resultFront) {
        return (Class<T>) resultFront.getClass();
    }


    public synchronized int interactWithDM(int generation, T solutionSet, int maxInteractions,
                                           int firstInteraction,
                                           int intervalInteraction, InteractiveFunction<T, E> interactiveFunction,
                                           int currentInteraction, HashSet<E> bestOfUserEvaluation) throws Exception {
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
                subjectiveAnalyzeAlgorithm
                    .evaluateSolutionSetScoreAndArchitecturalAlgorithm(getMlSolutionSet(solutionSet, solutionSet.getSolutions()), true);
            }
        }
        return currentInteraction;
    }

    private T getMlSolutionSet(T solutionSet, List<E> solutions) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        T newS = (T) clazz(solutionSet).getConstructors()[0].newInstance(solutions.size());
        newS.setSolutions(solutions);
        return newS;
    }

    public SubjectiveAnalyzeAlgorithm getSubjectiveAnalyzeAlgorithm() {
        return subjectiveAnalyzeAlgorithm;
    }

    public void setSubjectiveAnalyzeAlgorithm(SubjectiveAnalyzeAlgorithm subjectiveAnalyzeAlgorithm) {
        this.subjectiveAnalyzeAlgorithm = subjectiveAnalyzeAlgorithm;
    }
}
