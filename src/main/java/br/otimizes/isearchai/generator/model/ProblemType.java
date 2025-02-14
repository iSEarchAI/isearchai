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
import org.nautilus.core.encoding.solution.NBinarySolution;
import org.nautilus.core.encoding.solution.NDoubleSolution;
import org.nautilus.core.encoding.solution.NIntegerSolution;

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
        public Class getNautilusSolution() {
            return NBinarySolution.class;
        }

        @Override
        public String getVariablesAsListBody() {
            return "BinarySet binarySet = sol.getVariableValue(0);\n" +
                "        for (int i = 0; i < binarySet.getBinarySetLength(); i++) {\n" +
                "            if (binarySet.get(i)) {\n" +
                "                variables.add(\"Solution #\"+i+\": \"+data.getSolution(i));\n" +
                "            }\n" +
                "        }";
        }

        @Override
        public String getTXTInstanceBody() {
            return "protected int sumOfSolution;\n" +
                "\n" +
                "    protected double sumOfElement;\n" +
                "\n" +
                "    protected List<Integer> numberOfElements;\n" +
                "\n" +
                "    protected List<$solution.name> solutions;\n" +
                "\n" +
                "    $element.objectivesAttributes\n" +
                "\n" +
                "    $element.listAttributes\n" +
                "\n" +
                "    public TXTInstance(Path path) {\n" +
                "\n" +
                "        this.solutions = new ArrayList<>();\n" +
                "        $element.objectivesConstructor\n" +
                "\n" +
                "        InstanceReader reader = new InstanceReader(path, \" \");\n" +
                "\n" +
                "        reader.ignoreLine();\n" +
                "        this.sumOfSolution = reader.readIntegerValue();\n" +
                "\n" +
                "        reader.ignoreLine();\n" +
                "        this.numberOfElements = reader.readIntegerValues();\n" +
                "\n" +
                "        for (int i = 0; i < sumOfSolution; i++) {\n" +
                "\n" +
                "            reader.ignoreLine();\n" +
                "\n" +
                "            List<$element.name> elements = new ArrayList<>();\n" +
                "\n" +
                "            for (int j = 0; j < numberOfElements.get(i); j++) {\n" +
                "\n" +
                "                List<Double> values = reader.readDoubleValues();\n" +
                "\n" +
                "                $element.name element = new $element.name(\n" +
                "                    $element.valuesGet\n" +
                "                );\n" +
                "\n" +
                "                elements.add(element);\n" +
                "            }\n" +
                "\n" +
                "            this.solutions.add(new $solution.name(elements));\n" +
                "        }\n" +
                "\n" +
                "        for ($solution.name solution : solutions) {\n" +
                "            $element.solutionFor\n" +
                "        }\n" +
                "\n" +
                "        $element.solutionStream\n" +
                "        this.sumOfElement = this.numberOfElements.stream().mapToDouble(e -> e).sum();\n" +
                "    }\n" +
                "\n" +
                "    public int getSumOfSolution() {\n" +
                "        return sumOfSolution;\n" +
                "    }\n" +
                "\n" +
                "    $element.getters\n" +
                "\n" +
                "    public double getSumOfElement() {\n" +
                "        return this.sumOfElement;\n" +
                "    }\n" +
                "\n" +
                "    $element.geti\n" +
                "\n" +
                "    public List<$element.name> gelementnames(int solutionId) {\n" +
                "        return this.solutions.get(solutionId).elements;\n" +
                "    }\n" +
                "\n" +
                "    public $solution.name getSolution(int index) {\n" +
                "        return this.solutions.get(index);\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public List<Tab> getTabs(Instance data) {\n" +
                "\n" +
                "        TXTInstance c = (TXTInstance) data;\n" +
                "\n" +
                "        List<Tab> tabs = new ArrayList<>();\n" +
                "\n" +
                "        tabs.add(getSolutionsTab(c));\n" +
                "\n" +
                "        return tabs;\n" +
                "    }\n" +
                "\n" +
                "    protected Tab getSolutionsTab(TXTInstance data) {\n" +
                "\n" +
                "        TableTabContent table = new TableTabContent(Arrays.asList($element.objStrList));\n" +
                "\n" +
                "        for (int i = 0; i < data.getSumOfSolution(); i++) {\n" +
                "            table.getRows().add(Arrays.asList(\n" +
                "                $element.dataGet\n" +
                "            ));\n" +
                "        }\n" +
                "\n" +
                "        return new Tab(\"Solutions\", table);\n" +
                "    }";
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
        public Class getNautilusSolution() {
            return NIntegerSolution.class;
        }

        @Override
        public String getVariablesAsListBody() {
            return "for (int i = 0; i < sol.getNumberOfVariables(); i++) {\n" +
                "            variables.add(String.valueOf(sol.getVariableValue(i)));\n" +
                "        }";
        }

        @Override
        public String getTXTInstanceBody() {
            return getTXTInstanceBodyTemplate().replaceAll("\\$templateType","int");
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
        public Class getNautilusSolution() {
            return NDoubleSolution.class;
        }

        @Override
        public String getVariablesAsListBody() {
            return "for (int i = 0; i < sol.getNumberOfVariables(); i++) {\n" +
                "            variables.add(String.valueOf(sol.getVariableValue(i)));\n" +
                "        }";
        }

        @Override
        public String getTXTInstanceBody() {
            return getTXTInstanceBodyTemplate().replaceAll("\\$templateType","double");
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
    };

    public static String getTXTInstanceBodyTemplate() {
        return "protected $templateType lowerBound;\n" +
            "\n" +
            "\tprotected $templateType upperBound;\n" +
            "\n" +
            "\tprotected int numberOfVariables;\n" +
            "\t$element.objectivesAttributes\n" +
            "\n" +
            "\tpublic TXTInstance(Path path) {\n" +
            "\n" +
            "\t\tPreconditions.checkNotNull(path, \"The path should not be null\");\n" +
            "\t\tPreconditions.checkArgument(Files.exists(path), \"The path does not exists\");\n" +
            "\n" +
            "\t\tInstanceReader reader = new InstanceReader(path);\n" +
            "\n" +
            "\t\tthis.lowerBound = reader.getInteger();\n" +
            "\t\tthis.upperBound = reader.getInteger();\n" +
            "\t\tthis.numberOfVariables = reader.getInteger();\n" +
            "\t}\n" +
            "\n" +
            "\tpublic $templateType getLowerBound() {\n" +
            "\t\treturn lowerBound;\n" +
            "\t}\n" +
            "\n" +
            "\tpublic void setLowerBound($templateType lowerBound) {\n" +
            "\t\tthis.lowerBound = lowerBound;\n" +
            "\t}\n" +
            "\n" +
            "\tpublic $templateType getUpperBound() {\n" +
            "\t\treturn upperBound;\n" +
            "\t}\n" +
            "\n" +
            "\tpublic void setUpperBound($templateType upperBound) {\n" +
            "\t\tthis.upperBound = upperBound;\n" +
            "\t}\n" +
            "\n" +
            "\tpublic int getNumberOfVariables() {\n" +
            "\t\treturn numberOfVariables;\n" +
            "\t}\n" +
            "\n" +
            "\n" +
            "\tpublic int getSumOfSolution() {\n" +
            "        return numberOfVariables;\n" +
            "    }" +
            "\n\n" +
            "\t$element.geti" +
            "\n\n" +
            "\t$element.getters" +
            "\n" +
            "\tpublic void setNumberOfVariables(int numberOfVariables) {\n" +
            "\t\tthis.numberOfVariables = numberOfVariables;\n" +
            "\t}\n" +
            "\n" +
            "\t@Override\n" +
            "    public List<Tab> getTabs(Instance data) {\n" +
            "        return Arrays.asList(getContentTab(data));\n" +
            "    }\n" +
            "\n" +
            "    protected Tab getContentTab(Instance data) {\n" +
            "\n" +
            "        TXTInstance d = (TXTInstance) data;\n" +
            "\n" +
            "        TableTabContent table = new TableTabContent(\"Key\", \"Value\");\n" +
            "\n" +
            "        table.addRow(\"Lower Bound\", d.getLowerBound());\n" +
            "        table.addRow(\"Upper Bound\", d.getUpperBound());\n" +
            "        table.addRow(\"Number of Variables\", d.getNumberOfVariables());\n" +
            "\n" +
            "        return new Tab(\"Content\", table);\n" +
            "    }";
    }
}
