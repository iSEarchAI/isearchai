package br.otimizes.isearchai.generator.starter;

import br.otimizes.isearchai.generator.model.Generate;
import br.otimizes.isearchai.generator.model.Objective;
import br.otimizes.isearchai.util.ObjMapUtils;
import br.otimizes.isearchai.util.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ObjectiveStarter {
    public static void main(String[] args) throws JsonProcessingException {
        generateForFile("nrp-generate.json");
    }

    public static void generateForFile(String file) throws JsonProcessingException {
        String jsonFile = readFileFromResources(file);
        generate(ObjMapUtils.mapper().readValue(jsonFile, Generate.class));
    }

    public static void generate(Generate jsonFile) throws JsonProcessingException {
        List<Objective> objectives = jsonFile.getObjectives();
        for (Objective objective : objectives) {
            String fileClass = readFileFromResources("nautilus-framework-plugin/src/main/java/org/nautilus/plugin/nrp/encoding/objective/Objective.java");
            String className = objective.getName();
            fileClass = fileClass.replace("$objective.name", className);
            fileClass = fileClass.replace("$objective.type", objective.getType());
            String incrementWith = objective.getProcess().getIncrementWith();

            if (!isNumber(incrementWith)) {
                incrementWith = "instance.get" + incrementWith.toLowerCase() + "(i)";
            }
            fileClass = fileClass.replace("$objective.process.incrementWith", incrementWith);
            fileClass = fileClass.replace("$objective.maximize", objective.getMaximize() ? "true": "false");

            List<String> expressionList = objective.getCalculate().getExpression();

            String expression = (objective.getCalculate().getInvert() ? "1.0 - " : "") + expressionList.stream().map(v -> {
                if (Objects.equals(v, "sum"))
                    return "(double) " + v;
                if (StringUtils.isMathOperator(v))
                    return v;
                return "(double) instance.getSumOf" + StringUtils.camelcase(v) + "()";
            }).collect(Collectors.joining(" "));

            fileClass = fileClass.replace("$objective.calculate.expression", expression);

            writeFile("generated/nautilus-framework-plugin/src/main/java/org/nautilus/plugin/nrp/encoding/objective/" + className + ".java", fileClass);
        }
    }

    private static boolean isNumber(String incrementWith) {
        try {
            return Double.parseDouble(incrementWith) > 0;
        } catch (RuntimeException ex) {
            return false;
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
        try (InputStream inputStream = ObjectiveStarter.class.getClassLoader().getResourceAsStream(fileName);
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
