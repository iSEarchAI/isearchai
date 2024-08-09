package br.otimizes.isearchai.rest;

import br.otimizes.isearchai.generator.model.Generate;
import br.otimizes.isearchai.generator.replacers.Generator;
import br.otimizes.isearchai.util.ObjMapUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static spark.Spark.*;


public class Application {
    public static void main(String[] args) {
        port(8080);
        staticFileLocation("/public");
        get("/hello", (req, res) -> "Hello, World!");
        post("/generate", (req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Methods", "POST");
            res.header("Access-Control-Allow-Headers", "Content-Type");
            res.header("Content-Disposition", "attachment; filename=\"nautilus-framework-plugin.zip\"");
            res.type("application/zip");

            String f = req.body();
//            Generate generate = ObjMapUtils.mapper().readValue(body, Generate.class);
//            Generator.generate(generate);
            ByteArrayOutputStream byteArrayOutputStream = createByteArrayOutputStreamFromFile("generated/nautilus-framework-plugin.zip");
            // Set response headers

            // Write the zip content to the response
            res.raw().getOutputStream().write(byteArrayOutputStream.toByteArray());
            res.raw().getOutputStream().flush();
            res.raw().getOutputStream().close();

            return res.raw();
        });
        options("/*",
            (request, response) -> {

                String accessControlRequestHeaders = request
                    .headers("Access-Control-Request-Headers");
                if (accessControlRequestHeaders != null) {
                    response.header("Access-Control-Allow-Headers",
                        accessControlRequestHeaders);
                }

                String accessControlRequestMethod = request
                    .headers("Access-Control-Request-Method");
                if (accessControlRequestMethod != null) {
                    response.header("Access-Control-Allow-Methods",
                        accessControlRequestMethod);
                }

                return "OK";
            });
        after((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "*");
        });
//        init();
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
