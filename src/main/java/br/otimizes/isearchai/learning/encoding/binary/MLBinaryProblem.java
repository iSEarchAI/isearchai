package br.otimizes.isearchai.learning.encoding.binary;

import org.nautilus.core.encoding.NProblem;
import org.nautilus.core.model.Instance;
import org.nautilus.core.objective.AbstractObjective;
import org.uma.jmetal.util.binarySet.BinarySet;

import java.util.List;

public class MLBinaryProblem extends NProblem<MLBinarySolution> {

    private static final long serialVersionUID = 8418817221014037519L;

    protected List<Integer> bitsPerVariable;

    public MLBinaryProblem(Instance instance, List<AbstractObjective> objectives) {
        super(instance, objectives);
    }


    public int getNumberOfBits(int index) {
        return bitsPerVariable.get(index);
    }

    public int getTotalNumberOfBits() {

        int count = 0;

        for (int i = 0; i < this.getNumberOfVariables(); i++) {
            count += this.getNumberOfBits(i);
        }

        return count;
    }

    public List<Integer> getBitsPerVariable() {
        return bitsPerVariable;
    }

    public void setBitsPerVariable(List<Integer> bitsPerVariable) {
        this.bitsPerVariable = bitsPerVariable;
    }

    @Override
    public void evaluate(MLBinarySolution solution) {

//		long startTime = System.currentTimeMillis();

        BinarySet binarySet = solution.getVariableValue(0);

        for (int i = 0; i < objectives.size(); i++) {
            objectives.get(i).beforeProcess(instance, solution);
        }

        for (int i = 0; i < binarySet.getBinarySetLength(); i++) {

            if (binarySet.get(i)) {

                for (int j = 0; j < objectives.size(); j++) {
                    objectives.get(j).process(instance, solution, i);
                }
            }
        }

        for (int i = 0; i < objectives.size(); i++) {
            objectives.get(i).afterProcess(instance, solution);
        }

        for (int i = 0; i < objectives.size(); i++) {
            solution.setObjective(i, objectives.get(i).evaluate(instance, solution));
        }

//		long endTime = System.currentTimeMillis() - startTime;
//
//		System.out.println(endTime);
    }

    @Override
    public MLBinarySolution createSolution() {
        return new MLBinarySolution(
            getNumberOfObjectives(),
            getNumberOfVariables(),
            getBitsPerVariable()
        );
    }
}
