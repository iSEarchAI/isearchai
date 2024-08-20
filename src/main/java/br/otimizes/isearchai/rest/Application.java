package br.otimizes.isearchai.rest;

import br.otimizes.isearchai.adapter.Adapter;
import br.otimizes.isearchai.generator.model.Generate;
import br.otimizes.isearchai.generator.starter.Starter;
import br.otimizes.isearchai.util.ObjMapUtils;
import br.otimizes.isearchai.util.ZipUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

import static spark.Spark.*;


public class Application {
    public static void main(String[] args) {
        port(8080);
        staticFileLocation("/public/isearchai/build");
        before((request, response) -> {
            request.raw().setAttribute("org.eclipse.jetty.multipartConfig", new javax.servlet.MultipartConfigElement("/temp"));
        });
        get("/hello", (req, res) -> "Hello, World!");
        post("/upload/zip/:fileName", (request, response) -> {
            response.type("application/json");
            String fileName = request.params(":fileName");

            try {
                // Get the file from the request
                InputStream fileInputStream = request.raw().getPart("file").getInputStream();
                ZipUtils.unzip("generated/" + fileName, fileInputStream);
//
//                // Specify the path where the file will be saved
//                String filePath = "generated/" + fileName + ".zip";
//
//                // Save the file
//                FileOutputStream outputStream = new FileOutputStream(filePath);
//                IOUtils.copy(fileInputStream, outputStream);
//                outputStream.close();


                // Respond with success
                return "{\"status\":\"success\"}";
            } catch (Exception e) {
                response.status(500);
                return "{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}";
            }
        });

        get("/adapt/files", (req, res) -> {
//            res.header("Access-Control-Allow-Origin", "*");
//            res.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
//            res.header("Access-Control-Allow-Headers", "Content-Type");
//            res.type("application/json");
//
            Path startDir = Paths.get("generated/adapt");
//
            ArrayList<String> files = new ArrayList<>();
            // Use try-with-resources to ensure the stream is closed after use
            try (Stream<Path> stream = Files.walk(startDir)) {
                // Iterate over the stream and print each file
                stream
                    .filter(Files::isRegularFile) // Filter to only include files, not directories
                    .forEach(file -> {
                        String filePath = file.toString();
                        if (filePath.contains("src/main/java"))
                            files.add(filePath);
                    }); // Print the file path
            } catch (IOException e) {
                e.printStackTrace();
            }

            res.body(ObjMapUtils.mapper().writeValueAsString(files));
            return ObjMapUtils.mapper().writeValueAsString(files);
        });

        post("/adapt", (req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Methods", "POST");
            res.header("Access-Control-Allow-Headers", "Content-Type");
            res.header("Content-Disposition", "attachment; filename=\"nautilus-framework-plugin.zip\"");
            res.type("application/zip");

            Adapter adapter = ObjMapUtils.mapper().readValue(req.body(), Adapter.class);
            adapter.implement();

            return res.raw();
        });
        post("/generate", (req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Methods", "POST");
            res.header("Access-Control-Allow-Headers", "Content-Type");
            res.header("Content-Disposition", "attachment; filename=\"nautilus-framework-plugin.zip\"");
            res.type("application/zip");

            Generate generate = ObjMapUtils.mapper().readValue(req.body(), Generate.class);
            Starter.generate(generate);
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
