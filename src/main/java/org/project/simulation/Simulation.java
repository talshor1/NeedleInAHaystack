package org.project.simulation;

import java.util.ArrayList;
import java.util.List;

import static org.project.manager.Manager.runIndex;

public class Simulation {
    public static double runSimulation(){
        String[] values = {"Ann", "Doris", "Sandy", "Casey", "Harry", "Jeanne", "Jenna", "Brianna", "Clarence", "Howard", "Lynn"};
        return simulateQuery("First name", values, "./indexes", true);
    }

    public static double simulateQuery(String selectedField, String[] selectedValue, String indexFolderPath, boolean useIndex) {
        List<Long> executionTimes = new ArrayList<>();
        for (int i = 0; i < selectedValue.length; i++) {
            System.out.println("Running query on value " + selectedValue[i]);
            long startTime = System.nanoTime();
            if (useIndex) {
                runIndex(selectedField, selectedValue[i], indexFolderPath);
            }
            long endTime = System.nanoTime();
            executionTimes.add(endTime - startTime);
            System.out.println("The query took " + (endTime - startTime)/ 1_000_000_000.0 + " seconds");
        }
        long totalExecutionTime = executionTimes.stream().mapToLong(Long::longValue).sum();
        return totalExecutionTime / 20.0 / 1_000_000_000.0;
    }
}
