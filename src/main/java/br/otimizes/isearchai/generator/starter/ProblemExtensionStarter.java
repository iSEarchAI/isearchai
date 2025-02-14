package br.otimizes.isearchai.generator.starter;

import br.otimizes.isearchai.generator.model.Generate;
import br.otimizes.isearchai.generator.model.Objective;
import br.otimizes.isearchai.generator.model.ProblemType;
import br.otimizes.isearchai.util.ObjMapUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * The type Problem extension starter.
 */
public class ProblemExtensionStarter {
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
        String fileClass = readFileFromResources("nautilus-framework-plugin/src/main/java/org/nautilus/plugin/nrp/extension/problem/ProblemExtension.java");

        String methods = "";
        String imports = "";
        ProblemType type = json.getProblem().getType();
        Class nautilusSolution = type.getNautilusSolution();
        for (Objective jsonNode : json.getObjectives()) {
            methods += "objectives.add(new " + jsonNode.getName() + "());\n\t\t";
            imports += "import org.nautilus.plugin.nrp.encoding.objective." + jsonNode.getName() + ";\n";
            imports += "import " + nautilusSolution.getName() + ";\n";
        }
        fileClass = fileClass.replace("$methods", methods);

        fileClass = fileClass.replaceAll("\\$nautilusSolution\\.name", nautilusSolution.getSimpleName());
        fileClass = fileClass.replace("$imports", imports);
        fileClass = fileClass.replace("$getVariablesAsListCode", type.getVariablesAsListBody());


        System.out.println(fileClass);
        writeFile("generated/nautilus-framework-plugin/src/main/java/org/nautilus/plugin/nrp/extension/problem/ProblemExtension.java", fileClass);
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
        try (InputStream inputStream = ProblemExtensionStarter.class.getClassLoader().getResourceAsStream(fileName);
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
