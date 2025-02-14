package br.otimizes.isearchai.adapter;

import java.io.IOException;

/**
 * The type Adapter.
 */
public class Adapter {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws IOException the io exception
     */
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

    /**
     * Instantiates a new Adapter.
     */
    public Adapter() {
    }


    /**
     * Instantiates a new Adapter.
     *
     * @param elementProject         the element project
     * @param elementClazz           the element clazz
     * @param solutionProject        the solution project
     * @param solutionClazz          the solution clazz
     * @param solutionSetProject     the solution set project
     * @param solutionSetClazz       the solution set clazz
     * @param searchAlgorithmProject the search algorithm project
     * @param searchAlgorithmClazz   the search algorithm clazz
     */
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

    /**
     * Implement.
     */
    public void implement() {
        MLElementAdapter mlElementParser = implementMLElement();
        MLSolutionAdapter mlSolutionParser = implementMlSolution(mlElementParser);
        MLSolutionSetAdapter mlSolutionSetParser = implementMlSolutionSet(mlElementParser, mlSolutionParser);
        implementMlSearchAlgorithm(mlElementParser, mlSolutionParser, mlSolutionSetParser);
    }

    /**
     * Implement ml element ml element adapter.
     *
     * @return the ml element adapter
     */
    public MLElementAdapter implementMLElement() {
        return new MLElementAdapter().setSourceClazz(elementProject, elementClazz)
            .withTypeParameter("E")
            .implement()
            .writeFile();
    }

    /**
     * Implement ml solution ml solution adapter.
     *
     * @param mlElementParser the ml element parser
     * @return the ml solution adapter
     */
    public MLSolutionAdapter implementMlSolution(MLElementAdapter mlElementParser) {
        return new MLSolutionAdapter().setSourceClazz(solutionProject, solutionClazz)
            .withTypeParameter("S")
            .implement(mlElementParser)
            .writeFile();
    }

    /**
     * Implement ml solution set ml solution set adapter.
     *
     * @param mlElementParser  the ml element parser
     * @param mlSolutionParser the ml solution parser
     * @return the ml solution set adapter
     */
    public MLSolutionSetAdapter implementMlSolutionSet(MLElementAdapter mlElementParser, MLSolutionAdapter mlSolutionParser) {
        return new MLSolutionSetAdapter().setSourceClazz(solutionSetProject, solutionSetClazz)
            .withTypeParameter("T")
            .extend(mlElementParser, mlSolutionParser)
            .writeFile();
    }

    /**
     * Implement ml search algorithm ml search algorithm adapter.
     *
     * @param mlElementParser     the ml element parser
     * @param mlSolutionParser    the ml solution parser
     * @param mlSolutionSetParser the ml solution set parser
     * @return the ml search algorithm adapter
     */
    public MLSearchAlgorithmAdapter implementMlSearchAlgorithm(MLElementAdapter mlElementParser, MLSolutionAdapter mlSolutionParser,
                                                               MLSolutionSetAdapter mlSolutionSetParser) {
        return new MLSearchAlgorithmAdapter().setSourceClazz(searchAlgorithmProject, searchAlgorithmClazz)
            .implement(mlElementParser, mlSolutionParser, mlSolutionSetParser)
            .replaceComments()
            .addAttributes()
            .writeFile();
    }



}
