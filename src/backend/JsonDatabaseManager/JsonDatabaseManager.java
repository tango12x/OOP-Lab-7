package backend.JsonDatabaseManager;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class JsonDatabaseManager {
    private String filePath;

    public JsonDatabaseManager(String filePath) {
        this.filePath = filePath;
        createFileIfNotExists();
    }

    // Create file with empty array if it doesn't exist
    private void createFileIfNotExists() {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                Files.write(Paths.get(filePath), "[]".getBytes());
            }
        } catch (IOException e) {
            System.err.println("Error creating file: " + e.getMessage());
        }
    }

    // Read entire JSON file as string
    public String readFile() {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return "[]";
        }
    }

    // Write string to JSON file
    public void writeFile(String content) {
        try {
            Files.write(Paths.get(filePath), content.getBytes());
        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
        }
    }

    // Parse JSON array into List of Maps
    public List<Map<String, String>> readAll() {
        String json = readFile().trim();
        if (json.equals("[]") || json.isEmpty()) {
            return new ArrayList<>();
        }

        List<Map<String, String>> records = new ArrayList<>();
        
        // Remove outer brackets
        json = json.substring(1, json.length() - 1).trim();
        
        if (json.isEmpty()) {
            return records;
        }

        // Split by objects (find { } pairs)
        List<String> objects = splitJsonObjects(json);
        
        for (String obj : objects) {
            Map<String, String> record = parseJsonObject(obj);
            if (record != null) {
                records.add(record);
            }
        }

        return records;
    }

    // Split JSON array into individual object strings
    private List<String> splitJsonObjects(String json) {
        List<String> objects = new ArrayList<>();
        int braceCount = 0;
        int start = -1;

        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            
            if (c == '{') {
                if (braceCount == 0) {
                    start = i;
                }
                braceCount++;
            } else if (c == '}') {
                braceCount--;
                if (braceCount == 0 && start != -1) {
                    objects.add(json.substring(start, i + 1));
                    start = -1;
                }
            }
        }

        return objects;
    }

    // Parse a single JSON object into a Map
    private Map<String, String> parseJsonObject(String json) {
        Map<String, String> map = new LinkedHashMap<>();
        
        // Remove braces
        json = json.trim();
        if (json.startsWith("{")) json = json.substring(1);
        if (json.endsWith("}")) json = json.substring(0, json.length() - 1);
        json = json.trim();

        if (json.isEmpty()) {
            return map;
        }

        // Split by comma (but not inside strings)
        List<String> pairs = splitByComma(json);

        for (String pair : pairs) {
            int colonIndex = pair.indexOf(':');
            if (colonIndex > 0) {
                String key = pair.substring(0, colonIndex).trim();
                String value = pair.substring(colonIndex + 1).trim();

                // Remove quotes
                key = removeQuotes(key);
                value = removeQuotes(value);

                map.put(key, value);
            }
        }

        return map;
    }

    // Split by comma, but ignore commas inside quotes
    private List<String> splitByComma(String str) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        boolean escaped = false;

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            if (escaped) {
                current.append(c);
                escaped = false;
                continue;
            }

            if (c == '\\') {
                escaped = true;
                current.append(c);
                continue;
            }

            if (c == '"') {
                inQuotes = !inQuotes;
                current.append(c);
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString().trim());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }

        if (current.length() > 0) {
            result.add(current.toString().trim());
        }

        return result;
    }

    // Remove surrounding quotes from a string
    private String removeQuotes(String str) {
        str = str.trim();
        if (str.startsWith("\"") && str.endsWith("\"")) {
            return str.substring(1, str.length() - 1);
        }
        return str;
    }

    // Convert Map to JSON object string
    private String mapToJson(Map<String, String> map) {
        StringBuilder sb = new StringBuilder("{");
        int count = 0;
        
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (count > 0) sb.append(",");
            sb.append("\"").append(escapeJson(entry.getKey())).append("\":");
            sb.append("\"").append(escapeJson(entry.getValue())).append("\"");
            count++;
        }
        
        sb.append("}");
        return sb.toString();
    }

    // Escape special characters for JSON
    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }

    // Convert List of Maps to JSON array string
    private String listToJson(List<Map<String, String>> list) {
        StringBuilder sb = new StringBuilder("[");
        
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(mapToJson(list.get(i)));
        }
        
        sb.append("]");
        return sb.toString();
    }

    // CREATE: Add a new record
    public void insert(Map<String, String> record) {
        List<Map<String, String>> records = readAll();
        records.add(record);
        writeFile(listToJson(records));
        System.out.println("Record inserted successfully.");
    }

    // READ: Get all records
    public List<Map<String, String>> selectAll() {
        return readAll();
    }

    // READ: Find records by key-value pair
    public List<Map<String, String>> selectWhere(String key, String value) {
        List<Map<String, String>> allRecords = readAll();
        List<Map<String, String>> result = new ArrayList<>();

        for (Map<String, String> record : allRecords) {
            if (value.equals(record.get(key))) {
                result.add(record);
            }
        }

        return result;
    }

    // READ: Get record by index
    public Map<String, String> selectByIndex(int index) {
        List<Map<String, String>> records = readAll();
        if (index >= 0 && index < records.size()) {
            return records.get(index);
        }
        return null;
    }

    // UPDATE: Update record by index
    public void updateByIndex(int index, Map<String, String> newRecord) {
        List<Map<String, String>> records = readAll();
        
        if (index >= 0 && index < records.size()) {
            records.set(index, newRecord);
            writeFile(listToJson(records));
            System.out.println("Record updated successfully.");
        } else {
            System.out.println("Index out of bounds.");
        }
    }

    // UPDATE: Update records where key=value
    public void updateWhere(String key, String value, Map<String, String> newData) {
        List<Map<String, String>> records = readAll();
        boolean updated = false;

        for (int i = 0; i < records.size(); i++) {
            if (value.equals(records.get(i).get(key))) {
                // Merge new data with existing record
                Map<String, String> record = records.get(i);
                record.putAll(newData);
                updated = true;
            }
        }

        if (updated) {
            writeFile(listToJson(records));
            System.out.println("Records updated successfully.");
        } else {
            System.out.println("No matching records found.");
        }
    }

    // DELETE: Delete record by index
    public void deleteByIndex(int index) {
        List<Map<String, String>> records = readAll();
        
        if (index >= 0 && index < records.size()) {
            records.remove(index);
            writeFile(listToJson(records));
            System.out.println("Record deleted successfully.");
        } else {
            System.out.println("Index out of bounds.");
        }
    }

    // DELETE: Delete records where key=value
    public void deleteWhere(String key, String value) {
        List<Map<String, String>> records = readAll();
        List<Map<String, String>> filtered = new ArrayList<>();

        for (Map<String, String> record : records) {
            if (!value.equals(record.get(key))) {
                filtered.add(record);
            }
        }

        int deleted = records.size() - filtered.size();
        writeFile(listToJson(filtered));
        System.out.println(deleted + " record(s) deleted.");
    }

    // DELETE: Clear all records
    public void deleteAll() {
        writeFile("[]");
        System.out.println("All records deleted.");
    }

    // COUNT: Get total number of records
    public int count() {
        return readAll().size();
    }

    // Display all records
    public void displayAll() {
        List<Map<String, String>> records = readAll();
        
        if (records.isEmpty()) {
            System.out.println("No records found.");
            return;
        }

        System.out.println("\n=== All Records ===");
        for (int i = 0; i < records.size(); i++) {
            System.out.println("Record #" + i + ":");
            for (Map.Entry<String, String> entry : records.get(i).entrySet()) {
                System.out.println("  " + entry.getKey() + ": " + entry.getValue());
            }
            System.out.println();
        }
    }
















        public static void main(String[] args) {
        // Initialize database
        JsonDatabaseManager db = new JsonDatabaseManager("data/courses.json");

        System.out.println("=== JSON Database Demo ===\n");

        // CREATE - Insert records
        System.out.println("1. Inserting records...");
        
        Map<String, String> course1 = new HashMap<>();
        course1.put("id", "CS101");
        course1.put("name", "Introduction to Programming");
        course1.put("instructor", "Dr. Smith");
        course1.put("credits", "3");
        db.insert(course1);

        Map<String, String> course2 = new HashMap<>();
        course2.put("id", "CS202");
        course2.put("name", "Data Structures");
        course2.put("instructor", "Dr. Johnson");
        course2.put("credits", "4");
        db.insert(course2);

        Map<String, String> course3 = new HashMap<>();
        course3.put("id", "CS303");
        course3.put("name", "Algorithms");
        course3.put("instructor", "Dr. Smith");
        course3.put("credits", "4");
        db.insert(course3);

        // READ - Display all records
        System.out.println("\n2. Reading all records:");
        db.displayAll();

        // READ - Select by condition
        System.out.println("3. Finding courses by instructor 'Dr. Smith':");
        List<Map<String, String>> smithCourses = db.selectWhere("instructor", "Dr. Smith");
        for (Map<String, String> course : smithCourses) {
            System.out.println("  - " + course.get("name") + " (" + course.get("id") + ")");
        }

        // READ - Select by index
        System.out.println("\n4. Getting record at index 1:");
        Map<String, String> record = db.selectByIndex(1);
        if (record != null) {
            System.out.println("  Course: " + record.get("name"));
            System.out.println("  Instructor: " + record.get("instructor"));
        }

        // COUNT
        System.out.println("\n5. Total number of courses: " + db.count());

        // UPDATE - Update by index
        System.out.println("\n6. Updating record at index 0...");
        Map<String, String> updatedCourse = new HashMap<>();
        updatedCourse.put("id", "CS101");
        updatedCourse.put("name", "Introduction to Programming");
        updatedCourse.put("instructor", "Dr. Williams");
        updatedCourse.put("credits", "3");
        db.updateByIndex(0, updatedCourse);

        // UPDATE - Update where condition matches
        System.out.println("\n7. Updating credits for courses taught by Dr. Smith...");
        Map<String, String> updateData = new HashMap<>();
        updateData.put("credits", "5");
        db.updateWhere("instructor", "Dr. Smith", updateData);

        // Display after updates
        System.out.println("\n8. Records after updates:");
        db.displayAll();

        // DELETE - Delete by index
        System.out.println("9. Deleting record at index 1...");
        db.deleteByIndex(1);

        // Display after deletion
        System.out.println("\n10. Records after deletion:");
        db.displayAll();

        // DELETE - Delete where condition matches
        System.out.println("11. Deleting courses with 5 credits...");
        db.deleteWhere("credits", "5");

        // Final state
        System.out.println("\n12. Final state:");
        db.displayAll();
        System.out.println("Total courses remaining: " + db.count());

        // Optional: Clear all
        // System.out.println("\n13. Clearing all records...");
        // db.deleteAll();
        // db.displayAll();
    }
}