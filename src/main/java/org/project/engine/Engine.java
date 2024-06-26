package org.project.engine;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import org.json.JSONArray;
import org.json.JSONObject;
import org.project.customer.Customer;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Engine {
    public static List<String> getRelevantIndexFiles(String fieldValue, String metadataFilePath) {
        List<String> relevantFiles  = new ArrayList<>();
        try (JsonReader jsonReader = Json.createReader(new FileReader(metadataFilePath))) {
            JsonObject metadata = jsonReader.readObject();
            JsonArray filesArray = metadata.getJsonArray("files");

            for (int i = 0; i < filesArray.size(); i++) {
                JsonObject fileObject = filesArray.getJsonObject(i);
                String fileName = fileObject.getString("filename");
                String minValue = fileObject.getString("minValue");
                String maxValue = fileObject.getString("maxValue");

                if (fieldValue.compareTo(minValue) >= 0 && fieldValue.compareTo(maxValue) <= 0) {
                    relevantFiles.add(fileName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return relevantFiles;
    }

    public static Set<String> getRelevantFiles(List<String> relevantIndexFiles, String indexesFilesDirectory, String fieldValue) {
        Set<String> relevantFiles = new HashSet<>();

        for (String indexFileName : relevantIndexFiles) {
            Path indexFilePath = Paths.get(indexesFilesDirectory, indexFileName);
            try (BufferedReader reader = Files.newBufferedReader(indexFilePath)) {
                StringBuilder jsonBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
                String jsonString = jsonBuilder.toString();
                JSONObject jsonObject = new JSONObject(jsonString);

                for (String key : jsonObject.keySet()) {
                    if (key.equals(fieldValue)) {
                        JSONArray filesArray = jsonObject.getJSONArray(key);
                        for (int i = 0; i < filesArray.length(); i++) {
                            relevantFiles.add(filesArray.getString(i));
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return relevantFiles;
    }

    public static List<Customer> runQuery(Set<String> relevantFiles, String fieldKey, String fieldValue) {
        List<Customer> matchingCustomers = new ArrayList<>();

        for (String filePath : relevantFiles) {
            try (CSVParser csvParser = new CSVParser(new FileReader(filePath), CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
                for (CSVRecord record : csvParser) {
                    if (record.isMapped(fieldKey) && record.get(fieldKey).equals(fieldValue)) {
                        Customer customer = new Customer(
                                filePath,
                                record.get("Customer Id"),
                                record.get("First Name"),
                                record.get("Last Name"),
                                record.get("Company"),
                                record.get("City"),
                                record.get("Country"),
                                record.get("Phone 1"),
                                record.get("Phone 2"),
                                record.get("Email"),
                                record.get("Subscription Date"),
                                record.get("Website")
                        );
                        matchingCustomers.add(customer);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return matchingCustomers;
    }
}
