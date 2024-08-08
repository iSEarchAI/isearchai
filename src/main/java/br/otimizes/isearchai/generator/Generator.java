package br.otimizes.isearchai.generator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Generator {
    public static void main(String[] args) {

        try {
//            extractAndinstallDependencies();
//            extractZipFileFromResources("nautilus-framework-plugin.zip", "generated");
            ObjectiveReplacer.generate("nrp-generate.json");
            ItemReplacer.generate("nrp-generate.json");
            SolutionReplacer.generate("nrp-generate.json");
            TXTInstanceReplacer.generate("nrp-generate.json");
            ProblemExtensionReplacer.generate("nrp-generate.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void extractAndinstallDependencies() throws IOException {
        extractZipFileFromResources("jvalidation.zip", "generated");
        mvnCleanInstall("generated/jvalidation");
        extractZipFileFromResources("nautilus-framework.zip", "generated");
        mvnCleanInstall("generated/nautilus-framework");
    }

    private static void mvnCleanInstall(String folder) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(folder));
        processBuilder.command("/home/wmfsystem/.sdkman/candidates/maven/3.9.8/bin/mvn", "clean", "install", "-DskipTests");

        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            System.out.println("\nExited with code: " + exitCode);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    public static void extractZipFileFromResources(String zipFileName, String outputDir) throws IOException {
        // Get the input stream of the zip file from resources
        try (InputStream inputStream = Generator.class.getClassLoader().getResourceAsStream(zipFileName);
             ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {

            if (inputStream == null) {
                throw new IOException("File not found: " + zipFileName);
            }

            // Create the output directory if it doesn't exist
            Files.createDirectories(Paths.get(outputDir));

            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                String filePath = outputDir + File.separator + entry.getName();

                if (entry.isDirectory()) {
                    Files.createDirectories(Paths.get(filePath));
                } else {
                    // Ensure parent directories exist
                    Files.createDirectories(Paths.get(filePath).getParent());

                    // Write file content
                    try (FileOutputStream fos = new FileOutputStream(filePath)) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = zipInputStream.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                    }
                }
                zipInputStream.closeEntry();
            }
        }
    }
}
