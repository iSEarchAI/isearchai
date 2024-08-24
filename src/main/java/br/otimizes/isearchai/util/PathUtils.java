package br.otimizes.isearchai.util;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class PathUtils {

    private List<String> ignoreFiles = new ArrayList<>();

    public static void main(String[] args) {
        Path sourceDirectory = Paths.get("src/main/resources/nautilus-framework-plugin"); // Replace with the source folder path
        Path targetDirectory = Paths.get("generated/nautilus-framework-plugin"); // Replace with the target folder path

        try {
            new PathUtils().copyDirectoryRecursively(sourceDirectory, targetDirectory);
            System.out.println("Directory copied successfully.");
        } catch (IOException e) {
            System.err.println("Error occurred while copying the directory: " + e.getMessage());
        }
    }

    public void copyDirectoryRecursively(Path source, Path target, String...ignore) throws IOException {
        Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path targetDir = target.resolve(source.relativize(dir));
                if (!Files.exists(targetDir)) {
                    Files.createDirectories(targetDir);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (!ignoreFiles.contains(file.getFileName().toString())) {
                    Files.copy(file, target.resolve(source.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) {
                System.err.println("Failed to copy file: " + file.toString() + " due to " + exc.getMessage());
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public PathUtils withIgnoreFiles(List<String> ignoreFiles) {
        this.ignoreFiles = ignoreFiles;
        return this;
    }
}
