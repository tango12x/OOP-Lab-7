package backend.JsonDatabaseManager;

import java.util.ArrayList;

public class JsonManipulation {
    
    // Inner class to represent a JSON object (like a record/row)
    public static class JsonObject {
        private ArrayList<String> keys;
        private ArrayList<String> values;
        
        public JsonObject() {
            keys = new ArrayList<>();
            values = new ArrayList<>();
        }
        
        // Add a field to this object
        public void addField(String key, String value) {
            keys.add(key);
            values.add(value);
        }
        
        // Get value by key
        public String getValue(String key) {
            for (int i = 0; i < keys.size(); i++) {
                if (keys.get(i).equals(key)) {
                    return values.get(i);
                }
            }
            return null;
        }
        
        // Update value by key
        public void setValue(String key, String value) {
            for (int i = 0; i < keys.size(); i++) {
                if (keys.get(i).equals(key)) {
                    values.set(i, value);
                    return;
                }
            }
            // If key doesn't exist, add it
            addField(key, value);
        }
        
        // Get all keys
        public ArrayList<String> getKeys() {
            return keys;
        }
        
        // Get all values
        public ArrayList<String> getValues() {
            return values;
        }
        
        // Get number of fields
        public int getFieldCount() {
            return keys.size();
        }
        
        // Convert this object to JSON string
        public String toJsonString() {
            StringBuilder sb = new StringBuilder("{");
            
            for (int i = 0; i < keys.size(); i++) {
                if (i > 0) {
                    sb.append(",");
                }
                sb.append("\"").append(escapeString(keys.get(i))).append("\":");
                sb.append("\"").append(escapeString(values.get(i))).append("\"");
            }
            
            sb.append("}");
            return sb.toString();
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < keys.size(); i++) {
                sb.append(keys.get(i)).append(": ").append(values.get(i)).append("\n");
            }
            return sb.toString();
        }
    }
    
    // Parse JSON array string into ArrayList of JsonObjects
    public static ArrayList<JsonObject> parseJsonArray(String jsonText) {
        ArrayList<JsonObject> objects = new ArrayList<>();
        
        jsonText = jsonText.trim();
        
        // Check if empty array
        if (jsonText.equals("[]") || jsonText.isEmpty()) {
            return objects;
        }
        
        // Remove outer brackets [ ]
        if (jsonText.startsWith("[")) {
            jsonText = jsonText.substring(1);
        }
        if (jsonText.endsWith("]")) {
            jsonText = jsonText.substring(0, jsonText.length() - 1);
        }
        jsonText = jsonText.trim();
        
        if (jsonText.isEmpty()) {
            return objects;
        }
        
        // Split into individual objects
        ArrayList<String> objectStrings = splitJsonObjects(jsonText);
        
        for (String objStr : objectStrings) {
            JsonObject obj = parseJsonObject(objStr);
            if (obj != null) {
                objects.add(obj);
            }
        }
        
        return objects;
    }
    
    // Parse a single JSON object string into JsonObject
    public static JsonObject parseJsonObject(String jsonText) {
        JsonObject obj = new JsonObject();
        
        jsonText = jsonText.trim();
        
        // Remove braces { }
        if (jsonText.startsWith("{")) {
            jsonText = jsonText.substring(1);
        }
        if (jsonText.endsWith("}")) {
            jsonText = jsonText.substring(0, jsonText.length() - 1);
        }
        jsonText = jsonText.trim();
        
        if (jsonText.isEmpty()) {
            return obj;
        }
        
        // Split into key-value pairs
        ArrayList<String> pairs = splitByComma(jsonText);
        
        for (String pair : pairs) {
            int colonIndex = findColon(pair);
            if (colonIndex > 0) {
                String key = pair.substring(0, colonIndex).trim();
                String value = pair.substring(colonIndex + 1).trim();
                
                // Remove quotes
                key = removeQuotes(key);
                value = removeQuotes(value);
                
                // Unescape special characters
                key = unescapeString(key);
                value = unescapeString(value);
                
                obj.addField(key, value);
            }
        }
        
        return obj;
    }
    
    // Convert ArrayList of JsonObjects to JSON array string
    public static String toJsonArray(ArrayList<JsonObject> objects) {
        StringBuilder sb = new StringBuilder("[");
        
        for (int i = 0; i < objects.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(objects.get(i).toJsonString());
        }
        
        sb.append("]");
        return sb.toString();
    }
    
    // Split JSON array into individual object strings
    private static ArrayList<String> splitJsonObjects(String json) {
        ArrayList<String> objects = new ArrayList<>();
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
    
    // Split by comma, ignoring commas inside quotes
    private static ArrayList<String> splitByComma(String str) {
        ArrayList<String> result = new ArrayList<>();
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
    
    // Find colon position, ignoring colons inside quotes
    private static int findColon(String str) {
        boolean inQuotes = false;
        boolean escaped = false;
        
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            
            if (escaped) {
                escaped = false;
                continue;
            }
            
            if (c == '\\') {
                escaped = true;
                continue;
            }
            
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ':' && !inQuotes) {
                return i;
            }
        }
        
        return -1;
    }
    
    // Remove quotes from start and end
    private static String removeQuotes(String str) {
        str = str.trim();
        if (str.length() >= 2 && str.startsWith("\"") && str.endsWith("\"")) {
            return str.substring(1, str.length() - 1);
        }
        return str;
    }
    
    // Escape special characters for JSON
    private static String escapeString(String str) {
        if (str == null) return "";
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            switch (c) {
                case '\\':
                    sb.append("\\\\");
                    break;
                case '"':
                    sb.append("\\\"");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    sb.append(c);
            }
        }
        
        return sb.toString();
    }
    
    // Unescape special characters from JSON
    private static String unescapeString(String str) {
        if (str == null) return "";
        StringBuilder sb = new StringBuilder();
        boolean escaped = false;
        
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            
            if (escaped) {
                switch (c) {
                    case 'n':
                        sb.append('\n');
                        break;
                    case 'r':
                        sb.append('\r');
                        break;
                    case 't':
                        sb.append('\t');
                        break;
                    case '\\':
                        sb.append('\\');
                        break;
                    case '"':
                        sb.append('"');
                        break;
                    default:
                        sb.append(c);
                }
                escaped = false;
            } else if (c == '\\') {
                escaped = true;
            } else {
                sb.append(c);
            }
        }
        
        return sb.toString();
    }
    
    // Pretty print JSON array
    public static String prettifyJson(String json) {
        StringBuilder sb = new StringBuilder();
        int indent = 0;
        boolean inQuotes = false;
        
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            
            if (c == '"' && (i == 0 || json.charAt(i - 1) != '\\')) {
                inQuotes = !inQuotes;
            }
            
            if (!inQuotes) {
                if (c == '{' || c == '[') {
                    sb.append(c).append('\n');
                    indent++;
                    addIndent(sb, indent);
                } else if (c == '}' || c == ']') {
                    sb.append('\n');
                    indent--;
                    addIndent(sb, indent);
                    sb.append(c);
                } else if (c == ',') {
                    sb.append(c).append('\n');
                    addIndent(sb, indent);
                } else if (c == ':') {
                    sb.append(c).append(' ');
                } else if (c != ' ' && c != '\n' && c != '\r' && c != '\t') {
                    sb.append(c);
                } else if (c == ' ' && i > 0 && json.charAt(i - 1) == ':') {
                    // Skip space after colon (we add it ourselves)
                } else {
                    sb.append(c);
                }
            } else {
                sb.append(c);
            }
        }
        
        return sb.toString();
    }
    
    private static void addIndent(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append("  ");
        }
    }
}