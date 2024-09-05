package br.otimizes.isearchai.generator.starter;

import br.otimizes.isearchai.generator.model.Generate;
import br.otimizes.isearchai.util.MavenUtils;
import br.otimizes.isearchai.util.PathUtils;
import br.otimizes.isearchai.util.ZipUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Starter {
    public static void main(String[] args) throws IOException {
        generateForFile();
    }

    public static void generate(Generate file) {
        try {
            FileUtils.deleteDirectory(new File("generated/nautilus-framework-plugin"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            extractAndinstallDependencies();
            ObjectiveStarter.generate(file);
            ElementStarter.generate(file);
            SolutionStarter.generate(file);
            TXTInstanceStarter.generate(file);
            ProblemExtensionStarter.generate(file);
            ProblemStarter.generate(file);
            SearchAlgorithmStarter.generate(file);
            ZipUtils.zipFolder("generated/nautilus-framework-plugin", "generated/nautilus-framework-plugin.zip");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateForFile() {
        try {
            extractAndinstallDependencies();
            ObjectiveStarter.generateForFile("nrp-generate.json");
            ElementStarter.generateForFile("nrp-generate.json");
            SolutionStarter.generateForFile("nrp-generate.json");
            TXTInstanceStarter.generateForFile("nrp-generate.json");
            ProblemExtensionStarter.generateForFile("nrp-generate.json");
            ProblemStarter.generateForFile("nrp-generate.json");
            SearchAlgorithmStarter.generateForFile("nrp-generate.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void extractAndinstallDependencies() throws IOException {
        ZipUtils.extractZipFileFromResources("jvalidation.zip", "generated");
        MavenUtils.cleanInstall("generated/jvalidation");
        MavenUtils.cleanInstall("generated/nautilus-framework");
        initProject();
    }

    private static void initProject() {
        Path sourceDirectory = Paths.get("src/main/resources/nautilus-framework-plugin"); // Replace with the source folder path
        Path targetDirectory = Paths.get("generated/nautilus-framework-plugin"); // Replace with the target folder path

        try {
            new PathUtils()
                .withIgnoreFiles(Arrays.asList(
                    "TXTInstance.java", "Element.java", "Solution.java", "Objective.java", "ProblemExtension.java"
                ))
                .copyDirectoryRecursively(sourceDirectory, targetDirectory);
            System.out.println("Directory copied successfully.");
        } catch (IOException e) {
            System.err.println("Error occurred while copying the directory: " + e.getMessage());
        }
    }

}
