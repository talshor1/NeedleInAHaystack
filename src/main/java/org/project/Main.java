package org.project;

import org.checkerframework.checker.units.qual.C;
import org.project.appConfig.AppConfig;
import org.project.cassandra.CassandraConnector;
import org.project.gui.MainMenu;
import org.project.state.AppState;

import java.io.IOException;

import static org.project.simulation.Simulation.runSimulation;


public class Main {
    public static void main(String[] args) throws IOException {
        AppConfig config = null;
        AppState appState = new AppState();
        try {
            config = new AppConfig("config.json");
        } catch (Exception e) {
            System.exit(1);
        }

        CassandraConnector cassandraConnector = new CassandraConnector();

        new MainMenu(config, appState, cassandraConnector);

        while (appState.shouldRun()) {}
    }
}