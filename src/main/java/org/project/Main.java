package org.project;

import org.project.appConfig.AppConfig;
import org.project.gui.GUI;
import org.project.index.Index;
import org.project.manager.Manager;
import org.project.utils.utils;

import javax.swing.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static void main(String[] args) {
        String selectedField = "";
        BlockingQueue<String> selectionQueue = new ArrayBlockingQueue<>(1);
        AppConfig config = null;

        try {
            config = new AppConfig("config.json");
            System.out.println(config);
        } catch (Exception e){
            System.exit(1);
        }

        SwingUtilities.invokeLater(() -> new GUI(selectionQueue));

        try {
            selectedField = selectionQueue.take();
            System.out.println("Selected field: " + selectedField);
        } catch (InterruptedException e) {
            System.exit(1);
        }

        Index index = new Index();
        Manager m = new Manager(config.getFolderPath(), utils.cleanFieldName(selectedField), config.getNumberOfThreads(), index);
        m.createIndexFile();
        index.outputToMultipleFiles(config.getIndexFolderPath());
    }
}