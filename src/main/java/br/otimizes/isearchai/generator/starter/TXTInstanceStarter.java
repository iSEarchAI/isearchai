package br.otimizes.isearchai.generator.starter;

import br.otimizes.isearchai.generator.model.Generate;
import br.otimizes.isearchai.generator.model.ProblemType;
import br.otimizes.isearchai.util.ObjMapUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The type Txt instance starter.
 */
public class TXTInstanceStarter {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws JsonProcessingException the json processing exception
     */
    public static void main(String[] args) throws JsonProcessingException {
        generateForFile("nrp-generate.json");
    }

    /**
     * Generate for file.
     *
     * @param file the file
     * @throws JsonProcessingException the json processing exception
     */
    public static void generateForFile(String file) throws JsonProcessingException {
        String jsonFile = readFileFromResources(file);
        generate(ObjMapUtils.mapper().readValue(jsonFile, Generate.class));

    }

    /**
     * Generate.
     *
     * @param json the json
     * @throws JsonProcessingException the json processing exception
     */
    public static void generate(Generate json) throws JsonProcessingException {
        String solutionName = json.getSolution().getName();
        String elementName = json.getElement().getName();
        String fileClass = readFileFromResources("nautilus-framework-plugin/src/main/java/org/nautilus/plugin/nrp/encoding/instance/TXTInstance.java");
        fileClass = fileClass.replace("$txtInstanceBody", json.getProblem().getType().getTXTInstanceBody());
        fileClass = fileClass.replaceAll("\\$solution\\.name", solutionName);
        fileClass = fileClass.replaceAll("\\$element\\.name", elementName);

        List<String> objectives = new ArrayList<>();
        for (String jsonNode : json.getElement().getObjectives()) {
            objectives.add(jsonNode.toLowerCase());
        }
        String objectivesAttributes = objectives.stream().map(str -> "public double sumOf" + str + " = 1").collect(Collectors.joining(";\n\t")) + ";";
        String listAttributes = objectives.stream().map(str -> "public List<Double> solution" + str).collect(Collectors.joining(";\n\t")) + ";";
        String objectivesParams = objectives.stream().map(str -> "double" + str).collect(Collectors.joining(","));
        String objectivesConstructor = objectives.stream().map(str -> "this.solution" + str + " = new ArrayList<>()").collect(Collectors.joining(";\n\t")) + ";";
        String valuesGet = IntStream.range(0, objectives.size())
            .mapToObj(i -> "values.get(" + i + ")")
            .collect(Collectors.joining(", "));
//        String valuesGet = objectives.stream().map(str -> "values.get(0)").collect(Collectors.joining(",")); // TODO
        String solutionFor = objectives.stream().map(str -> "this.solution" + str + ".add(solution.get" + capitalizeFirstLetter(str) + "());\n\t\t\t").collect(Collectors.joining(""));
        String solutionStream = objectives.stream().map(str -> "this.sumOf" + str + " = this.solution" + str + ".stream().mapToDouble(e -> e).sum()").collect(Collectors.joining(";\n\t")) + ";";
        String getters = objectives.stream().map(str -> "public double getSumOf" + capitalizeFirstLetter(str) + "() {return this.sumOf" + str + ";}").collect(Collectors.joining("\n\n\t"));
        String geti = objectives.stream().map(str -> "public double get" + str + "(int solutionId) {return " + (json.getProblem().getType().equals(ProblemType.BINARY) ? "this.solution" + str + ".get(solutionId)" : "1") + ";}").collect(Collectors.joining("\n\n\t"));
        String dataGet = objectives.stream().map(str -> "\"\" + data.get" + str + "(i)").collect(Collectors.joining(","));


        fileClass = fileClass.replace("$element.objectivesAttributes", objectivesAttributes);
        fileClass = fileClass.replace("$element.listAttributes", listAttributes);
        fileClass = fileClass.replace("$element.objectivesParams", objectivesParams);
        fileClass = fileClass.replace("$element.objectivesConstructor", objectivesConstructor);
        fileClass = fileClass.replace("$element.valuesGet", valuesGet);
        fileClass = fileClass.replace("$element.solutionFor", solutionFor);
        fileClass = fileClass.replace("$element.solutionStream", solutionStream);
        fileClass = fileClass.replace("$element.getters", getters);
        fileClass = fileClass.replace("$element.geti", geti);
        fileClass = fileClass.replace("$element.dataGet", dataGet);
        fileClass = fileClass.replace("$element.objStrList", objectives.stream().map(str -> "\"" + str + "\"").collect(Collectors.joining(",")));
        System.out.println(fileClass);

        writeFile("generated/nautilus-framework-plugin/src/main/java/org/nautilus/plugin/nrp/encoding/instance/TXTInstance.java", fileClass);
    }

    /**
     * Capitalize first letter string.
     *
     * @param str the str
     * @return the string
     */
    public static String capitalizeFirstLetter(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * Write file.
     *
     * @param filePath the file path
     * @param content  the content
     */
    public static void writeFile(String filePath, String content) {
        File file = new File(filePath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
            System.out.println("File written successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read file from resources string.
     *
     * @param fileName the file name
     * @return the string
     */
    public static String readFileFromResources(String fileName) {
        // Get the input stream of the file from resources
        try (InputStream inputStream = TXTInstanceStarter.class.getClassLoader().getResourceAsStream(fileName);
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
