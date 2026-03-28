use crate::storage::Storage;
use anyhow::Result;
use colored::*;
use reqwest::Client;
use serde_json::json;

const DEEPSEEK_API_URL: &str = "https://api.deepseek.com/v1/chat/completions";
const DEEPSEEK_MODEL: &str = "deepseek-chat";

pub async fn enhance_task_description(task_id: &str, storage: &Storage) -> Result<String> {
    // Get the task from storage
    let task = storage.get_task_by_id(task_id)?;
    
    println!("{}: {}", "Original task".yellow(), task.description);

    // For now, we'll use a placeholder implementation
    // In a real implementation, you would need to:
    // 1. Get an API key from DeepSeek
    // 2. Make an actual API call
    // 3. Parse the response
    
    // Placeholder implementation - simulates AI enhancement
    let enhanced = if task.description.to_lowercase().contains("learn") {
        format!("{} - Break down into weekly study goals with practice exercises",
               task.description)
    } else if task.description.to_lowercase().contains("buy") ||
              task.description.to_lowercase().contains("groceries") {
        format!("{} - Create a shopping list and check pantry inventory first",
               task.description)
    } else if task.description.to_lowercase().contains("write") ||
              task.description.to_lowercase().contains("document") {
        format!("{} - Outline key sections first, then write introduction and conclusion",
               task.description)
    } else {
        format!("{} - Make it SMART (Specific, Measurable, Achievable, Relevant, Time-bound)",
               task.description)
    };

    println!("{}: {}", "AI-enhanced task".green(), enhanced);

    // In a real implementation, you would update the task with the enhanced description
    // let mut updated_task = task.clone();
    // updated_task.update_description(enhanced.clone());
    // storage.update_task(updated_task)?;

    Ok(enhanced)
}

// This is the actual implementation that would work with a real API key
#[allow(dead_code)]
async fn enhance_with_deepseek_api(description: &str, api_key: &str) -> Result<String> {
    let client = Client::new();

    let request_body = json!({
        "model": DEEPSEEK_MODEL,
        "messages": [
            {
                "role": "system",
                "content": "You are a helpful assistant that improves task descriptions to make them more specific, actionable, and clear."
            },
            {
                "role": "user",
                "content": format!("Please improve this task description: '{}'. Make it more specific and actionable.", description)
            }
        ],
        "max_tokens": 100,
        "temperature": 0.7
    });

    let response = client
        .post(DEEPSEEK_API_URL)
        .header("Authorization", format!("Bearer {}", api_key))
        .header("Content-Type", "application/json")
        .json(&request_body)
        .send()
        .await?;

    if response.status().is_success() {
        let response_json: serde_json::Value = response.json().await?;
        let enhanced_description = response_json["choices"][0]["message"]["content"]
            .as_str()
            .unwrap_or(description)
            .to_string();

        Ok(enhanced_description)
    } else {
        let error_text = response.text().await?;
        Err(anyhow::anyhow!("API request failed: {}", error_text))
    }
}

// Helper function to get API key from environment
#[allow(dead_code)]
fn get_api_key() -> Result<String> {
    std::env::var("DEEPSEEK_API_KEY")
        .map_err(|_| anyhow::anyhow!("DEEPSEEK_API_KEY environment variable not set"))
}
