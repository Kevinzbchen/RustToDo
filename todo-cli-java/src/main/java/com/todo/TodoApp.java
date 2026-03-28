package com.todo;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

@Command(
    name = "todo",
    description = "A Java CLI ToDo application with AI enhancement and colored output",
    mixinStandardHelpOptions = true,
    version = "1.0.0"
)
public class TodoApp implements Callable<Integer> {
    
    @Command(name = "add", description = "Add a new task")
    public Integer add(
        @Parameters(paramLabel = "DESCRIPTION", description = "Task description") String description
    ) {
        try {
            Storage storage = new Storage("tasks.json");
            Task task = new Task(description);
            storage.addTask(task);
            System.out.println("\u001B[32mTask added successfully!\u001B[0m");
            return 0;
        } catch (Exception e) {
            System.err.println("\u001B[31mError: " + e.getMessage() + "\u001B[0m");
            return 1;
        }
    }
    
    @Command(name = "list", description = "List all tasks")
    public Integer list() {
        try {
            Storage storage = new Storage("tasks.json");
            List<Task> tasks = storage.getAllTasks();
            
            if (tasks.isEmpty()) {
                System.out.println("\u001B[33mNo tasks found.\u001B[0m");
                return 0;
            }
            
            System.out.println("\u001B[36m=== Your Tasks ===\u001B[0m\n");
            
            int completedCount = 0;
            int pendingCount = 0;
            int overdueCount = 0;
            
            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);
                System.out.println("\u001B[36mTask #" + (i + 1) + "\u001B[0m " + task);
                System.out.println();
                
                if (task.isCompleted()) {
                    completedCount++;
                } else if (task.isOverdue()) {
                    overdueCount++;
                } else {
                    pendingCount++;
                }
            }
            
            System.out.println("\u001B[36m=== Summary ===\u001B[0m");
            System.out.println("  \u001B[32mCompleted\u001B[0m: " + completedCount);
            System.out.println("  \u001B[33mPending\u001B[0m: " + pendingCount);
            System.out.println("  \u001B[31mOverdue\u001B[0m: " + overdueCount);
            System.out.println("  \u001B[36mTotal\u001B[0m: " + tasks.size());
            
            return 0;
        } catch (Exception e) {
            System.err.println("\u001B[31mError: " + e.getMessage() + "\u001B[0m");
            return 1;
        }
    }
    
    @Command(name = "complete", description = "Mark a task as complete")
    public Integer complete(
        @Parameters(paramLabel = "ID", description = "Task ID") String taskId
    ) {
        try {
            Storage storage = new Storage("tasks.json");
            storage.completeTask(taskId);
            System.out.println("\u001B[32mTask " + taskId + " marked as complete!\u001B[0m");
            return 0;
        } catch (Storage.TaskNotFoundException e) {
            System.err.println("\u001B[31mError: " + e.getMessage() + "\u001B[0m");
            return 1;
        } catch (Exception e) {
            System.err.println("\u001B[31mError: " + e.getMessage() + "\u001B[0m");
            return 1;
        }
    }
    
    @Command(name = "delete", description = "Delete a task")
    public Integer delete(
        @Parameters(paramLabel = "ID", description = "Task ID") String taskId
    ) {
        try {
            Storage storage = new Storage("tasks.json");
            storage.deleteTask(taskId);
            System.out.println("\u001B[31mTask " + taskId + " deleted!\u001B[0m");
            return 0;
        } catch (Storage.TaskNotFoundException e) {
            System.err.println("\u001B[31mError: " + e.getMessage() + "\u001B[0m");
            return 1;
        } catch (Exception e) {
            System.err.println("\u001B[31mError: " + e.getMessage() + "\u001B[0m");
            return 1;
        }
    }
    
    @Command(name = "enhance", description = "Enhance task description using AI")
    public Integer enhance(
        @Parameters(paramLabel = "ID", description = "Task ID") String taskId
    ) {
        try {
            Storage storage = new Storage("tasks.json");
            Task task = storage.getTaskById(taskId);
            
            String apiKey = AIService.getApiKeyFromEnv();
            AIService aiService = new AIService(apiKey);
            
            String enhancedDescription = aiService.enhanceTaskDescription(task.getDescription());
            System.out.println("\u001B[36mEnhanced task description\u001B[0m: " + enhancedDescription);
            
            return 0;
        } catch (Storage.TaskNotFoundException e) {
            System.err.println("\u001B[31mError: " + e.getMessage() + "\u001B[0m");
            return 1;
        } catch (Exception e) {
            System.err.println("\u001B[31mError: " + e.getMessage() + "\u001B[0m");
            return 1;
        }
    }
    
    @Override
    public Integer call() {
        // Default command (when no subcommand is provided)
        System.out.println("Please specify a command. Use 'todo --help' for usage information.");
        return 1;
    }
    
    public static void main(String[] args) {
        int exitCode = new CommandLine(new TodoApp()).execute(args);
        System.exit(exitCode);
    }
}