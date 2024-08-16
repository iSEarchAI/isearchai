package br.otimizes.isearchai.generator.starter;

import br.otimizes.isearchai.generator.model.Generate;
import br.otimizes.isearchai.util.MavenUtils;
import br.otimizes.isearchai.util.ZipUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class Starter {
    public static void main(String[] args) throws IOException {
        ZipUtils.zipFolder("generated/nautilus-framework-plugin", "generated/nautilus-framework-plugin.zip");
    }

    public static void generate(Generate file) {
        try {
            FileUtils.deleteDirectory(new File("generated/nautilus-framework-plugin"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            extractAndinstallDependencies();
            ZipUtils.extractZipFileFromResources("nautilus-framework-plugin.zip", "generated");
            ObjectiveStarter.generate(file);
            ItemStarter.generate(file);
            SolutionStarter.generate(file);
            TXTInstanceStarter.generate(file);
            ProblemExtensionStarter.generate(file);
            ZipUtils.zipFolder("generated/nautilus-framework-plugin", "generated/nautilus-framework-plugin.zip");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateForFile() {
        try {
            extractAndinstallDependencies();
            ZipUtils.extractZipFileFromResources("nautilus-framework-plugin.zip", "generated");
            ObjectiveStarter.generateForFile("nrp-generate.json");
            ItemStarter.generateForFile("nrp-generate.json");
            SolutionStarter.generateForFile("nrp-generate.json");
            TXTInstanceStarter.generateForFile("nrp-generate.json");
            ProblemExtensionStarter.generateForFile("nrp-generate.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void extractAndinstallDependencies() throws IOException {
        ZipUtils.extractZipFileFromResources("jvalidation.zip", "generated");
        MavenUtils.cleanInstall("generated/jvalidation");
        ZipUtils.extractZipFileFromResources("nautilus-framework.zip", "generated");
        MavenUtils.cleanInstall("generated/nautilus-framework");
    }

}
