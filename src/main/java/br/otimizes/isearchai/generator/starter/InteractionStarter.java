package br.otimizes.isearchai.generator.starter;

import br.otimizes.isearchai.generator.model.Generate;
import br.otimizes.isearchai.generator.model.ProblemType;
import br.otimizes.isearchai.util.ObjMapUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * The type Interaction starter.
 */
public class InteractionStarter {


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
        String fileClass = readFileFromResources("nautilus-framework-plugin/src/main/java/org/nautilus/plugin/nrp/encoding/runner/MyInteractionRunner.java");


        ProblemType type = json.getProblem().getType();
        Class searchAlgorithmClass = type.getRunners()
                .stream().filter(clazz -> clazz.getSuperclass().equals(json.getSearchAlgorithm().getRunner().runner())).findFirst().orElse(null);

        String imports = "import " + type.getSolution().getName() + ";\nimport " +
                searchAlgorithmClass.getName() + ";";


        fileClass = fileClass.replace("$searchAlgorithm.imports", imports);
        fileClass = fileClass.replace("$searchAlgorithm.solution", type.getSolution().getSimpleName());
        fileClass = fileClass.replace("$interaction.first", json.getInteraction().getFirst() + "");
        fileClass = fileClass.replace("$interaction.max", json.getInteraction().getMax() + "");
        fileClass = fileClass.replace("$interaction.interval", json.getInteraction().getInterval() + "");
        writeFile("generated/nautilus-framework-plugin/src/main/java/org/nautilus/plugin/nrp/encoding/runner/MyInteractionRunner.java", fileClass);
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
        try (InputStream inputStream = InteractionStarter.class.getClassLoader().getResourceAsStream(fileName);
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
