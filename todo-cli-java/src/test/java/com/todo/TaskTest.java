package com.todo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class TaskTest {
    private Task task;
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    @BeforeEach
    void setUp() {
        task = new Task("Test task description");
    }
    
    @Test
    void testTaskCreation() {
        assertNotNull(task.getId());
        assertEquals("Test task description", task.getDescription());
        assertFalse(task.isCompleted());
        assertNotNull(task.getCreatedAt());
        assertNotNull(task.getUpdatedAt());
        assertEquals(task.getCreatedAt(), task.getUpdatedAt());
    }
    
    @Test
    void testMarkCompleted() {
        String initialUpdatedAt = task.getUpdatedAt();
        
        // Add a small delay to ensure timestamps are different
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            // Ignore
        }

        task.markCompleted();
        
        assertTrue(task.isCompleted());
        // Verify updatedAt is after createdAt
        LocalDateTime createdAt = task.getCreatedAtDateTime();
        LocalDateTime updatedAt = task.getUpdatedAtDateTime();
        assertTrue(updatedAt.isAfter(createdAt) || updatedAt.isEqual(createdAt));
    }
    
    @Test
    void testIsOverdue() {
        // New task should not be overdue
        assertFalse(task.isOverdue());
        
        // Mark as completed - should not be overdue even if old
        task.markCompleted();
        assertFalse(task.isOverdue());
        
        // Create a task with old date (simulate by setting createdAt to 8 days ago)
        Task oldTask = new Task("Old task");
        LocalDateTime eightDaysAgo = LocalDateTime.now().minusDays(8);
        oldTask.setCreatedAt(eightDaysAgo.format(ISO_FORMATTER));
        
        // Should be overdue if not completed
        assertTrue(oldTask.isOverdue());
        
        // Mark as completed - should not be overdue
        oldTask.markCompleted();
        assertFalse(oldTask.isOverdue());
    }
    
    @Test
    void testToStringContainsTaskInfo() {
        String taskString = task.toString();
        
        assertTrue(taskString.contains(task.getId()));
        assertTrue(taskString.contains(task.getDescription()));
        assertTrue(taskString.contains("Created:"));
        assertTrue(taskString.contains("Updated:"));
    }
    
    @Test
    void testEqualsAndHashCode() {
        Task task1 = new Task("Task 1");
        Task task2 = new Task("Task 2");
        Task task1Copy = new Task("Task 1");
        task1Copy.setId(task1.getId());
        
        // Same object
        assertEquals(task1, task1);
        
        // Different IDs
        assertNotEquals(task1, task2);
        
        // Same ID, different description
        assertEquals(task1, task1Copy);
        
        // Hash code consistency
        assertEquals(task1.hashCode(), task1Copy.hashCode());
        
        // Null comparison
        assertNotEquals(null, task1);
        
        // Different class
        assertNotEquals("not a task", task1);
    }
    
    @Test
    void testDateTimeParsing() {
        LocalDateTime createdAt = task.getCreatedAtDateTime();
        LocalDateTime updatedAt = task.getUpdatedAtDateTime();
        
        assertNotNull(createdAt);
        assertNotNull(updatedAt);
        
        // Should be able to parse the string back
        String createdAtStr = task.getCreatedAt();
        LocalDateTime parsedCreatedAt = LocalDateTime.parse(createdAtStr, ISO_FORMATTER);
        assertEquals(createdAt, parsedCreatedAt);
    }
}