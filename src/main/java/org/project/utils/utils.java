package org.project.utils;

import org.project.customer.Customer;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class utils {
    public static String cleanFieldName(String field){
        field = field.toLowerCase();
        return field.replaceAll(" ", "");
    }

    public static boolean isFolderNotEmpty(String folderPath) {
        try {
            File folder = new File(folderPath);
            if (folder.exists() && folder.isDirectory()) {
                File[] files = folder.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isFile()) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e){
            return false;
        }
        return false;
    }

    public static void deleteFolderContents(String path) {
        File folder = new File(path);
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteFileOrFolder(file);
                }
            }
        }
    }

    private static void deleteFileOrFolder(File fileOrFolder) {
        if (fileOrFolder.isDirectory()) {
            File[] files = fileOrFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteFileOrFolder(file);
                }
            }
        }
        fileOrFolder.delete();
    }

    public static String getCustomersString(List<Customer> customers){
        String customersUI = "";
        if (customers == null || customers.isEmpty()){
            return customersUI;
        }
        for (Customer c : customers){
            customersUI += c;
            customersUI += "\n";
        }
        return customersUI;
    }

    public static int getNumberOfIndexFilesWritten(String indexDir) {
        File directory = new File(indexDir);
        return Objects.requireNonNull(directory.listFiles()).length;
    }
}
