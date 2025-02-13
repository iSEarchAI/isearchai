package br.otimizes.isearchai.generator.model;

import java.util.List;

public interface IProblemType {
    String getPackage();
    Class getSolution();
    Class getSolutionSet();
    Class getProblem();
    Class getNautilusSolution();
    String getVariablesAsListBody();
    String getTXTInstanceBody();

    String getBody();
    List<Class> getRunners();
}
