package br.otimizes.isearchai.rest;

import br.otimizes.isearchai.generator.model.Generate;
import br.otimizes.isearchai.generator.replacers.Generator;
import br.otimizes.isearchai.util.ObjMapUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static spark.Spark.*;


public class Application {
    public static void main(String[] args) {
        port(8080);
        get("/hello", (req, res) -> "Hello, World!");
        post("/generate", (req, res) -> {
            String body = req.body();
            Generate generate = ObjMapUtils.mapper().readValue(body, Generate.class);
            Generator.generate(generate);
            ByteArrayOutputStream byteArrayOutputStream = createByteArrayOutputStreamFromFile("generated/nautilus-framework-plugin.zip");
            // Set response headers
            res.type("application/zip");
            res.header("Content-Disposition", "attachment; filename=\"nautilus-framework-plugin.zip\"");

            // Write the zip content to the response
            res.raw().getOutputStream().write(byteArrayOutputStream.toByteArray());
            res.raw().getOutputStream().flush();
            res.raw().getOutputStream().close();

            return res.raw();
        });
    }

    public static ByteArrayOutputStream createByteArrayOutputStreamFromFile(String filePath) {
        Path path = Paths.get(filePath);
        byte[] fileBytes = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            fileBytes = Files.readAllBytes(path);
            byteArrayOutputStream.write(fileBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return byteArrayOutputStream;
    }
}
