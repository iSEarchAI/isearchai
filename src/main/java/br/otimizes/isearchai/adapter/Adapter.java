package br.otimizes.isearchai.adapter;

import java.io.IOException;

public class Adapter {
    public static void main(String[] args) throws IOException {


        // Save the modified file
//            FileWriter writer = new FileWriter("path/to/your/Element.java");
//            writer.write(cu.toString());
//            writer.close();
        MLElementAdapter mlElementParser = getMlElementParser();
        MLSolutionAdapter mlSolutionParser = getMlSolutionParser(mlElementParser);
        MLSolutionSetAdapter mlSolutionSetParser = getMlSolutionSetParser(mlElementParser, mlSolutionParser);
        getMlSearchAlgorithmParser(mlElementParser, mlSolutionParser, mlSolutionSetParser);
        System.out.println();
    }

    private static MLElementAdapter getMlElementParser() {
        String project = "/home/wmfsystem/Documents/Doutorado/0_framework/code/OPLA-Tool/modules/architecture-representation";
        String clazz = "src/main/java/br/otimizes/oplatool/architecture/representation/Element.java";
        MLElementAdapter ps = new MLElementAdapter().projectClazz(project, clazz)
            .withTypeParameter("E")
            .implement()
            .writeFile();
        return ps;
    }

    private static MLSolutionAdapter getMlSolutionParser(MLElementAdapter mlElementParser) {
        String project = "/home/wmfsystem/Documents/Doutorado/0_framework/code/OPLA-Tool/modules/opla-core";
        String clazz = "src/main/java/br/otimizes/oplatool/core/jmetal4/core/Solution.java";
        MLSolutionAdapter ps = new MLSolutionAdapter().projectClazz(project, clazz)
            .withTypeParameter("S")
            .implement(mlElementParser)
            .writeFile();
        return ps;
    }

    private static MLSolutionSetAdapter getMlSolutionSetParser(MLElementAdapter mlElementParser, MLSolutionAdapter mlSolutionParser) {
        String project = "/home/wmfsystem/Documents/Doutorado/0_framework/code/OPLA-Tool/modules/opla-core";
        String clazz = "src/main/java/br/otimizes/oplatool/core/jmetal4/core/SolutionSet.java";
        MLSolutionSetAdapter ps = new MLSolutionSetAdapter().projectClazz(project, clazz)
            .withTypeParameter("T")
            .extend(mlElementParser, mlSolutionParser)
            .writeFile();
        return ps;
    }
    private static MLSearchAlgorithmAdapter getMlSearchAlgorithmParser(MLElementAdapter mlElementParser, MLSolutionAdapter mlSolutionParser,
                                                                       MLSolutionSetAdapter mlSolutionSetParser) {
        String project = "/home/wmfsystem/Documents/Doutorado/0_framework/code/OPLA-Tool/modules/opla-core";
        String clazz = "src/main/java/br/otimizes/oplatool/core/jmetal4/metaheuristics/nsgaII/NSGAII.java";
        MLSearchAlgorithmAdapter ps = new MLSearchAlgorithmAdapter().projectClazz(project, clazz)
            .implement(mlElementParser, mlSolutionParser, mlSolutionSetParser)
            .replaceComments()
            .addAttributes()
            .writeFile();
        return ps;
    }
}
