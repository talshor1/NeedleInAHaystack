package org.project.gui;

import org.project.appConfig.AppConfig;
import org.project.cassandra.CassandraConnector;
import org.project.csv.CSVSplitter;
import org.project.state.AppState;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class SplitCSVMenu extends JFrame {

    private JTextField csvFilePathField;
    private JTextField outputDirField;
    private JTextField splitCountField;

    public SplitCSVMenu(AppConfig config, AppState appState, CassandraConnector cassandraConnector) {
        setTitle("Split CSV");
        setSize(config.getWidth(), config.getHeight());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel csvFilePathLabel = new JLabel("CSV File Path:");
        csvFilePathField = new JTextField(20);

        JLabel outputDirLabel = new JLabel("Output Directory:");
        outputDirField = new JTextField(20);

        JLabel splitCountLabel = new JLabel("Records per file:");
        splitCountField = new JTextField(20);

        JButton backButton = new JButton("Back");
        GUIUtils.addHover(backButton);
        GUIUtils.paintButton(backButton);

        JButton splitButton = new JButton("Split");
        GUIUtils.addHover(splitButton);
        GUIUtils.paintButton(splitButton);

        backButton.addActionListener(e -> {
            new MainMenu(config, appState, cassandraConnector).setVisible(true);
            dispose();
        });

        splitButton.addActionListener(e -> {
            try {
                splitCSV();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        setLayout(new GridLayout(5, 2));
        add(csvFilePathLabel);
        add(csvFilePathField);
        add(outputDirLabel);
        add(outputDirField);
        add(splitCountLabel);
        add(splitCountField);
        add(backButton);
        add(splitButton);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void splitCSV() throws IOException {
        String csvFilePath = csvFilePathField.getText();
        String outputDir = outputDirField.getText();
        int filesAmount = 1;

        try {
            filesAmount = Integer.parseInt(splitCountField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for split count.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        CSVSplitter.splitCSV(csvFilePath, outputDir, filesAmount);
    }
}
