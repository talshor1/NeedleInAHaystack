package org.project.gui;

import org.project.appConfig.AppConfig;
import org.project.cassandra.CassandraConnector;
import org.project.state.AppState;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {

    public MainMenu(AppConfig config, AppState appState, CassandraConnector cassandraConnector) {
        setTitle("Main Menu");
        setSize(config.getWidth(), config.getHeight());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton cassandraButton = new JButton("Cassandra");
        GUIUtils.addHover(cassandraButton);
        GUIUtils.paintButton(cassandraButton);

        JButton nonCassandraButton = new JButton("Non-Cassandra");
        GUIUtils.addHover(nonCassandraButton);
        GUIUtils.paintButton(nonCassandraButton);

        JButton splitCSVButton = new JButton("Split CSV");
        GUIUtils.addHover(splitCSVButton);
        GUIUtils.paintButton(splitCSVButton);

        JButton closeButton = new JButton("Close");
        GUIUtils.addHover(closeButton);
        GUIUtils.paintButton(closeButton);

        cassandraButton.addActionListener(e -> {
            new CassandraMenu(config, appState, cassandraConnector).setVisible(true);
            dispose();
        });

        nonCassandraButton.addActionListener(e -> {
            new IndexMenu(config, appState, cassandraConnector).setVisible(true);
            dispose();
        });

        splitCSVButton.addActionListener(e -> {
            new SplitCSVMenu(config, appState, cassandraConnector).setVisible(true);
            dispose();
        });

        closeButton.addActionListener(e -> {
            appState.stop();
            System.exit(0);
        });

        setLayout(new GridLayout(4, 1));
        add(cassandraButton);
        add(nonCassandraButton);
        add(splitCSVButton);
        add(closeButton);

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
