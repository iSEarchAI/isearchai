package org.nautilus.plugin.nrp.encoding.problem;

import java.util.ArrayList;
import java.util.List;

import org.nautilus.core.encoding.problem.NBinaryProblem;
import org.nautilus.core.model.Instance;
import org.nautilus.core.objective.AbstractObjective;
import org.nautilus.plugin.nrp.encoding.instance.TXTInstance;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.util.binarySet.BinarySet;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

public class SearchProblem extends NBinaryProblem {

    private static final long serialVersionUID = 1233594822179588853L;

    public SearchProblem(Instance instance, List<AbstractObjective> objectives) {
        super(instance, objectives);

        setNumberOfVariables(1);

        List<Integer> bitsPerVariable = new ArrayList<>(getNumberOfVariables());

        for (int i = 0; i < getNumberOfVariables(); i++) {
            bitsPerVariable.add(((TXTInstance) getInstance()).getSumOfSolution());
        }

        setBitsPerVariable(bitsPerVariable);
    }

    @Override
    public void evaluate(BinarySolution solution) {

        // Change if it is invalid

        BinarySet binarySet = (BinarySet) solution.getVariableValue(0);

        if (binarySet.isEmpty()) {

            int pos = JMetalRandom.getInstance().nextInt(0, binarySet.getBinarySetLength() - 1);

            binarySet.set(pos, true);
        }

        super.evaluate(solution);
    }
}
