# Rust ToDo CLI Application

A simple command-line ToDo application written in Rust with AI enhancement capabilities.

## Features

- Add new tasks
- List all tasks
- Mark tasks as complete
- Delete tasks
- Enhance task descriptions using AI (DeepSeek API integration)

## Installation

1. Make sure you have Rust installed. If not, install it from [rust-lang.org](https://rust-lang.org)
2. Clone this repository
3. Build the project:

```bash
cargo build --release
```

## Usage

### Add a task
```bash
todo add "Buy groceries"
```

### List all tasks
```bash
todo list
```

### Complete a task
```bash
todo complete <task-id>
```

### Delete a task
```bash
todo delete <task-id>
```

### Enhance a task description with AI
```bash
todo enhance <task-id>
```

## AI Integration

The AI enhancement feature uses the DeepSeek API. To use it:

1. Get an API key from [DeepSeek](https://platform.deepseek.com/)
2. Set it as an environment variable:

```bash
export DEEPSEEK_API_KEY="your-api-key-here"
```

On Windows:
```powershell
$env:DEEPSEEK_API_KEY="your-api-key-here"
```

## Project Structure

- `src/main.rs` - Main entry point with CLI argument parsing
- `src/task.rs` - Task struct and related functions
- `src/storage.rs` - JSON file storage operations
- `src/ai.rs` - DeepSeek API integration

## Dependencies

- `clap` - CLI argument parsing
- `serde` - Serialization/deserialization
- `anyhow` - Error handling
- `reqwest` - HTTP client for API calls
- `tokio` - Async runtime
- `chrono` - Date/time handling
- `uuid` - Unique ID generation

## License

MIT