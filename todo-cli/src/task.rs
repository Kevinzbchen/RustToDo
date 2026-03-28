use chrono::{DateTime, Utc};
use serde::{Deserialize, Serialize};
use uuid::Uuid;

#[derive(Debug, Serialize, Deserialize, Clone)]
pub struct Task {
    pub id: String,
    pub description: String,
    pub completed: bool,
    pub created_at: DateTime<Utc>,
    pub updated_at: DateTime<Utc>,
}

impl Task {
    pub fn new(description: String) -> Self {
        let now = Utc::now();
        Task {
            id: Uuid::new_v4().to_string(),
            description,
            completed: false,
            created_at: now,
            updated_at: now,
        }
    }

    pub fn mark_completed(&mut self) {
        self.completed = true;
        self.updated_at = Utc::now();
    }

    pub fn update_description(&mut self, description: String) {
        self.description = description;
        self.updated_at = Utc::now();
    }
}

impl std::fmt::Display for Task {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        let status = if self.completed { "✓" } else { "✗" };
        write!(
            f,
            "[{}] {} (ID: {})\n  Created: {}\n  Updated: {}",
            status,
            self.description,
            self.id,
            self.created_at.format("%Y-%m-%d %H:%M:%S"),
            self.updated_at.format("%Y-%m-%d %H:%M:%S")
        )
    }
}