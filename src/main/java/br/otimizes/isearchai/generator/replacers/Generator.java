package br.otimizes.isearchai.generator.replacers;

import br.otimizes.isearchai.generator.model.Generate;
import br.otimizes.isearchai.util.MavenUtils;
import br.otimizes.isearchai.util.ZipUtils;

import java.io.*;

public class Generator {
    public static void main(String[] args) throws IOException {
        ZipUtils.zipFolder("generated/nautilus-framework-plugin", "generated/nautilus-framework-plugin.zip");
    }

    public static void generate(Generate file) {
        try {
            extractAndinstallDependencies();
            ZipUtils.extractZipFileFromResources("nautilus-framework-plugin.zip", "generated");
            ObjectiveReplacer.generate(file);
            ItemReplacer.generate(file);
            SolutionReplacer.generate(file);
            TXTInstanceReplacer.generate(file);
            ProblemExtensionReplacer.generate(file);
            ZipUtils.zipFolder("generated/nautilus-framework-plugin", "generated/nautilus-framework-plugin.zip");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    public static void generateForFile() {
        try {
            extractAndinstallDependencies();
            ZipUtils.extractZipFileFromResources("nautilus-framework-plugin.zip", "generated");
            ObjectiveReplacer.generateForFile("nrp-generate.json");
            ItemReplacer.generateForFile("nrp-generate.json");
            SolutionReplacer.generateForFile("nrp-generate.json");
            TXTInstanceReplacer.generateForFile("nrp-generate.json");
            ProblemExtensionReplacer.generateForFile("nrp-generate.json");
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
