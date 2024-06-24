package org.project.manager;

import org.project.files.Files;
import org.project.files.FilesDetails;
import org.project.index.Index;
import org.project.threadpool.ThreadPoolManager;

public class Manager {
    private final FilesDetails filesDetails;
    private final String fieldName;
    private final int numberOfThreads;
    private final Index index;

    public Manager(String FolderPath, String fieldName, int numberOfThreads, Index index ){
        this.filesDetails = Files.getAllFilesInDirectory(FolderPath);
        this.fieldName = fieldName;
        this.numberOfThreads = numberOfThreads;
        this.index = index;
    }

    public void createIndexFile(){
        ThreadPoolManager threadPoolManager = new ThreadPoolManager(numberOfThreads, filesDetails, this.fieldName, index);
        threadPoolManager.start();
    }
}
