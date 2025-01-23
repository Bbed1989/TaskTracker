package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class TaskManager {
    private static final String FILE_NAME = "tasks.json";
    // Constant for recognizing empty JSON lists
    private static final String EMPTY_JSON = "[]";

    public Task createTask(String description) {
        return new Task(
                (int) (Math.random() * 1000), // generating random Id
                description,
                TaskStatus.TODO, // task starts with TODO status
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public void addTask(Task task) throws IOException {
        List<Map<String, Object>> tasks = readTasks();

        Map<String, Object> taskMap = new HashMap<>();
        taskMap.put("id", task.getId());
        taskMap.put("description", task.getDescription());
        taskMap.put("status", task.getStatus().toString());
        taskMap.put("createdAt", task.getCreatedAt());
        taskMap.put("updatedAt", task.getUpdatedAt());

        tasks.add(taskMap);

        writeTasks(tasks);
    }

    public void listTasks() throws IOException {
        List<Map<String, Object>> tasks = readTasks();

        for (Map<String, Object> task : tasks) {
            System.out.println(task.get("description").toString());
        }
    }

    public void listTasksByStatus (TaskStatus taskStatus) throws IOException {
        List<Map<String, Object>> tasks = readTasks();
        for(Map<String, Object> task : tasks){
            if(TaskStatus.valueOf(task.get("status").toString()) == taskStatus) {
                System.out.println(task.get("description").toString());
            }
        }
    }

    public boolean updateTask(int id, String description) throws IOException {
        List<Map<String, Object>> tasks = readTasks();
        boolean isUpdated = false;
        for (Map<String, Object> task : tasks) {
            if (Integer.parseInt(task.get("id").toString()) == id) {
                task.put("description", description);
                task.put("updatedAt", LocalDateTime.now().toString());
                isUpdated = true;
                break;
            }
        }
        if(isUpdated) {
            writeTasks(tasks);
        }
        return true;
    }

    public boolean deleteTask (Integer id) throws IOException {
        List<Map<String, Object>> tasks = readTasks();
        boolean isDeleted = false;
        for (Map<String, Object> task : tasks) {
            if (Integer.parseInt(task.get("id").toString()) == id) {
                task.clear();
                isDeleted = true;
                break;
            }
        }
        if (isDeleted) {
            writeTasks(tasks);
        }
        return isDeleted;
    }

    public boolean changeStatus(TaskStatus taskStatus, int id) throws IOException {
        List<Map<String, Object>> tasks = readTasks();
        boolean statusChanged = false;
        for (Map<String, Object> task : tasks) {
            if (Integer.parseInt(task.get("id").toString()) == id) {
                task.put("status", taskStatus);
                task.put("updatedAt", LocalDateTime.now().toString());
                statusChanged = true;
                break;
            }
        }
        if (statusChanged) {
            writeTasks(tasks);
        }
        return statusChanged;
    }

    private void writeTasks(List<Map<String, Object>> tasks) throws IOException {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            // filter empty maps
            List<Map<String, Object>> nonEmptyTasks = tasks.stream()
                    .filter(task -> !task.isEmpty())  //skip empty
                    .collect(Collectors.toList());

            if (nonEmptyTasks.isEmpty()) {
                writer.write("[]");
            } else {
                writer.write("[\n");
                String jsonContent = nonEmptyTasks.stream()
                        .map(this::mapToJson)
                        .collect(Collectors.joining(",\n"));
                writer.write(jsonContent);
                writer.write("\n]");
            }
        }
    }

    private Map<String, Object> parseJsonToMap(String json) {
        Map<String, Object> map = new HashMap<>();
        String[] pairs = json.substring(1, json.length() - 1).split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.split(":", 2);
            String key = keyValue[0].trim().replaceAll("\"", "");
            String value = keyValue[1].trim().replaceAll("\"", "");
            map.put(key, value);
        }
        return map;
    }

    private String mapToJson(Map<String, Object> map) {
        StringBuilder json = new StringBuilder("{");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            json.append("\"").append(entry.getKey()).append("\":");
            json.append("\"").append(entry.getValue()).append("\",");
        }
        if (json.length() > 1) {
            json.deleteCharAt(json.length() - 1); // Видалення останньої коми
        }
        json.append("}");
        return json.toString();
    }

    private List<Map<String, Object>> readTasks() throws IOException {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        String jsonString = readFileContent(file).trim();

        if (jsonString.isEmpty() || jsonString.equals(EMPTY_JSON)) {
            return new ArrayList<>();
        }

        return parseTasksFromJson(jsonString);
    }

    // Helper method to read the content of a file
    private String readFileContent(File file) throws IOException {
        StringBuilder json = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
        }
        return json.toString();
    }

    // Helper method to parse a JSON string into a List of Maps
    private List<Map<String, Object>> parseTasksFromJson(String jsonString) {
        List<Map<String, Object>> tasks = new ArrayList<>();
        String[] objects = jsonString.substring(1, jsonString.length() - 1).split("},\\{");
        for (String obj : objects) {
            obj = obj.trim();
            if (!obj.startsWith("{")) obj = "{" + obj;
            if (!obj.endsWith("}")) obj = obj + "}";
            tasks.add(parseJsonToMap(obj));
        }
        return tasks;
    }
}
