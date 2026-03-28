# Java ToDo CLI Application

A Java command-line ToDo application with AI enhancement capabilities and colored output, rewritten from the original Rust version.

## Features

- Add new tasks
- List all tasks with colored output:
  - ✅ Completed tasks in **green**
  - ⏳ Pending tasks in **yellow**  
  - ⚠️ Overdue tasks (7+ days old) in **red**
  - Headers and task numbers in **cyan**
- Mark tasks as complete
- Delete tasks
- Enhance task descriptions using AI (DeepSeek API integration)
- Comprehensive unit tests

## Requirements

- Java 17 or higher
- Maven 3.6 or higher

## Building the Project

```bash
# Clone the repository
cd todo-cli-java

# Build the project
mvn clean package

# The executable JAR will be created at:
# target/todo-cli-1.0.0-jar-with-dependencies.jar
```

## Usage

### Run the application:
```bash
java -jar target/todo-cli-1.0.0-jar-with-dependencies.jar <command>
```

### Or create an alias:
```bash
alias todo='java -jar /path/to/todo-cli-1.0.0-jar-with-dependencies.jar'
```

### Commands:

#### Add a task
```bash
todo add "Buy groceries"
```
*Success message appears in green*

#### List all tasks
```bash
todo list
```
*Shows tasks with color-coded status and summary*

#### Complete a task
```bash
todo complete <task-id>
```
*Success message appears in green*

#### Delete a task
```bash
todo delete <task-id>
```
*Warning message appears in red*

#### Enhance a task description with AI
```bash
todo enhance <task-id>
```
*Shows original task in yellow and enhanced version in green*

## Color Coding

- **Green (✓)**: 
  - Completed tasks
  - Success messages (task added, task completed)
  - AI-enhanced task descriptions
  - Completed task count in summary

- **Yellow (✗)**:
  - Pending tasks
  - Original task in AI enhancement
  - Pending task count in summary

- **Red (✗)**:
  - Overdue tasks (7+ days old and not completed)
  - Delete confirmation messages
  - Overdue task count in summary

- **Cyan**:
  - Headers ("=== Your Tasks ===", "=== Summary ===")
  - Task numbers ("Task #1", "Task #2", etc.)
  - Informational messages ("Enhanced task description:")
  - Total task count in summary

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

**Note**: The current implementation includes a placeholder that simulates AI enhancement when no API key is provided. To use the real DeepSeek API, set the `DEEPSEEK_API_KEY` environment variable.

## Project Structure

```
src/main/java/com/todo/
├── TodoApp.java          # Main CLI application (picocli)
├── Task.java             # Task model with colored output
├── Storage.java          # JSON file storage operations
└── AIService.java        # DeepSeek API integration

src/test/java/com/todo/
├── TaskTest.java         # Unit tests for Task class
├── StorageTest.java      # Unit tests for Storage class
└── AIServiceTest.java    # Unit tests for AIService class
```

## Dependencies

- **picocli** - CLI argument parsing
- **Gson** - JSON serialization/deserialization  
- **OkHttp** - HTTP client for API calls
- **JUnit 5** - Testing framework
- **Mockito** - Mocking framework for tests

## Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=TaskTest

# Run with test coverage report
mvn clean test jacoco:report
```

## Examples

### Adding and listing tasks:
```bash
$ todo add "Learn Java"
Task added successfully!  # Green text

$ todo add "Write documentation"
Task added successfully!  # Green text

$ todo list
=== Your Tasks ===  # Cyan header

Task #1 [✗] Learn Java (ID: ...)  # Yellow pending task
  Created: 2026-03-28 12:00:00
  Updated: 2026-03-28 12:00:00

Task #2 [✗] Write documentation (ID: ...)  # Yellow pending task
  Created: 2026-03-28 12:00:05
  Updated: 2026-03-28 12:00:05

=== Summary ===  # Cyan header
  Completed: 0  # Green count
  Pending: 2    # Yellow count
  Overdue: 0    # Red count
  Total: 2      # Cyan count
```

### Completing a task:
```bash
$ todo complete <task-id>
Task <task-id> marked as complete!  # Green text

$ todo list
...
Task #1 [✓] Learn Java (ID: ...)  # Green completed task
...
=== Summary ===
  Completed: 1  # Green count
  Pending: 1    # Yellow count
  ...
```

## Comparison with Rust Version

This Java version maintains all features from the original Rust implementation:

- ✅ Same CLI commands and functionality
- ✅ Colored output with same color scheme
- ✅ JSON file storage (`tasks.json`)
- ✅ AI enhancement (placeholder + DeepSeek API)
- ✅ Overdue task detection (7+ days)
- ✅ Task summary with counts

Additional improvements in Java version:
- ✅ Comprehensive unit tests
- ✅ Better error handling with custom exceptions
- ✅ Maven build system
- ✅ Proper package structure

## License

MIT