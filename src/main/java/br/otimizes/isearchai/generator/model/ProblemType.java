package br.otimizes.isearchai.generator.model;

import br.otimizes.isearchai.learning.algorithms.binary.MLNSGAIIBinaryRunner;
import br.otimizes.isearchai.learning.algorithms.doubl.MLNSGAIIDoubleRunner;
import br.otimizes.isearchai.learning.algorithms.integer.MLNSGAIIIntegerRunner;
import br.otimizes.isearchai.learning.encoding.binary.MLBinaryProblem;
import br.otimizes.isearchai.learning.encoding.binary.MLBinarySolution;
import br.otimizes.isearchai.learning.encoding.binary.MLBinarySolutionSet;
import br.otimizes.isearchai.learning.encoding.doubl.MLDoubleProblem;
import br.otimizes.isearchai.learning.encoding.doubl.MLDoubleSolution;
import br.otimizes.isearchai.learning.encoding.doubl.MLDoubleSolutionSet;
import br.otimizes.isearchai.learning.encoding.integer.MLIntegerProblem;
import br.otimizes.isearchai.learning.encoding.integer.MLIntegerSolution;
import br.otimizes.isearchai.learning.encoding.integer.MLIntegerSolutionSet;

import java.util.Arrays;
import java.util.List;

public enum ProblemType implements IProblemType {
    BINARY {
        @Override
        public String getPackage() {
            return "br.otimizes.isearchai.learning.encoding.binary";
        }

        @Override
        public Class getSolution() {
            return MLBinarySolution.class;
        }

        @Override
        public Class getSolutionSet() {
            return MLBinarySolutionSet.class;
        }

        @Override
        public Class getProblem() {
            return MLBinaryProblem.class;
        }

        @Override
        public String getBody() {
            return "  public SearchProblem(Instance instance, List<AbstractObjective> objectives) {\n" +
                "        super(instance, objectives);\n" +
                "\n" +
                "        setNumberOfVariables(1);\n" +
                "\n" +
                "        List<Integer> bitsPerVariable = new ArrayList<>(getNumberOfVariables());\n" +
                "\n" +
                "        for (int i = 0; i < getNumberOfVariables(); i++) {\n" +
                "            bitsPerVariable.add(((TXTInstance) getInstance()).getSumOfSolution());\n" +
                "        }\n" +
                "\n" +
                "        setBitsPerVariable(bitsPerVariable);\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public void evaluate(MLBinarySolution solution) {\n" +
                "\n" +
                "        // Change if it is invalid\n" +
                "\n" +
                "        BinarySet binarySet = (BinarySet) solution.getVariableValue(0);\n" +
                "\n" +
                "        if (binarySet.isEmpty()) {\n" +
                "\n" +
                "            int pos = JMetalRandom.getInstance().nextInt(0, binarySet.getBinarySetLength() - 1);\n" +
                "\n" +
                "            binarySet.set(pos, true);\n" +
                "        }\n" +
                "\n" +
                "        super.evaluate(solution);\n" +
                "    }";
        }

        @Override
        public List<Class> getRunners() {
            return Arrays.asList(MLNSGAIIBinaryRunner.class);
        }
    }, INTEGER {
        @Override
        public String getPackage() {
            return "br.otimizes.isearchai.learning.encoding.integer";
        }

        @Override
        public Class getSolution() {
            return MLIntegerSolution.class;
        }

        @Override
        public Class getSolutionSet() {
            return MLIntegerSolutionSet.class;
        }

        @Override
        public Class getProblem() {
            return MLIntegerProblem.class;
        }

        @Override
        public String getBody() {
            return "public SearchProblem(Instance instance, List<AbstractObjective> objectives) {\n" +
                "        super(instance, objectives);\n" +
                "\n" +
                "        TXTInstance d = (TXTInstance) instance;\n" +
                "\n" +
                "        setNumberOfVariables(d.getNumberOfVariables());\n" +
                "\n" +
                "        setLowerBounds(Collections.nCopies(getNumberOfVariables(), d.getLowerBound()));\n" +
                "        setUpperBounds(Collections.nCopies(getNumberOfVariables(), d.getUpperBound()));\n" +
                "    }";
        }

        @Override
        public List<Class> getRunners() {
            return Arrays.asList(MLNSGAIIIntegerRunner.class);
        }
    }, DOUBLE {
        @Override
        public String getPackage() {
            return "br.otimizes.isearchai.learning.encoding.doubl";
        }

        @Override
        public Class getSolution() {
            return MLDoubleSolution.class;
        }

        @Override
        public Class getSolutionSet() {
            return MLDoubleSolutionSet.class;
        }

        @Override
        public Class getProblem() {
            return MLDoubleProblem.class;
        }

        @Override
        public String getBody() {
            return "public SearchProblem(Instance instance, List<AbstractObjective> objectives) {\n" +
                "        super(instance, objectives);\n" +
                "\n" +
                "        TXTInstance d = (TXTInstance) instance;\n" +
                "\n" +
                "        setNumberOfVariables(d.getNumberOfVariables());\n" +
                "\n" +
                "        setLowerBounds(Collections.nCopies(getNumberOfVariables(), d.getLowerBound()));\n" +
                "        setUpperBounds(Collections.nCopies(getNumberOfVariables(), d.getUpperBound()));\n" +
                "    }";
        }

        @Override
        public List<Class> getRunners() {
            return Arrays.asList(MLNSGAIIDoubleRunner.class);
        }
    }
}
