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
 *
 * @param <T> the type parameter
 * @param <E> the type parameter
 */
public class InteractWithDM<T extends MLSolutionSet<E, MLElement>, E extends MLSolution<MLElement, ?>> {
    private SubjectiveAnalyzeAlgorithm subjectiveAnalyzeAlgorithm = null;

    /**
     * The Interactive.
     */
    Boolean interactive = true;
    /**
     * The Generation.
     */
    int generation;
    private InteractiveConfig config;

    /**
     * Instantiates a new Interact with dm.
     */
    public InteractWithDM() {
    }

    /**
     * Instantiates a new Interact with dm.
     *
     * @param interactiveConfig the interactive config
     */
    public InteractWithDM(InteractiveConfig interactiveConfig) {
        this.setInteractive(true);
        this.config = interactiveConfig;
    }

    @SuppressWarnings("unchecked")
    private Class<T> clazz(T resultFront) {
        return (Class<T>) resultFront.getClass();
    }


    /**
     * Interact with dm int.
     *
     * @param generation           the generation
     * @param solutionSet          the solution set
     * @param maxInteractions      the max interactions
     * @param firstInteraction     the first interaction
     * @param intervalInteraction  the interval interaction
     * @param interactiveFunction  the interactive function
     * @param currentInteraction   the current interaction
     * @param bestOfUserEvaluation the best of user evaluation
     * @return the int
     * @throws Exception the exception
     */
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

    /**
     * Gets generation.
     *
     * @return the generation
     */
    public int getGeneration() {
        return generation;
    }

    /**
     * Sets generation.
     *
     * @param generation the generation
     */
    public void setGeneration(int generation) {
        this.generation = generation;
    }

    /**
     * Subjective analyze algorithm evaluate.
     *
     * @param solutionSet the solution set
     */
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

    /**
     * Interact with dm updating interaction int.
     *
     * @param solutionSet          the solution set
     * @param bestOfUserEvaluation the best of user evaluation
     * @param generation           the generation
     * @return the int
     */
    public synchronized int interactWithDMUpdatingInteraction(T solutionSet, HashSet<E> bestOfUserEvaluation, int generation) {
        this.config.setCurrentInteraction(interactWithDM(solutionSet, bestOfUserEvaluation, generation));
        return this.config.getCurrentInteraction();
    }

    /**
     * Interact with dm int.
     *
     * @param solutionSet          the solution set
     * @param bestOfUserEvaluation the best of user evaluation
     * @param generation           the generation
     * @return the int
     */
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


    /**
     * Gets interactive.
     *
     * @return the interactive
     */
    public Boolean getInteractive() {
        return interactive != null && interactive;
    }

    /**
     * Sets interactive.
     *
     * @param interactive the interactive
     */
    public void setInteractive(Boolean interactive) {
        this.interactive = interactive;
    }
}
