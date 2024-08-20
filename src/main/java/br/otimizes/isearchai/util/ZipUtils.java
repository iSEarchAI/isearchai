package br.otimizes.isearchai.util;

import br.otimizes.isearchai.generator.starter.Starter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtils {
    public static void main(String[] args) {
        String sourceFolder = "path/to/your/folder"; // Change to your folder path
        String zipFilePath = "path/to/your/output.zip"; // Change to your output zip path

        try {
            zipFolder(sourceFolder, zipFilePath);
            System.out.println("Folder successfully zipped to " + zipFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void extractZipFileFromResources(String zipFileName, String outputDir) throws IOException {
        // Get the input stream of the zip file from resources
        InputStream resourceAsStream = Starter.class.getClassLoader().getResourceAsStream(zipFileName);
        unzip(outputDir, resourceAsStream);
    }

    public static void unzip(String outputDir, InputStream resourceAsStream) {
        // Create the directory if it doesn't exist
        File dir = new File(outputDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (InputStream inputStream = resourceAsStream;
             ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {

            if (inputStream == null) {
                throw new IOException("File not found");
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
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void zipFolder(String sourceFolder, String zipFilePath) throws IOException {
        Path sourceFolderPath = Paths.get(sourceFolder);
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFilePath))) {
            Files.walkFileTree(sourceFolderPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    zos.putNextEntry(new ZipEntry(sourceFolderPath.relativize(file).toString()));
                    Files.copy(file, zos);
                    zos.closeEntry();
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    zos.putNextEntry(new ZipEntry(sourceFolderPath.relativize(dir).toString() + "/"));
                    zos.closeEntry();
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        }
    }
}
