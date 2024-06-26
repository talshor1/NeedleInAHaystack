package org.project.gui;

import org.project.appConfig.AppConfig;
import org.project.customer.Customer;
import org.project.manager.Manager;
import org.project.utils.utils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static org.project.utils.utils.isFolderNotEmpty;

public class GUI {

    private final JFrame frame;
    private final JComboBox<String> fieldComboBox;
    private final JTextField userInputField;
    private String selectedField;
    private final String[] fields = {
            "Customer Id", "First Name", "Last Name", "Company", "City", "Country",
            "Phone1", "Phone2", "Email", "Subscription Date", "Website"
    };

    public GUI(AppConfig config) {

        frame = new JFrame("Select Field and Run Query");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setLayout(new BorderLayout());

        fieldComboBox = new JComboBox<>(fields);
        frame.add(fieldComboBox, BorderLayout.NORTH);

        userInputField = new JTextField();
        frame.add(userInputField, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));

        JButton indexButton = new JButton("Create index");
        buttonPanel.add(indexButton);

        JButton queryButton = new JButton("Run query");
        buttonPanel.add(queryButton);

        JButton deleteIndexButton = new JButton("Delete Index");
        buttonPanel.add(deleteIndexButton);

        JButton closeButton = new JButton("Close");
        buttonPanel.add(closeButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        fieldComboBox.addActionListener(e -> {
            selectedField = (String) fieldComboBox.getSelectedItem();
        });

        closeButton.addActionListener(e -> {
            config.setStop();
        });

        indexButton.addActionListener(e -> {
            boolean isIndexInitialized = isFolderNotEmpty(config.getIndexFolderPath());
            if (isIndexInitialized) {
                JOptionPane.showMessageDialog(frame, "Index already initialized, first clean the current one");
                return;
            }
            selectedField = (String) fieldComboBox.getSelectedItem();
            Manager.createIndex(config.getNumberOfThreads(), selectedField, config.getFolderPath(), config.getNumberOfIndexFiles(), config.getIndexFolderPath());
        });

        queryButton.addActionListener(e -> {
            boolean isIndexInitialized = isFolderNotEmpty(config.getIndexFolderPath());
            String selectedValue = userInputField.getText();
            if (!isIndexInitialized){
                JOptionPane.showMessageDialog(frame, "Index is not initialized");
                return;
            }
            JOptionPane.showMessageDialog(frame, "Running query: " + "SELECT * FROM Customers WHERE " + selectedField + " = " + selectedValue);
            List<Customer> customers =  Manager.runIndex(selectedField, selectedValue, config.getIndexFolderPath());
            String customersUI = utils.getCustomersString(customers);
            JOptionPane.showMessageDialog(frame, customersUI);
        });

        deleteIndexButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Cleaning index dir");
            Manager.cleanIndexDirectory(config.getIndexFolderPath());
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
