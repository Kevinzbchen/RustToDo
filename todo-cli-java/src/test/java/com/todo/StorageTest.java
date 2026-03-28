package com.todo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

class StorageTest {
    @TempDir
    Path tempDir;
    
    private Storage storage;
    private String testFilePath;
    
    @BeforeEach
    void setUp() throws IOException {
        testFilePath = tempDir.resolve("test-tasks.json").toString();
        storage = new Storage(testFilePath);
    }
    
    @AfterEach
    void tearDown() {
        // Cleanup is handled by @TempDir
    }
    
    @Test
    void testStorageInitialization() throws IOException {
        // Storage should create file if it doesn't exist
        Path filePath = tempDir.resolve("new-tasks.json");
        Storage newStorage = new Storage(filePath.toString());
        
        assertTrue(filePath.toFile().exists());
        List<Task> tasks = newStorage.getAllTasks();
        assertNotNull(tasks);
        assertTrue(tasks.isEmpty());
    }
    
    @Test
    void testAddAndGetAllTasks() throws IOException {
        Task task1 = new Task("Task 1");
        Task task2 = new Task("Task 2");
        
        storage.addTask(task1);
        storage.addTask(task2);
        
        List<Task> tasks = storage.getAllTasks();
        assertEquals(2, tasks.size());
        assertTrue(tasks.contains(task1));
        assertTrue(tasks.contains(task2));
    }
    
    @Test
    void testCompleteTask() throws IOException, Storage.TaskNotFoundException {
        Task task = new Task("Test task");
        String taskId = task.getId();
        
        storage.addTask(task);
        assertFalse(storage.getAllTasks().get(0).isCompleted());
        
        storage.completeTask(taskId);
        
        Task completedTask = storage.getAllTasks().get(0);
        assertTrue(completedTask.isCompleted());
    }
    
    @Test
    void testCompleteTaskNotFound() throws IOException {
        Task task = new Task("Test task");
        storage.addTask(task);
        
        assertThrows(Storage.TaskNotFoundException.class, () -> {
            storage.completeTask("non-existent-id");
        });
    }
    
    @Test
    void testDeleteTask() throws IOException, Storage.TaskNotFoundException {
        Task task1 = new Task("Task 1");
        Task task2 = new Task("Task 2");
        String task1Id = task1.getId();
        
        storage.addTask(task1);
        storage.addTask(task2);
        assertEquals(2, storage.getAllTasks().size());
        
        storage.deleteTask(task1Id);
        
        List<Task> tasks = storage.getAllTasks();
        assertEquals(1, tasks.size());
        assertEquals(task2.getId(), tasks.get(0).getId());
    }
    
    @Test
    void testDeleteTaskNotFound() throws IOException {
        Task task = new Task("Test task");
        storage.addTask(task);
        
        assertThrows(Storage.TaskNotFoundException.class, () -> {
            storage.deleteTask("non-existent-id");
        });
    }
    
    @Test
    void testGetTaskById() throws IOException, Storage.TaskNotFoundException {
        Task task1 = new Task("Task 1");
        Task task2 = new Task("Task 2");
        String task1Id = task1.getId();
        String task2Id = task2.getId();
        
        storage.addTask(task1);
        storage.addTask(task2);
        
        Task retrievedTask1 = storage.getTaskById(task1Id);
        Task retrievedTask2 = storage.getTaskById(task2Id);
        
        assertEquals(task1.getId(), retrievedTask1.getId());
        assertEquals(task1.getDescription(), retrievedTask1.getDescription());
        assertEquals(task2.getId(), retrievedTask2.getId());
        assertEquals(task2.getDescription(), retrievedTask2.getDescription());
    }
    
    @Test
    void testGetTaskByIdNotFound() throws IOException {
        Task task = new Task("Test task");
        storage.addTask(task);
        
        assertThrows(Storage.TaskNotFoundException.class, () -> {
            storage.getTaskById("non-existent-id");
        });
    }
    
    @Test
    void testPersistence() throws IOException, Storage.TaskNotFoundException {
        // Create tasks and save them
        Task task1 = new Task("Task 1");
        Task task2 = new Task("Task 2");
        String task1Id = task1.getId();
        
        storage.addTask(task1);
        storage.addTask(task2);
        
        // Create new Storage instance to load from same file
        Storage newStorage = new Storage(testFilePath);
        List<Task> loadedTasks = newStorage.getAllTasks();
        
        assertEquals(2, loadedTasks.size());
        
        // Verify we can complete a task and it persists
        newStorage.completeTask(task1Id);
        
        // Create yet another Storage instance to verify persistence
        Storage anotherStorage = new Storage(testFilePath);
        Task completedTask = anotherStorage.getTaskById(task1Id);
        assertTrue(completedTask.isCompleted());
    }
}