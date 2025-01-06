package org.example;

import java.io.IOException;
import java.util.Scanner;

public class CommandHandler {
    private final TaskManager taskManager = new TaskManager();
    private Scanner scanner = new Scanner(System.in);


    public void handleCommand(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            String input = scanner.nextLine().trim();
            args = input.split("\\s+", 2);
        }

        String command = args[0]; // first word is a command}

        switch (command) {
            case "add":
                handleAdd(args);
                break;
            case "list":
                handleList();
                break;
            case "update":
                handleUpdate(args);
                break;
            case "delete":
                handleDelete(args);
                break;
            default:
                System.out.println("Unknown command: " + command);
        }
    }

    private void handleAdd(String[] args) throws IOException {
        String description;
        if (args.length < 2) {
            System.out.println("Please provide a task description.");
            description = scanner.nextLine().trim();
        } else description = args[1];
        Task task = taskManager.createTask(description);
        taskManager.addTask(task);
        System.out.println("Task added successfully: " + task);
    }

    private void handleList() throws IOException {
        taskManager.listTasks();
    }

    private void handleUpdate(String[] args) {
        // Додайте реалізацію для оновлення завдання
    }

    private void handleDelete(String[] args) {
        // Додайте реалізацію для видалення завдання
    }
}
