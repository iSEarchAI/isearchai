package br.otimizes.isearchai.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class MavenUtils {

    public static void cleanInstall(String folder) {
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
}
