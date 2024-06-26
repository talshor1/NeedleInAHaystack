package org.project.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CassandraConnector {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private Cluster cluster;
    private Session session;

    public CassandraConnector() {
        try {
            System.out.println("Trying to connect");
            this.cluster = Cluster.builder().addContactPoint("localhost").build();
            this.session = cluster.connect();
            System.out.println("Connected");
        } catch (Exception e){
            System.out.println("Failed connecting... " +  e);
        }
    }

    public Session getSession(){
        return this.session;
    }

    public void createKeySpace(){
        String createKeyspaceQuery = "CREATE KEYSPACE IF NOT EXISTS my_keyspace " +
                "WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 3};";
        session.execute(createKeyspaceQuery);
    }

    public void createTable() {
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append("project.").append("customers").append(" (")
                .append("count int PRIMARY KEY, ")
                .append("customer_id text, ")
                .append("first_name text, ")
                .append("last_name text, ")
                .append("company text, ")
                .append("city text, ")
                .append("country text, ")
                .append("phone1 text, ")
                .append("phone2 text, ")
                .append("email text, ")
                .append("subscription_date date, ")
                .append("website text);");

        String createTableQuery = sb.toString();
        session.execute(createTableQuery);
    }

    public void dropTable(){
        String dropTableQuery = "DROP TABLE IF EXISTS project.customers;";
        session.execute(dropTableQuery);
    }

    public void showAmountOfRecords(){
        String showAmount = "SELECT COUNT(*) FROM project.customers";
        ResultSet result = session.execute(showAmount);
        Row row = result.one();
        if (row != null) {
            long count = row.getLong(0);
            System.out.println("Total number of records: " + count);
        } else {
            System.out.println("No records found.");
        }
    }

    public void insertCSVData(String directoryPath) throws IOException {
        Session session = this.getSession();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(directoryPath), "*.csv")) {
            for (Path entry : stream) {
                processCSVFile(entry, session);
            }
        }
    }

    private static void processCSVFile(Path csvFilePath, Session session) throws IOException {
        try (FileReader reader = new FileReader(csvFilePath.toFile());
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            for (CSVRecord record : csvParser) {
                String insertQuery = String.format(
                        "INSERT INTO project.customers (count, customer_id, first_name, last_name, company, city, country, phone1, phone2, email, subscription_date, website) " +
                                "VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                        Integer.parseInt(record.get("Index")),
                        record.get("Customer Id"),
                        record.get("First Name"),
                        record.get("Last Name"),
                        record.get("Company"),
                        record.get("City"),
                        record.get("Country"),
                        record.get("Phone 1"),
                        record.get("Phone 2"),
                        record.get("Email"),
                        LocalDate.parse(record.get("Subscription Date"), DATE_FORMATTER),
                        record.get("Website")
                );
                try {
                    session.execute(insertQuery);
                }catch (Exception e){
                    System.out.println(e);
                }
            }
        }
    }

    public String queryByKeyValue(String key, String value) {
        try {
            String query = String.format("SELECT * FROM project.customers WHERE %s = '%s' ALLOW FILTERING;", key, value);
            System.out.println(query);
            ResultSet result = session.execute(query);

            StringBuilder resultString = new StringBuilder();
            for (Row row : result) {
                resultString.append(row.toString()).append("\n");
            }
            return resultString.toString();
        } catch (Exception e){
            return "Got exception " + e;
        }
    }

}
