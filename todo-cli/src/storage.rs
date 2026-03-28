use crate::task::Task;
use anyhow::Result;
use serde_json;
use std::fs::File;
use std::io::{BufReader, BufWriter};
use std::path::Path;

pub struct Storage {
    file_path: String,
}

impl Storage {
    pub fn new(file_path: &str) -> Result<Self> {
        let storage = Storage {
            file_path: file_path.to_string(),
        };
        
        // Create file if it doesn't exist
        if !Path::new(file_path).exists() {
            let empty_tasks: Vec<Task> = Vec::new();
            storage.save_tasks(&empty_tasks)?;
        }
        
        Ok(storage)
    }

    pub fn add_task(&mut self, task: Task) -> Result<()> {
        let mut tasks = self.load_tasks()?;
        tasks.push(task);
        self.save_tasks(&tasks)
    }

    pub fn get_all_tasks(&self) -> Result<Vec<Task>> {
        self.load_tasks()
    }

    pub fn complete_task(&mut self, task_id: &str) -> Result<()> {
        let mut tasks = self.load_tasks()?;
        
        for task in tasks.iter_mut() {
            if task.id == task_id {
                task.mark_completed();
                return self.save_tasks(&tasks);
            }
        }
        
        Err(anyhow::anyhow!("Task with ID {} not found", task_id))
    }

    pub fn delete_task(&mut self, task_id: &str) -> Result<()> {
        let mut tasks = self.load_tasks()?;
        let initial_len = tasks.len();
        
        tasks.retain(|task| task.id != task_id);
        
        if tasks.len() == initial_len {
            return Err(anyhow::anyhow!("Task with ID {} not found", task_id));
        }
        
        self.save_tasks(&tasks)
    }

    pub fn get_task_by_id(&self, task_id: &str) -> Result<Task> {
        let tasks = self.load_tasks()?;
        
        for task in tasks {
            if task.id == task_id {
                return Ok(task);
            }
        }
        
        Err(anyhow::anyhow!("Task with ID {} not found", task_id))
    }

    pub fn update_task(&mut self, updated_task: Task) -> Result<()> {
        let mut tasks = self.load_tasks()?;
        
        for task in tasks.iter_mut() {
            if task.id == updated_task.id {
                *task = updated_task;
                return self.save_tasks(&tasks);
            }
        }
        
        Err(anyhow::anyhow!("Task with ID {} not found", updated_task.id))
    }

    fn load_tasks(&self) -> Result<Vec<Task>> {
        let file = File::open(&self.file_path)?;
        let reader = BufReader::new(file);
        let tasks: Vec<Task> = serde_json::from_reader(reader)?;
        Ok(tasks)
    }

    fn save_tasks(&self, tasks: &Vec<Task>) -> Result<()> {
        let file = File::create(&self.file_path)?;
        let writer = BufWriter::new(file);
        serde_json::to_writer_pretty(writer, tasks)?;
        Ok(())
    }
}