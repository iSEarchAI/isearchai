package br.otimizes.isearchai.generator.starter;

import br.otimizes.isearchai.generator.model.Element;
import br.otimizes.isearchai.generator.model.Generate;
import br.otimizes.isearchai.util.ObjMapUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ElementStarter {
    public static void main(String[] args) throws JsonProcessingException {
        generateForFile("nrp-generate.json");
    }

    public static void generateForFile(String file) throws JsonProcessingException {
        String jsonFile = readFileFromResources(file);
        generate(ObjMapUtils.mapper().readValue(jsonFile, Generate.class));

    }

    public static void generate(Generate json) throws JsonProcessingException {
        Element element = json.getElement();
        String name = element.getName();
        String fileClass = readFileFromResources("nautilus-framework-plugin/src/main/java/org/nautilus/plugin/nrp/encoding/model/Element.java");
        fileClass = fileClass.replaceAll("\\$element\\.name", name);

        List<String> objectives = new ArrayList<>();
        for (String jsonNode : element.getObjectives()) {
            objectives.add(jsonNode.toLowerCase());
        }
        String objectivesAttributes = objectives.stream().map(str -> "public double " + str + ";").collect(Collectors.joining("\n\t"));
        String objectivesParams = objectives.stream().map(str -> "double " + str).collect(Collectors.joining(","));
        String objectivesConstructor = objectives.stream().map(str -> "this." + str + " = " + str + ";").collect(Collectors.joining("\n\t\t"));
        String objectivesRandomParams = objectives.stream().map(str -> "Converter.round(random.nextDouble(1, 10), 1)").collect(Collectors.joining(","));

        fileClass = fileClass.replace("$element.objectivesAttributes", objectivesAttributes);
        fileClass = fileClass.replace("$element.objectivesParams", objectivesParams);
        fileClass = fileClass.replace("$element.objectivesConstructor", objectivesConstructor);
        fileClass = fileClass.replace("$element.objectivesRandomParams", objectivesRandomParams);
        System.out.println(fileClass);
        writeFile("generated/nautilus-framework-plugin/src/main/java/org/nautilus/plugin/nrp/encoding/model/" + name + ".java", fileClass);
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
        try (InputStream inputStream = ElementStarter.class.getClassLoader().getResourceAsStream(fileName);
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
