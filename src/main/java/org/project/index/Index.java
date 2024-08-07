package org.project.index;

import org.project.state.AppState;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import javax.json.*;
import java.io.FileWriter;

public class Index {
    private Map<String, List<String>> indexMap;

    public Index() {
        this.indexMap = new HashMap<>();
    }

    public synchronized void notify(String value, String fileName) {
        indexMap.computeIfAbsent(value, k -> new ArrayList<>()).add(fileName);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : indexMap.entrySet()) {
            sb.append(entry.getKey()).append(" -> ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }

    private void sort(){
        this.indexMap = new TreeMap<>(indexMap);
    }

    public void outputToMultipleFiles(String directoryPath, int numFiles) {
        sort();
        int entriesPerFile = (int) Math.ceil((double) indexMap.size() / numFiles);
        Iterator<Map.Entry<String, List<String>>> iterator = indexMap.entrySet().iterator();

        for (int i = 0; i < numFiles; i++) {
            if (!iterator.hasNext()) break;

            JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
            String minValue = null, maxValue = null;

            for (int j = 0; j < entriesPerFile && iterator.hasNext(); j++) {
                Map.Entry<String, List<String>> entry = iterator.next();
                if (j == 0) {
                    minValue = entry.getKey();
                }
                maxValue = entry.getKey();

                JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
                for (String fileName : entry.getValue()) {
                    arrayBuilder.add(fileName);
                }
                jsonBuilder.add(entry.getKey(), arrayBuilder.build());
            }


            JsonObjectBuilder finalJsonBuilder = Json.createObjectBuilder()
                    .add("minValue", minValue)
                    .add("maxValue", maxValue);


            jsonBuilder.build().forEach(finalJsonBuilder::add);

            JsonObject jsonObject = finalJsonBuilder.build();
            String filePath = String.format("%s/index_%02d.json", directoryPath, i);


            try (JsonWriter jsonWriter = Json.createWriter(new FileWriter(filePath))) {
                jsonWriter.writeObject(jsonObject);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(1);
            }
        }
    }

    public void createMetadataFile(String directoryPath, int numFiles) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        for (int i = 0; i < numFiles; i++) {
            String filePath = String.format("%s/index_%02d.json", directoryPath, i);

            try (JsonReader jsonReader = Json.createReader(new FileReader(filePath))) {
                JsonObject jsonObject = jsonReader.readObject();
                String minValue = jsonObject.getString("minValue");
                String maxValue = jsonObject.getString("maxValue");

                JsonObjectBuilder jsonBuilder = Json.createObjectBuilder()
                        .add("filename", String.format("index_%02d.json", i))
                        .add("minValue", minValue)
                        .add("maxValue", maxValue);
                arrayBuilder.add(jsonBuilder.build());
            } catch (IOException e) {
                System.out.println("Exception in createMetadataFile 1");
                System.exit(1);
            }
        }

        JsonObject metadata = Json.createObjectBuilder()
                .add("files", arrayBuilder.build())
                .build();

        try (JsonWriter jsonWriter = Json.createWriter(new FileWriter(directoryPath + "/metadata.json"))) {
            jsonWriter.writeObject(metadata);
        } catch (IOException e) {
            System.out.println("Exception in createMetadataFile 2");
            System.exit(1);
        }
    }
}
