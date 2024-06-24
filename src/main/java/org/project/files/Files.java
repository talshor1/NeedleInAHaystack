package org.project.files;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Files {
    /**
     * Returns a list of all files in the specified directory.
     * @param directoryPath The path to the directory.
     * @return A list of files in the directory.
     */
    public static FilesDetails getAllFilesInDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        List<String> fileList = new ArrayList<>();

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        fileList.add(file.getPath());
                    }
                }
            }
        } else {
            System.out.println("The provided path is either not a directory or does not exist.");
        }

        return new FilesDetails(fileList);
    }
}
