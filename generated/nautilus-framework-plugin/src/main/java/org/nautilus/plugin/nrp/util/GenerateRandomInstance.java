package org.nautilus.plugin.nrp.util;

import org.nautilus.plugin.nrp.encoding.model.Requirement;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import java.util.ArrayList;
import java.util.List;

public class GenerateRandomInstance {

    protected static JMetalRandom random = JMetalRandom.getInstance();

    public static String generate(int numberOfRequirements) {

        StringBuilder builder = new StringBuilder();

        builder.append("# number of requirements").append("\n");
        builder.append(numberOfRequirements).append("\n");

        List<Requirement> requirements = new ArrayList<>(numberOfRequirements);

        for (int i = 0; i < numberOfRequirements; i++) {
            requirements.add(Requirement.getRandom());
        }

        builder.append("# number of items").append("\n");

        for (int i = 0; i < requirements.size(); i++) {
			
            builder.append(requirements.get(i).items.size());

            if (i + 1 != requirements.size()) {
                builder.append(" ");
            }
        }

        builder.append("\n");

        for (int i = 0; i < requirements.size(); i++) {

            builder.append("# requirement " + i).append("\n");

            Requirement requirement = requirements.get(i);

            for (int j = 0; j < requirement.items.size(); j++) {

                builder.append(requirement.items.get(j).cost)
                    .append(" ")
                    .append(requirement.items.get(j).profit)
                    .append(" ")
                    .append(requirement.items.get(j).importance)
                    .append("\n");
            }
        }

        return builder.toString();
    }

}

