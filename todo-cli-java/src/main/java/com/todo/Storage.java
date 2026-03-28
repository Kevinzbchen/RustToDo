package com.todo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles storage operations for tasks using JSON file.
 */
public class Storage {
    private final String filePath;
    private final Gson gson;
    
    /**
     * Creates a new Storage instance.
     * 
     * @param filePath Path to the JSON file
     */
    public Storage(String filePath) throws IOException {
        this.filePath = filePath;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        
        // Create file if it doesn't exist
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            saveTasks(new ArrayList<>());
        }
    }
    
    /**
     * Adds a new task to storage.
     */
    public void addTask(Task task) throws IOException {
        List<Task> tasks = loadTasks();
        tasks.add(task);
        saveTasks(tasks);
    }
    
    /**
     * Gets all tasks from storage.
     */
    public List<Task> getAllTasks() throws IOException {
        return loadTasks();
    }
    
    /**
     * Marks a task as completed by its ID.
     */
    public void completeTask(String taskId) throws IOException, TaskNotFoundException {
        List<Task> tasks = loadTasks();
        boolean found = false;
        
        for (Task task : tasks) {
            if (task.getId().equals(taskId)) {
                task.markCompleted();
                found = true;
                break;
            }
        }
        
        if (!found) {
            throw new TaskNotFoundException("Task with ID " + taskId + " not found");
        }
        
        saveTasks(tasks);
    }
    
    /**
     * Deletes a task by its ID.
     */
    public void deleteTask(String taskId) throws IOException, TaskNotFoundException {
        List<Task> tasks = loadTasks();
        int initialSize = tasks.size();
        
        tasks.removeIf(task -> task.getId().equals(taskId));
        
        if (tasks.size() == initialSize) {
            throw new TaskNotFoundException("Task with ID " + taskId + " not found");
        }
        
        saveTasks(tasks);
    }
    
    /**
     * Gets a task by its ID.
     */
    public Task getTaskById(String taskId) throws IOException, TaskNotFoundException {
        List<Task> tasks = loadTasks();
        
        for (Task task : tasks) {
            if (task.getId().equals(taskId)) {
                return task;
            }
        }
        
        throw new TaskNotFoundException("Task with ID " + taskId + " not found");
    }
    
    /**
     * Loads tasks from the JSON file.
     */
    private List<Task> loadTasks() throws IOException {
        try (FileReader reader = new FileReader(filePath)) {
            Type taskListType = new TypeToken<ArrayList<Task>>() {}.getType();
            List<Task> tasks = gson.fromJson(reader, taskListType);
            return tasks != null ? tasks : new ArrayList<>();
        }
    }
    
    /**
     * Saves tasks to the JSON file.
     */
    private void saveTasks(List<Task> tasks) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(tasks, writer);
        }
    }
    
    /**
     * Exception thrown when a task is not found.
     */
    public static class TaskNotFoundException extends Exception {
        public TaskNotFoundException(String message) {
            super(message);
        }
    }
}