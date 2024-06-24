package org.project.appConfig;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class AppConfig {
    private int numberOfThreads;
    private String inputFolderPath;
    private String indexFolderPath;
    private int numberOfIndexFiles;

    public AppConfig (String filePath) {
        try (InputStream inputStream = AppConfig.class.getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found: " + filePath);
            }

            Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8);
            String jsonText = scanner.useDelimiter("\\A").next();
            JSONObject jsonObject = new JSONObject(jsonText);

            this.setNumberOfThreads(jsonObject.getInt("numberOfThreads"));
            this.setInputFolderPath(jsonObject.getString("inputFolderPath"));
            this.setIndexFolderPath(jsonObject.getString("indexFolderPath"));
            this.setNumberOfIndexFiles(jsonObject.getInt("numberOfIndexFiles"));
        } catch (Exception e) {
            System.exit(1);
        }
    }

    public int getNumberOfThreads() {
        return numberOfThreads;
    }

    public void setNumberOfThreads(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    public String getFolderPath() {
        return inputFolderPath;
    }

    public void setInputFolderPath(String inputFolderPath) {
        this.inputFolderPath = inputFolderPath;
    }

    public int getNumberOfIndexFiles() {
        return numberOfIndexFiles;
    }

    public void setNumberOfIndexFiles(int numberOfIndexFiles) {
        this.numberOfIndexFiles = numberOfIndexFiles;
    }

    @Override
    public String toString() {
        return "AppConfig{" +
                "numberOfThreads=" + numberOfThreads +
                ", inputFolderPath='" + inputFolderPath + '\'' +
                ", numberOfIndexFiles=" + numberOfIndexFiles + '\'' +
                ", indexFolderPath=" + indexFolderPath +
                '}';
    }

    public String getIndexFolderPath() {
        return indexFolderPath;
    }

    public void setIndexFolderPath(String indexFolderPath) {
        this.indexFolderPath = indexFolderPath;
    }
}
