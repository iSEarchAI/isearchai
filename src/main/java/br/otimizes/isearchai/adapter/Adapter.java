package br.otimizes.isearchai.adapter;

import java.io.IOException;

public class Adapter {
    public static void main(String[] args) throws IOException {
        System.out.println();
        Adapter adapter = new Adapter("/home/wmfsystem/Documents/Doutorado/0_framework/code/OPLA-Tool/modules/architecture-representation",
            "src/main/java/br/otimizes/oplatool/architecture/representation/Element.java");
        adapter.implement();
    }

    private String project;
    private String clazz;

    public Adapter(String project, String clazz) {
        this.project = project;
        this.clazz = clazz;
    }

    public void implement() {
        MLElementAdapter mlElementParser = implementMLElement();
        MLSolutionAdapter mlSolutionParser = implementMlSolution(mlElementParser);
        MLSolutionSetAdapter mlSolutionSetParser = implementMlSolutionSet(mlElementParser, mlSolutionParser);
        implementMlSearchAlgorithm(mlElementParser, mlSolutionParser, mlSolutionSetParser);
    }

    public MLElementAdapter implementMLElement() {
        return new MLElementAdapter().setSourceClazz(project, clazz)
            .withTypeParameter("E")
            .implement()
            .writeFile();
    }

    public MLSolutionAdapter implementMlSolution(MLElementAdapter mlElementParser) {
        return new MLSolutionAdapter().setSourceClazz(project, clazz)
            .withTypeParameter("S")
            .implement(mlElementParser)
            .writeFile();
    }

    public MLSolutionSetAdapter implementMlSolutionSet(MLElementAdapter mlElementParser, MLSolutionAdapter mlSolutionParser) {
        return new MLSolutionSetAdapter().setSourceClazz(project, clazz)
            .withTypeParameter("T")
            .extend(mlElementParser, mlSolutionParser)
            .writeFile();
    }

    public MLSearchAlgorithmAdapter implementMlSearchAlgorithm(MLElementAdapter mlElementParser, MLSolutionAdapter mlSolutionParser,
                                                               MLSolutionSetAdapter mlSolutionSetParser) {
        return new MLSearchAlgorithmAdapter().setSourceClazz(project, clazz)
            .implement(mlElementParser, mlSolutionParser, mlSolutionSetParser)
            .replaceComments()
            .addAttributes()
            .writeFile();
    }
}
