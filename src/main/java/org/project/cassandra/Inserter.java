package org.project.cassandra;

import com.datastax.driver.core.Session;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;

public class Inserter{

    /* This method inserts a csv dir to casandra DB */
    public static void insertCSVData(String directoryPath, Session session) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(directoryPath), "*.csv")) {
            for (Path entry : stream) {
                System.out.println("Working on file " + entry.toString());
                processCSVFile(entry, session);
            }
        }catch (Exception e){
            System.out.println(e);
            System.exit(1);
        }
    }

    private static void processCSVFile(Path csvFilePath, Session session) throws IOException {
        try (FileReader reader = new FileReader(csvFilePath.toFile());
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            for (CSVRecord record : csvParser) {
                String insertQuery = String.format(
                        "INSERT INTO project.customers (count, customer_id, first_name, last_name, company, city, country, phone1, phone2, email, subscription_date, website) " +
                                "VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                        Integer.parseInt(record.get("Count")),
                        record.get("Customer Id"),
                        record.get("First Name"),
                        record.get("Last Name"),
                        record.get("Company"),
                        record.get("City"),
                        record.get("Country"),
                        record.get("Phone 1"),
                        record.get("Phone 2"),
                        record.get("Email"),
                        LocalDate.parse(record.get("Subscription Date")),
                        record.get("Website")
                );
                session.execute(insertQuery);
            }
        }
    }
}
