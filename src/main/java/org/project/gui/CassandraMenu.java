package org.project.gui;

import org.project.appConfig.AppConfig;
import org.project.cassandra.CassandraConnector;
import org.project.state.AppState;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class CassandraMenu extends JFrame {
    private final JTextField userInputField;
    private JTextArea outputTextArea;
    private String selectedField;
    private final JComboBox<String> fieldComboBox;

    public CassandraMenu(AppConfig config, AppState appState, CassandraConnector cassandraConnector) {
        String[] fields = {
                "Customer_Id", "first_name", "last_name", "Company", "City", "Country",
                "Phone1", "Phone2", "Email", "Subscription_Date", "Website"
        };

        setTitle("Cassandra");
        setSize(config.getWidth(), config.getHeight());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 1));

        JPanel inputPanel = new JPanel(new GridLayout(2, 1));

        fieldComboBox = new JComboBox<>(fields);
        fieldComboBox.addActionListener(e -> selectedField = (String) fieldComboBox.getSelectedItem());
        inputPanel.add(fieldComboBox);

        userInputField = new JTextField();
        userInputField.setBorder(BorderFactory.createTitledBorder("Please enter your value: [ SELECT * from customers WHERE <key> = <value>]"));
        inputPanel.add(userInputField);

        add(inputPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 5));

        JButton backButton = new JButton("Go Back to Main Menu");
        GUIUtils.paintButton(backButton);
        GUIUtils.addHover(backButton);
        backButton.addActionListener(e -> {
            new MainMenu(config, appState, cassandraConnector).setVisible(true);
            dispose();
        });

        JButton runQuery = new JButton("Run query");
        GUIUtils.paintButton(runQuery);
        GUIUtils.addHover(runQuery);
        runQuery.addActionListener(e -> {
            String selectedValue = userInputField.getText();
            System.out.println(selectedField);
            outputTextArea.setText("Running query: SELECT * FROM Customers WHERE " + selectedField + " = '" + selectedValue + "'");
            long startTime = System.nanoTime();
            String result = cassandraConnector.queryByKeyValue(selectedField, selectedValue);
            long endTime = System.nanoTime();
            long elapsedTime = endTime - startTime;
            double elapsedTimeInSeconds = elapsedTime / 1_000_000_000.0;
            String customersUI = "Time took to query in seconds: " + elapsedTimeInSeconds + " \n" + result;
            outputTextArea.setText(customersUI);
        });

        JButton insertCSVButton = new JButton("Insert CSV Data");
        GUIUtils.paintButton(insertCSVButton);
        GUIUtils.addHover(insertCSVButton);
        insertCSVButton.addActionListener(e -> {
            try {
                cassandraConnector.insertCSVData("./csvs");
                outputTextArea.setText("CSV data inserted successfully.");
            } catch (IOException ioException) {
                outputTextArea.setText("Error inserting CSV data: " + ioException.getMessage());
            }
        });

        JButton dropTableButton = new JButton("Drop Table");
        GUIUtils.paintButton(dropTableButton);
        GUIUtils.addHover(dropTableButton);
        dropTableButton.addActionListener(e -> {
            cassandraConnector.dropTable();
            outputTextArea.setText("Table dropped successfully.");
        });

        JButton createKeyspaceAndTableButton = new JButton("Create Keyspace and Table");
        GUIUtils.paintButton(createKeyspaceAndTableButton);
        GUIUtils.addHover(createKeyspaceAndTableButton);
        createKeyspaceAndTableButton.addActionListener(e -> {
            cassandraConnector.createKeySpace();
            cassandraConnector.createTable();
            outputTextArea.setText("Keyspace and Table created successfully.");
        });

        buttonPanel.add(backButton);
        buttonPanel.add(runQuery);
        buttonPanel.add(insertCSVButton);
        buttonPanel.add(dropTableButton);
        buttonPanel.add(createKeyspaceAndTableButton);

        add(buttonPanel);

        outputTextArea = new JTextArea();
        outputTextArea.setLineWrap(true);
        outputTextArea.setWrapStyleWord(true);
        outputTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(outputTextArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Output"));
        add(scrollPane);

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
