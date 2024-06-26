package org.project.manager;

import org.project.customer.Customer;
import org.project.engine.Engine;
import org.project.files.Files;
import org.project.files.FilesDetails;
import org.project.index.Index;
import org.project.threadpool.ThreadPoolManager;
import org.project.utils.utils;

import java.util.List;
import java.util.Set;

import static org.project.utils.utils.deleteFolderContents;

public class Manager {
    public static void createIndexFile(int numberOfThreads, FilesDetails filesDetails, String fieldName, Index index){
        ThreadPoolManager threadPoolManager = new ThreadPoolManager(numberOfThreads, filesDetails,fieldName, index);
        threadPoolManager.start();
    }

    public static void cleanIndexDirectory(String path){
        System.out.println("start cleaning index dir");
        deleteFolderContents(path);
        System.out.println("done cleaning index dir");
    }

    public static void createIndex(int numberOfThreads, String selectedField, String filesFolderPath, int numberOfIndexFiles, String indexFolderPath){
        System.out.println("Creating index, selected field: " + selectedField);
        Index index = new Index();
        FilesDetails filesDetails = Files.getAllFilesInDirectory(filesFolderPath);
        Manager.createIndexFile(numberOfThreads, filesDetails, utils.cleanFieldName(selectedField), index);
        index.outputToMultipleFiles(indexFolderPath, numberOfIndexFiles);
        index.createMetadataFile(indexFolderPath, numberOfIndexFiles);
        System.out.println("Done creating index");
    }

    public static List<Customer> runIndex(String selectedField, String selectedValue, String indexFolderPath){
        List<String> relevantIndexFiles = Engine.getRelevantFiles(selectedValue, indexFolderPath+ "/metadata.json");
        if (relevantIndexFiles.isEmpty()){
            return null;
        }
        Set<String> relevantCSVFiles = Engine.getRelevantFiles(relevantIndexFiles, indexFolderPath, selectedValue);
        System.out.println("Relevat csv files:" + relevantCSVFiles);
        List<Customer> customers = Engine.runQuery(relevantCSVFiles, selectedField, selectedValue);
        System.out.println("Relevat costumers:" + customers);
        return customers;
    }
}
