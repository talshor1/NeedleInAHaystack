package org.project.manager;

import org.project.customer.Customer;
import org.project.engine.Engine;
import org.project.files.FilesDetails;
import org.project.index.Index;
import org.project.state.AppState;
import org.project.threadpool.ThreadPoolManager;
import org.project.utils.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.project.files.Files.getAllFilesInDirectory;
import static org.project.utils.utils.deleteFolderContents;

public class Manager {
    public static void createIndexFile(int numberOfThreads, FilesDetails filesDetails, String fieldName, Index index, AppState appState){
        ThreadPoolManager threadPoolManager = new ThreadPoolManager(numberOfThreads, filesDetails,fieldName, index);
        threadPoolManager.start();
    }

    public static void cleanIndexDirectory(String path){
        deleteFolderContents(path);
    }

    public static void createIndex(int numberOfThreads, String selectedField, String filesFolderPath, int numberOfIndexFiles, String indexFolderPath, AppState appState){
        Index index = new Index();
        FilesDetails filesDetails = getAllFilesInDirectory(filesFolderPath);
        Manager.createIndexFile(numberOfThreads, filesDetails, utils.cleanFieldName(selectedField), index, appState);
        index.outputToMultipleFiles(indexFolderPath, numberOfIndexFiles);
        int numberOfIndexFilesCreated = utils.getNumberOfIndexFilesWritten(indexFolderPath);
        index.createMetadataFile(indexFolderPath, numberOfIndexFilesCreated);
    }

    public static List<Customer> runIndex(String selectedField, String selectedValue, String indexFolderPath){
        List<String> relevantIndexFiles = Engine.getRelevantIndexFiles(selectedValue, indexFolderPath+ "/metadata.json");
        if (relevantIndexFiles.isEmpty()){
            System.out.println("No relevant index files");
            return null;
        }
        Set<String> relevantCSVFiles = Engine.getRelevantFiles(relevantIndexFiles, indexFolderPath, selectedValue);
        if (relevantCSVFiles.isEmpty()){
            System.out.println("No relevant csv files");
            return null;
        }
        return Engine.runQuery(relevantCSVFiles, selectedField, selectedValue);
    }

    public static List<Customer> runWithoutIndex(String selectedField, String selectedValue, String csvFolderPath) {
        FilesDetails relevantCSVFiles = getAllFilesInDirectory(csvFolderPath);
        Set<String> set = new HashSet<>(relevantCSVFiles.getFilesList());
        return Engine.runQuery(set, selectedField, selectedValue);
    }
}
