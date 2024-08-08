package org.nautilus.plugin.nrp.extension.problem;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.nautilus.core.encoding.solution.NBinarySolution;
import org.nautilus.core.model.Instance;
import org.nautilus.core.objective.AbstractObjective;
import org.nautilus.plugin.extension.problem.AbstractProblemExtension;
import org.nautilus.plugin.nrp.encoding.instance.TXTInstance;
import org.nautilus.plugin.nrp.encoding.SearchProblem.SearchProblem;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.binarySet.BinarySet;
import org.nautilus.plugin.nrp.encoding.objective.Cost;
import org.nautilus.plugin.nrp.encoding.objective.Importance;
import org.nautilus.plugin.nrp.encoding.objective.Profit;
import org.nautilus.plugin.nrp.encoding.objective.Size;


public class ProblemExtension extends AbstractProblemExtension {

    @Override
    public org.uma.jmetal.problem.Problem<?> getProblem(Instance data, List<AbstractObjective> objectives) {
        return new SearchProblem(data, objectives);
    }

    @Override
    public String getName() {
        return "NRP Problem";
    }

    @Override
    public Class<? extends Solution<?>> supports() {
        return BinarySolution.class;
    }

    @Override
    public List<AbstractObjective> getObjectives() {

        List<AbstractObjective> objectives = new ArrayList<>();

        objectives.add(new Cost());
		objectives.add(new Importance());
		objectives.add(new Profit());
		objectives.add(new Size());
		

        return objectives;
    }

    @Override
    public Instance getInstance(Path path) {
        return new TXTInstance(path);
    }

    @Override
    public List<String> getVariablesAsList(Instance instance, Solution<?> solution) {

        TXTInstance data = (TXTInstance) instance;

        NBinarySolution sol = (NBinarySolution) solution;

        BinarySet binarySet = sol.getVariableValue(0);

        List<String> variables = new ArrayList<>();

        for (int i = 0; i < binarySet.getBinarySetLength(); i++) {

            if (binarySet.get(i)) {
                variables.add("Solution #"+i+": "+data.getSolution(i));
            }
        }

        return variables;
    }
}
