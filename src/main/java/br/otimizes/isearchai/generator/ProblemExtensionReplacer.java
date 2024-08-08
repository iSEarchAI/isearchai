package br.otimizes.isearchai.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ProblemExtensionReplacer {
    public static void main(String[] args) throws JsonProcessingException {
        generate("nrp-generate.json");
    }

    public static void generate(String file) throws JsonProcessingException {
        String jsonFile = readFileFromResources(file);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode json = objectMapper.readTree(jsonFile);
        String fileClass = readFileFromResources("nautilus-framework-plugin/src/main/java/org/nautilus/plugin/nrp/extension/problem/ProblemExtension.java");

        String methods = "";
        String imports = "";
        for (JsonNode jsonNode : json.get("objectives")) {
            methods += "objectives.add(new " + jsonNode.get("name").asText() + "());\n\t\t";
            imports += "import org.nautilus.plugin.nrp.encoding.objective." + jsonNode.get("name").asText() + ";\n";
        }
        fileClass = fileClass.replace("$methods", methods);
        fileClass = fileClass.replace("$imports", imports);
        System.out.println(fileClass);
        writeFile("generated/nautilus-framework-plugin/src/main/java/org/nautilus/plugin/nrp/extension/problem/ProblemExtension.java", fileClass);

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
        try (InputStream inputStream = ProblemExtensionReplacer.class.getClassLoader().getResourceAsStream(fileName);
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
