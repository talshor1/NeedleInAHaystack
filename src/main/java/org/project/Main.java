package org.project;

import org.project.appConfig.AppConfig;
import org.project.gui.GUI;
import org.project.index.Index;
import org.project.manager.Manager;
import org.project.utils.utils;

import javax.swing.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.project.utils.utils.deleteFolderContents;
import static org.project.utils.utils.isFolderNotEmpty;

public class Main {
    public static void main(String[] args) {
        BlockingQueue<String> selectionQueue = new ArrayBlockingQueue<>(1);
        String selectedField = "";
        AppConfig config = null;
        boolean stop  = false;
        boolean isIndexInitialized;

        try {
            config = new AppConfig("config.json");
            System.out.println(config);
        } catch (Exception e){
            System.exit(1);
        }

        isIndexInitialized = isFolderNotEmpty(config.getIndexFolderPath());

        SwingUtilities.invokeLater(() -> new GUI(selectionQueue));

        while (!stop) {
            try {
                String result = selectionQueue.take();
                if (result.equals("CLEANTHEINDEXES")){
                    deleteFolderContents(config.getIndexFolderPath());
                    continue;
                }
                if (result.startsWith("QUERY")) {
                    if (!isIndexInitialized){
                        System.out.println("First initialize a index");
                        continue;
                    }
                    result = result.substring(5);
                    System.out.println("Running query: " + "SELECT * FROM Customers WHERE " + selectedField + " = '" + result + "'");
                    stop = true;
                } else {
                    selectedField = result;
                    System.out.println("Selected field: " + selectedField);
                    Index index = new Index();
                    Manager m = new Manager(config.getFolderPath(), utils.cleanFieldName(result), config.getNumberOfThreads(), index);
                    m.createIndexFile();
                    index.outputToMultipleFiles(config.getIndexFolderPath(), config.getNumberOfIndexFiles());
                    index.createMetadataFile(config.getIndexFolderPath(), config.getNumberOfIndexFiles());
                    isIndexInitialized = true;
                }
            } catch (InterruptedException e) {
                System.exit(1);
            }
        }
        System.exit(0);
    }
}