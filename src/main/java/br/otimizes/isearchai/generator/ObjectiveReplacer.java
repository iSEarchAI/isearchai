package br.otimizes.isearchai.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ObjectiveReplacer {
    public static void main(String[] args) throws JsonProcessingException {
        generateObjectives("nrp-generate.json");
    }

    private static void generateObjectives(String file) throws JsonProcessingException {
        String jsonFile = readFileFromResources(file);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode json = objectMapper.readTree(jsonFile);
        JsonNode objectives = json.get("objectives");
        for (JsonNode objective : objectives) {
            String fileClass = readFileFromResources("nautilus-framework-plugin/src/main/java/org/nautilus/plugin/nrp/encoding/objective/Objective.java");
            String className = objective.get("name").asText();
            fileClass = fileClass.replace("$objective.name", className);
            fileClass = fileClass.replace("$objective.type", objective.get("type").asText());
            JsonNode incrementWithNode = objective.get("process").get("incrementWith");
            String incrementWith = incrementWithNode.asText();
            if (incrementWithNode.isTextual()) {
                incrementWith = "instance.getSumOf" + incrementWith + "()";
            }
            fileClass = fileClass.replace("$objective.process.incrementWith", incrementWith);

            fileClass = fileClass.replace("$objective.calculate.type", objective.get("calculate").get("type").asText());
            String valueA = objective.get("calculate").get("a").get("value").asText();
            if (!Objects.equals(valueA, "sum")) {
                valueA = "instance.getSumOf" + valueA + "()";
            }
            fileClass = fileClass.replace("$objective.calculate.a", valueA);

            String valueB = objective.get("calculate").get("b").get("value").asText();
            if (!Objects.equals(valueB, "sum")) {
                 valueB = "instance.getSumOf" + valueB + "()";
            }
            fileClass = fileClass.replace("$objective.calculate.b", valueB);
            writeFile("generated/nautilus-framework-plugin/src/main/java/org/nautilus/plugin/nrp/encoding/objective/" + className + ".java", fileClass);
        }
    }

    public static void writeFile(String filePath, String content) {
        File file = new File(filePath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
            System.out.println("File written successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFileFromResources(String fileName) {
        // Get the input stream of the file from resources
        try (InputStream inputStream = ObjectiveReplacer.class.getClassLoader().getResourceAsStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            if (inputStream == null) {
                throw new IllegalArgumentException("File not found: " + fileName);
            }

            // Read the file line by line
            String line;
            String toReturn = "";
            while ((line = reader.readLine()) != null) {
                toReturn += line + "\n";
                System.out.println(line);
            }
            return toReturn;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }
}
