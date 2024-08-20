package br.otimizes.isearchai.adapter;

import java.io.IOException;

public class Adapter {
    public static void main(String[] args) throws IOException {
        System.out.println();
        Adapter adapter = new Adapter("/home/wmfsystem/Documents/Doutorado/0_framework/code/OPLA-Tool/modules/architecture-representation",
            "src/main/java/br/otimizes/oplatool/architecture/representation/Element.java",
            "/home/wmfsystem/Documents/Doutorado/0_framework/code/OPLA-Tool/modules/opla-core",
            "src/main/java/br/otimizes/oplatool/core/jmetal4/core/Solution.java",
            "/home/wmfsystem/Documents/Doutorado/0_framework/code/OPLA-Tool/modules/opla-core",
            "src/main/java/br/otimizes/oplatool/core/jmetal4/core/SolutionSet.java",
            "/home/wmfsystem/Documents/Doutorado/0_framework/code/OPLA-Tool/modules/opla-core",
            "src/main/java/br/otimizes/oplatool/core/jmetal4/metaheuristics/nsgaII/NSGAII.java");
        adapter.implement();
    }

    private String elementProject;
    private String elementClazz;
    private String solutionProject;
    private String solutionClazz;
    private String solutionSetProject;
    private String solutionSetClazz;
    private String searchAlgorithmProject;
    private String searchAlgorithmClazz;

    public Adapter() {
    }


    public Adapter(String elementProject, String elementClazz,
                   String solutionProject, String solutionClazz,
                   String solutionSetProject, String solutionSetClazz,
                   String searchAlgorithmProject, String searchAlgorithmClazz) {
        this.elementProject = elementProject;
        this.elementClazz = elementClazz;
        this.solutionProject = solutionProject;
        this.solutionClazz = solutionClazz;
        this.solutionSetProject = solutionSetProject;
        this.solutionSetClazz = solutionSetClazz;
        this.searchAlgorithmProject = searchAlgorithmProject;
        this.searchAlgorithmClazz = searchAlgorithmClazz;
    }

    public void implement() {
        MLElementAdapter mlElementParser = implementMLElement();
        MLSolutionAdapter mlSolutionParser = implementMlSolution(mlElementParser);
        MLSolutionSetAdapter mlSolutionSetParser = implementMlSolutionSet(mlElementParser, mlSolutionParser);
        implementMlSearchAlgorithm(mlElementParser, mlSolutionParser, mlSolutionSetParser);
    }

    public MLElementAdapter implementMLElement() {
        return new MLElementAdapter().setSourceClazz(elementProject, elementClazz)
            .withTypeParameter("E")
            .implement()
            .writeFile();
    }

    public MLSolutionAdapter implementMlSolution(MLElementAdapter mlElementParser) {
        return new MLSolutionAdapter().setSourceClazz(elementProject, solutionClazz)
            .withTypeParameter("S")
            .implement(mlElementParser)
            .writeFile();
    }

    public MLSolutionSetAdapter implementMlSolutionSet(MLElementAdapter mlElementParser, MLSolutionAdapter mlSolutionParser) {
        return new MLSolutionSetAdapter().setSourceClazz(elementProject, solutionSetClazz)
            .withTypeParameter("T")
            .extend(mlElementParser, mlSolutionParser)
            .writeFile();
    }

    public MLSearchAlgorithmAdapter implementMlSearchAlgorithm(MLElementAdapter mlElementParser, MLSolutionAdapter mlSolutionParser,
                                                               MLSolutionSetAdapter mlSolutionSetParser) {
        return new MLSearchAlgorithmAdapter().setSourceClazz(elementProject, searchAlgorithmClazz)
            .implement(mlElementParser, mlSolutionParser, mlSolutionSetParser)
            .replaceComments()
            .addAttributes()
            .writeFile();
    }
}
