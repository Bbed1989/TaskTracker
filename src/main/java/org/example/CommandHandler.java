package org.example;

import java.io.IOException;
import java.util.Objects;
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

        String command = args[0]; // first word is a command

        switch (command) {
            case "add":
                handleAdd(args);
                break;
            case "list":
                handleList(args);
                break;
            case "update":
                handleUpdate(args);
                break;
            case "delete":
                handleDelete(args);
                break;
            case "mark-in-progress", "mark-done":
                handleStatus(args);
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
        System.out.println("Task added successfully: " + task.getId());
    }

    private void handleList(String[] args) throws IOException {
        if (args.length > 1){
            taskManager.listTasksByStatus(getTaskStatus(args));
            return;
        }
        taskManager.listTasks();
    }

    private void handleUpdate(String[] args) {
        int taskId = getTaskId(args);
        String newDescription = getTaskDescription(args);
        if (newDescription == null) return;

        try {
            boolean updated = taskManager.updateTask(taskId, newDescription);
            System.out.println(updated ? "Task updated successfully." : "Task with ID " + taskId + " not found.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    private void handleDelete(String[] args) {
        int taskId = getTaskId(args);
        try {
            boolean deleted = taskManager.deleteTask(taskId);
            System.out.println(deleted ? "Task deleted successfully." : "Task with ID " + taskId + " not found.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    private void handleStatus(String[] args) {
        int taskId = getTaskId(args);
        TaskStatus taskStatus = getTaskStatus(args);
        try {
            boolean deleted = taskManager.changeStatus(taskStatus, taskId);
            System.out.println(deleted ? "Status changed successfully." : "Task with ID " + taskId + " not found.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    private String getTaskDescription(String[] args) {
        if (args.length == 3) {
            return args[2];
        }
        System.out.print("Enter new description: ");
        return scanner.nextLine().trim();
    }

    private TaskStatus getTaskStatus (String[] args) {
        if (Objects.equals(args[0], "list")) {
            return switch (args[1]) {
                case "in-progress" -> TaskStatus.IN_PROGRESS;
                case "done" -> TaskStatus.DONE;
                case "todo" -> TaskStatus.TODO;
                default -> throw new IllegalArgumentException("Unknown command: " + args[1]);
            };
        }
        return switch (args[0]) {
            case "mark-in-progress" -> TaskStatus.IN_PROGRESS;
            case "mark-done" -> TaskStatus.DONE;
            default -> throw new IllegalArgumentException("Unknown command: " + args[0]);
        };
    }

    private int getTaskId(String[] args) {
        if (args.length >= 2) {
            try {
                return Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid task ID. Please provide a numeric ID.");
                return -1;
            }
        }

        while (true) {
            System.out.print("Enter task ID: ");
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID. Please enter a numeric ID.");
            }
        }
    }
}
