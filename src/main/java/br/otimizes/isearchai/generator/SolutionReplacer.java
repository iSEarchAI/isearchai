package br.otimizes.isearchai.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class SolutionReplacer {
    public static void main(String[] args) throws JsonProcessingException {
        generateObjectives("nrp-generate.json");
    }

    private static void generateObjectives(String file) throws JsonProcessingException {
        String jsonFile = readFileFromResources(file);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode json = objectMapper.readTree(jsonFile);
        String solutionName = json.get("solution").get("name").asText();
        String itemName = json.get("item").get("name").asText();
        String fileClass = readFileFromResources("nautilus-framework-plugin/src/main/java/org/nautilus/plugin/nrp/encoding/model/Solution.java");
        fileClass = fileClass.replaceAll("\\$solution\\.name", solutionName);
        fileClass = fileClass.replaceAll("\\$item\\.name", itemName);


        String methodTemplate = " public double get$objective() {\n" +
            "\n" +
            "        double sum = 0.0;\n" +
            "\n" +
            "        for ($item.name item : items) {\n" +
            "            sum += item.$attr;\n" +
            "        }\n" +
            "\n" +
            "        return sum;\n" +
            "    }";

        String methods = "";

        for (JsonNode jsonNode : json.get("item").get("objectives")) {
            methods += methodTemplate.replace("$item.name", itemName)
                .replace("$objective", jsonNode.asText())
                .replace("$attr", jsonNode.asText().toLowerCase()) + "\n\n\t";
        }
        fileClass = fileClass.replace("$methods", methods);
        System.out.println(fileClass);
        writeFile("generated/nautilus-framework-plugin/src/main/java/org/nautilus/plugin/nrp/encoding/model/" + solutionName + ".java", fileClass);

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
        try (InputStream inputStream = SolutionReplacer.class.getClassLoader().getResourceAsStream(fileName);
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
