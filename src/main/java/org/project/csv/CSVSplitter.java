package org.project.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/* Split a given csv file to files each contains 100 records */
public class CSVSplitter {
    public static void splitCSV(String inputFilePath, String outputDirPath, int recordsPerFile) throws IOException {
        System.out.println("Starting " + inputFilePath + " " + outputDirPath + " " + recordsPerFile);
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(inputFilePath));
         CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
            List<CSVRecord> records = csvParser.getRecords();
            int totalRecords = records.size();
            int fileIndex = 1;

            for (int i = 0; i < totalRecords; i += recordsPerFile) {
                int end = Math.min(i + recordsPerFile, totalRecords);
                List<CSVRecord> chunk = records.subList(i, end);

                String outputFilePath = Paths.get(outputDirPath, fileIndex + "b.csv").toString();
                writeChunkToFile(chunk, outputFilePath, csvParser.getHeaderNames());
                fileIndex++;
            }
        }
        System.out.println("Done");
    }

    private static void writeChunkToFile(List<CSVRecord> chunk, String outputFilePath, List<String> headers) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFilePath));
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(headers.toArray(new String[0])))) {

            for (CSVRecord record : chunk) {
                csvPrinter.printRecord(record);
            }
        }
    }
}
