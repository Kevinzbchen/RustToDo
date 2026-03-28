package com.todo;

import com.google.gson.annotations.SerializedName;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Represents a task in the ToDo application.
 */
public class Task {
    private String id;
    private String description;
    private boolean completed;
    
    @SerializedName("created_at")
    private String createdAt;
    
    @SerializedName("updated_at")
    private String updatedAt;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    /**
     * Constructor for creating a new task.
     */
    public Task(String description) {
        this.id = UUID.randomUUID().toString();
        this.description = description;
        this.completed = false;
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now.format(ISO_FORMATTER);
        this.updatedAt = this.createdAt;
    }
    
    /**
     * Default constructor for Gson deserialization.
     */
    public Task() {
        // Required for Gson
    }
    
    // Getters and setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public boolean isCompleted() {
        return completed;
    }
    
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    
    public String getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    /**
     * Marks the task as completed and updates the timestamp.
     */
    public void markCompleted() {
        this.completed = true;
        this.updatedAt = LocalDateTime.now().format(ISO_FORMATTER);
    }
    
    /**
     * Checks if the task is overdue (more than 7 days old and not completed).
     */
    public boolean isOverdue() {
        if (completed) {
            return false;
        }
        
        try {
            LocalDateTime created = LocalDateTime.parse(createdAt, ISO_FORMATTER);
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime sevenDaysAgo = now.minusDays(7);
            return created.isBefore(sevenDaysAgo);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Gets the created date as LocalDateTime.
     */
    public LocalDateTime getCreatedAtDateTime() {
        try {
            return LocalDateTime.parse(createdAt, ISO_FORMATTER);
        } catch (Exception e) {
            return LocalDateTime.now();
        }
    }
    
    /**
     * Gets the updated date as LocalDateTime.
     */
    public LocalDateTime getUpdatedAtDateTime() {
        try {
            return LocalDateTime.parse(updatedAt, ISO_FORMATTER);
        } catch (Exception e) {
            return LocalDateTime.now();
        }
    }
    
    /**
     * Returns a formatted string representation of the task.
     */
    @Override
    public String toString() {
        String status = completed ? "[X]" : "[ ]";
        String colorCode = getColorCode();
        String resetCode = "\u001B[0m";
        
        String createdStr = formatDateTime(createdAt);
        String updatedStr = formatDateTime(updatedAt);
        
        return String.format(
            "%s%s %s (ID: %s)\n  Created: %s\n  Updated: %s%s",
            colorCode, status, description, id, createdStr, updatedStr, resetCode
        );
    }
    
    /**
     * Gets the ANSI color code based on task status.
     */
    private String getColorCode() {
        if (completed) {
            return "\u001B[32m"; // Green
        } else if (isOverdue()) {
            return "\u001B[31m"; // Red
        } else {
            return "\u001B[33m"; // Yellow
        }
    }
    
    /**
     * Formats a date string for display.
     */
    private String formatDateTime(String dateTimeStr) {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, ISO_FORMATTER);
            return dateTime.format(DATE_FORMATTER);
        } catch (Exception e) {
            return dateTimeStr;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id.equals(task.id);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}