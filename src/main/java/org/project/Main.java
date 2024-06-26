package org.project;

import org.project.appConfig.AppConfig;
import org.project.gui.GUI;

public class Main {
    public static void main(String[] args) {
        AppConfig config = null;

        try {
            config = new AppConfig("config.json");
            System.out.println(config);
        } catch (Exception e){
            System.exit(1);
        }

        new GUI(config);
        while(!config.shouldStop()){}

        System.exit(0);
    }
}