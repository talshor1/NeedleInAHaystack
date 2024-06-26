package org.project.threadpool;

import java.util.List;
import java.util.concurrent.*;

import org.project.consumer.Consumer;
import org.project.customer.Customer;
import org.project.csv.CSVParser;
import org.project.files.FilesDetails;
import org.project.index.Index;

public class ThreadPoolManager {
    private final int numberOfThreads;
    private final FilesDetails filesDetails;
    private final ExecutorService executorService;
    private final BlockingQueue<Customer> blockingQueue;
    private final String fieldName;
    private final Index index;

    public ThreadPoolManager(int numberOfThreads, FilesDetails filesDetails, String fieldName, Index index) {
        this.numberOfThreads = numberOfThreads;
        this.filesDetails = filesDetails;
        this.executorService = Executors.newFixedThreadPool(numberOfThreads);
        this.blockingQueue = new LinkedBlockingDeque<>();
        this.fieldName = fieldName;
        this.index = index;
    }

    public void start() {
        int filesPerThread = filesDetails.getFilesList().size() / numberOfThreads;
        int remainingFiles = filesDetails.getFilesList().size() % numberOfThreads;
        int parserNumber = 1;

        int startIndex = 0;
        for (int i = 0; i < numberOfThreads; i++) {
            int endIndex = startIndex + filesPerThread;
            List<String> filesForThread = filesDetails.getFilesList().subList(startIndex, endIndex);
            CSVParser parser = new CSVParser(filesForThread, blockingQueue, parserNumber++);
            executorService.submit(parser::run);
            startIndex = endIndex;
        }

        if (remainingFiles > 0) {
            System.out.println("Remaining files: " + remainingFiles);
            int endIndex = startIndex + remainingFiles;
            List<String> filesForThread = filesDetails.getFilesList().subList(startIndex, endIndex);
            CSVParser parser = new CSVParser(filesForThread, blockingQueue, parserNumber);
            executorService.submit(parser::run);
        } else {
            System.out.println("No remaining files");
        }

        Consumer consumer = new Consumer(blockingQueue, fieldName, index);
        executorService.submit(consumer::run);
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException ie) {
            System.out.println("Threads did not finish after 1 minute, exiting....");
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
