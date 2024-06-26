package org.project.gui;

import org.project.appConfig.AppConfig;
import org.project.cassandra.CassandraConnector;
import org.project.customer.Customer;
import org.project.manager.Manager;
import org.project.state.AppState;
import org.project.utils.utils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static org.project.utils.utils.isFolderNotEmpty;

public class IndexMenu extends JFrame {
    private final JComboBox<String> fieldComboBox;
    private final JTextField userInputField;
    private final JTextArea outputTextArea;
    private String selectedField;

    public IndexMenu(AppConfig config, AppState appState, CassandraConnector cassandraConnector) {
        setTitle("Needle in a Haystack Index");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(config.getWidth(), config.getHeight());
        setLayout(new GridLayout(3, 1)); // Set layout to GridLayout with 3 rows and 1 column

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(2, 1));
        String[] fields = {
                "Customer Id", "First Name", "Last Name", "Company", "City", "Country",
                "Phone1", "Phone2", "Email", "Subscription Date", "Website"
        };

        fieldComboBox = new JComboBox<>(fields);
        inputPanel.add(fieldComboBox);

        userInputField = new JTextField();
        userInputField.setBorder(BorderFactory.createTitledBorder("Please enter your value: [ SELECT * from customers WHERE <key> = <value>]"));
        inputPanel.add(userInputField);

        add(inputPanel);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 6));

        JButton indexButton = new JButton("Create Index");
        GUIUtils.paintButton(indexButton);
        GUIUtils.addHover(indexButton);
        buttonPanel.add(indexButton);

        JButton queryButton = new JButton("Run Query");
        GUIUtils.paintButton(queryButton);
        GUIUtils.addHover(queryButton);
        buttonPanel.add(queryButton);

        JButton deleteIndexButton = new JButton("Delete Index");
        GUIUtils.paintButton(deleteIndexButton);
        GUIUtils.addHover(deleteIndexButton);
        buttonPanel.add(deleteIndexButton);

        JButton noIndexButton = new JButton("Run without Index");
        GUIUtils.paintButton(noIndexButton);
        GUIUtils.addHover(noIndexButton);
        buttonPanel.add(noIndexButton);

        JButton closeButton = new JButton("Close");
        GUIUtils.paintButton(closeButton);
        GUIUtils.addHover(closeButton);
        buttonPanel.add(closeButton);

        JButton backButton = new JButton("Back");
        GUIUtils.paintButton(backButton);
        GUIUtils.addHover(backButton);
        buttonPanel.add(backButton);

        add(buttonPanel);

        // Output Panel
        outputTextArea = new JTextArea();
        outputTextArea.setLineWrap(true);
        outputTextArea.setWrapStyleWord(true);
        outputTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(outputTextArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Output"));
        add(scrollPane);

        // Button Actions
        backButton.addActionListener(e -> {
            new MainMenu(config, appState, cassandraConnector).setVisible(true);
            dispose();
        });

        fieldComboBox.addActionListener(e -> {
            selectedField = (String) fieldComboBox.getSelectedItem();
        });

        closeButton.addActionListener(e -> {
            this.setVisible(false);
            appState.stop();
            System.exit(0);
        });

        indexButton.addActionListener(e -> {
            boolean isIndexInitialized = isFolderNotEmpty(config.getIndexFolderPath());
            if (isIndexInitialized) {
                outputTextArea.setText("Index already initialized, first clean the current one");
                return;
            }
            selectedField = (String) fieldComboBox.getSelectedItem();
            Manager.createIndex(config.getNumberOfThreads(), selectedField, config.getFolderPath(), config.getNumberOfIndexFiles(), config.getIndexFolderPath(), appState);
            config.setNumberOfIndexFiles(utils.getNumberOfIndexFilesWritten(config.getIndexFolderPath()));
            outputTextArea.setText("Index created successfully.");
        });

        noIndexButton.addActionListener(e -> {
            String selectedValue = userInputField.getText();
            outputTextArea.setText("Running query: SELECT * FROM Customers WHERE " + selectedField + " = '" + selectedValue + "'");
            long startTime = System.nanoTime();
            List<Customer> customers = Manager.runWithoutIndex(selectedField, selectedValue, config.getFolderPath());
            long endTime = System.nanoTime();
            long elapsedTime = endTime - startTime;
            double elapsedTimeInSeconds = elapsedTime / 1_000_000_000.0;
            String customersUI = "Time took to query: " + elapsedTimeInSeconds + " seconds \n" + utils.getCustomersString(customers);
            outputTextArea.setText(customersUI);
        });

        queryButton.addActionListener(e -> {
            boolean isIndexInitialized = isFolderNotEmpty(config.getIndexFolderPath());
            String selectedValue = userInputField.getText();
            if (!isIndexInitialized) {
                outputTextArea.setText("Index is not initialized");
                return;
            }
            outputTextArea.setText("Running query: SELECT * FROM Customers WHERE " + selectedField + " = '" + selectedValue + "'");
            long startTime = System.nanoTime();
            List<Customer> customers = Manager.runIndex(selectedField, selectedValue, config.getIndexFolderPath());
            long endTime = System.nanoTime();
            long elapsedTime = endTime - startTime;
            double elapsedTimeInSeconds = elapsedTime / 1_000_000_000.0;
            String customersUI = "Time took to query: " + elapsedTimeInSeconds + " seconds \n" + utils.getCustomersString(customers);
            outputTextArea.setText(customersUI);
        });

        deleteIndexButton.addActionListener(e -> {
            outputTextArea.setText("Cleaning index dir");
            Manager.cleanIndexDirectory(config.getIndexFolderPath());
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
