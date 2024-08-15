package br.otimizes.isearchai.parser;

import java.io.IOException;

public class Adapter {
    public static void main(String[] args) throws IOException {


        // Save the modified file
//            FileWriter writer = new FileWriter("path/to/your/Element.java");
//            writer.write(cu.toString());
//            writer.close();
        MLElementParser mlElementParser = getMlElementParser();
        MLSolutionParser mlSolutionParser = getMlSolutionParser(mlElementParser);
        MLSolutionSetParser mlSolutionSetParser = getMlSolutionSetParser(mlElementParser, mlSolutionParser);
        System.out.println();
    }

    private static MLElementParser getMlElementParser() {
        String project = "/home/wmfsystem/Documents/Doutorado/0_framework/code/OPLA-Tool/modules/architecture-representation";
        String clazz = "src/main/java/br/otimizes/oplatool/architecture/representation/Element.java";
        MLElementParser ps = new MLElementParser().projectClazz(project, clazz)
            .implement()
            .writeFile();
        return ps;
    }

    private static MLSolutionParser getMlSolutionParser(MLElementParser mlElementParser) {
        String project = "/home/wmfsystem/Documents/Doutorado/0_framework/code/OPLA-Tool/modules/opla-core";
        String clazz = "src/main/java/br/otimizes/oplatool/core/jmetal4/core/Solution.java";
        MLSolutionParser ps = new MLSolutionParser().projectClazz(project, clazz)
            .implement(mlElementParser)
            .writeFile();
        return ps;
    }

    private static MLSolutionSetParser getMlSolutionSetParser(MLElementParser mlElementParser, MLSolutionParser mlSolutionParser) {
        String project = "/home/wmfsystem/Documents/Doutorado/0_framework/code/OPLA-Tool/modules/opla-core";
        String clazz = "src/main/java/br/otimizes/oplatool/core/jmetal4/core/SolutionSet.java";
        MLSolutionSetParser ps = new MLSolutionSetParser().projectClazz(project, clazz)
            .extend(mlElementParser, mlSolutionParser)
            .writeFile();
        return ps;
    }
}
