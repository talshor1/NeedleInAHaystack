package org.project.gui;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.BlockingQueue;

public class GUI {

    private final JFrame frame;
    private final JComboBox<String> fieldComboBox;
    private final JTextField userInputField;
    private String selectedField;
    private String userInput;

    public GUI(BlockingQueue<String> selectionQueue) {

        frame = new JFrame("Select Field and Run Query");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new BorderLayout());

        String[] fields = {
                "Customer Id", "First Name", "Last Name", "Company", "City", "Country",
                "Phone1", "Phone2", "Email", "Subscription Date", "Website"
        };
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

        frame.add(buttonPanel, BorderLayout.SOUTH);

        fieldComboBox.addActionListener(e -> {
            selectedField = (String) fieldComboBox.getSelectedItem();
        });

        userInputField.addActionListener(e -> {
            userInput = userInputField.getText();
        });

        indexButton.addActionListener(e -> {
            selectedField = (String) fieldComboBox.getSelectedItem();
            JOptionPane.showMessageDialog(frame, "You selected: " + selectedField);
            try {
                selectionQueue.put(selectedField);
            } catch (InterruptedException ex) {
                System.out.println("Exception in gui....");
                System.exit(1);
            }
        });

        queryButton.addActionListener(e -> {
            userInput = userInputField.getText();
            try {
                selectionQueue.put("QUERY"+userInput);
            } catch (InterruptedException ex) {
                System.exit(1);
            }
        });

        deleteIndexButton.addActionListener(e -> {
            try {
                selectionQueue.put("CLEANTHEINDEXES");
            } catch (InterruptedException ex) {
                System.exit(1);
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
