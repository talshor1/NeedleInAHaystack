package org.project.csv;

import java.io.*;
import java.util.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.project.costumer.Customer;

public class CSVParser {
    private final List<String> fileNames;
    private final BlockingQueue<Customer> blockingQueue;
    private final int parserNumber;

    public CSVParser(List<String> fileNames, BlockingQueue<Customer> blockingQueue, int parserNumber) {
        this.fileNames = fileNames;
        this.blockingQueue = blockingQueue;
        this.parserNumber = parserNumber;
    }

    public void run() {
        for (String fileName : fileNames) {
            try (CSVReader reader = new CSVReader(new FileReader(fileName, StandardCharsets.UTF_8))) {
                String[] data;
                reader.readNext(); /* skip first line */
                while ((data = reader.readNext()) != null) {
                    Customer customer = new Customer(
                            fileName,
                            data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8], data[9], data[10], data[11]
                    );
                    blockingQueue.add(customer);
                }
            } catch (IOException | CsvValidationException e) {
                System.out.println("Exception in parser....");
                System.exit(1);
            }
        }
        System.out.println("Done " + parserNumber);
    }
}
