package org.project.gui;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.BlockingQueue;

public class GUI {

    private final JFrame frame;
    private final JComboBox<String> fieldComboBox;
    private String selectedField;

    public GUI(BlockingQueue<String> selectionQueue) {

        frame = new JFrame("Select Field");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new BorderLayout());

        String[] fields = {
                "Customer Id", "First Name", "Last Name", "Company", "City", "Country",
                "Phone1", "Phone2", "Email", "Subscription Date", "Website"
        };
        fieldComboBox = new JComboBox<>(fields);
        frame.add(fieldComboBox, BorderLayout.CENTER);

        JButton submitButton = new JButton("Submit");
        frame.add(submitButton, BorderLayout.SOUTH);

        submitButton.addActionListener(e -> {
            selectedField = (String) fieldComboBox.getSelectedItem();
            JOptionPane.showMessageDialog(frame, "You selected: " + selectedField);
            try {
                selectionQueue.put(selectedField);
            } catch (InterruptedException ex) {
                System.out.println("Exception in gui....");
                System.exit(1);
            }
            frame.dispose();
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
