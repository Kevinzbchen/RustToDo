use chrono::{DateTime, Duration, Utc};
use colored::*;
use serde::{Deserialize, Serialize};
use uuid::Uuid;

#[derive(Debug, Serialize, Deserialize, Clone)]
pub struct Task {
    pub id: String,
    pub description: String,
    pub completed: bool,
    pub created_at: String,
    pub updated_at: String,
}

impl Task {
    pub fn new(description: String) -> Self {
        let now = Utc::now().to_rfc3339();
        Task {
            id: Uuid::new_v4().to_string(),
            description,
            completed: false,
            created_at: now.clone(),
            updated_at: now,
        }
    }

    pub fn mark_completed(&mut self) {
        self.completed = true;
        self.updated_at = Utc::now().to_rfc3339();
    }

    pub fn created_at_datetime(&self) -> Option<DateTime<Utc>> {
        DateTime::parse_from_rfc3339(&self.created_at)
            .ok()
            .map(|dt| dt.with_timezone(&Utc))
    }

    pub fn updated_at_datetime(&self) -> Option<DateTime<Utc>> {
        DateTime::parse_from_rfc3339(&self.updated_at)
            .ok()
            .map(|dt| dt.with_timezone(&Utc))
    }

    pub fn is_overdue(&self) -> bool {
        if self.completed {
            return false;
        }

        if let Some(created_at) = self.created_at_datetime() {
            let now = Utc::now();
            // Consider a task overdue if it's more than 7 days old and not completed
            let seven_days_ago = now - Duration::days(7);
            created_at < seven_days_ago
        } else {
            false
        }
    }
}

impl std::fmt::Display for Task {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        let (status_symbol, status_color) = if self.completed {
            ("✓", Color::Green)
        } else if self.is_overdue() {
            ("✗", Color::Red)
        } else {
            ("✗", Color::Yellow)
        };

        let status = status_symbol.color(status_color);

        let description_color = if self.completed {
            Color::Green
        } else if self.is_overdue() {
            Color::Red
        } else {
            Color::Yellow
        };

        let description = self.description.color(description_color);

        let created_str = self.created_at_datetime()
            .map(|dt| dt.format("%Y-%m-%d %H:%M:%S").to_string())
            .unwrap_or_else(|| self.created_at.clone());

        let updated_str = self.updated_at_datetime()
            .map(|dt| dt.format("%Y-%m-%d %H:%M:%S").to_string())
            .unwrap_or_else(|| self.updated_at.clone());

        let id_color = if self.completed {
            Color::Green
        } else if self.is_overdue() {
            Color::Red
        } else {
            Color::Yellow
        };

        let id = self.id.color(id_color);

        write!(
            f,
            "[{}] {} (ID: {})\n  Created: {}\n  Updated: {}",
            status, description, id, created_str, updated_str
        )
    }
}