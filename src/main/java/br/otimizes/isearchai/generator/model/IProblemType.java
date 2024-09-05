package br.otimizes.isearchai.generator.model;

import java.util.List;

public interface IProblemType {
    String getPackage();
    Class getSolution();
    Class getSolutionSet();
    Class getProblem();

    String getBody();
    List<Class> getRunners();
}
