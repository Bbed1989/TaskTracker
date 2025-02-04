# TaskTracker

TaskTracker is a command-line task management application built in Java. It allows users to create, update, delete, and manage tasks efficiently.

## Features

- Add new tasks
- List all tasks
- List tasks by status (TODO, IN_PROGRESS, DONE)
- Update existing tasks
- Delete tasks
- Mark tasks as in-progress or done
- Command-line interface for easy interaction

## Getting Started

### Prerequisites

- Java JDK 17 or higher
- Maven

### Installation

1. Clone the repository:
   git clone https://github.com/yourusername/TaskTracker.git
2. Navigate to the project directory:
   cd TaskTracker
3. Build the project:
   mvn clean install

## Usage

Run the application using:
java -jar target/TaskTracker-1.0-SNAPSHOT.jar

### Available Commands

- `add <description>`: Add a new task
- `list`: List all tasks
- `list <status>`: List tasks by status (todo, in-progress, done)
- `update <id> <new description>`: Update a task
- `delete <id>`: Delete a task
- `mark-in-progress <id>`: Mark a task as in-progress
- `mark-done <id>`: Mark a task as done

## Running Tests

To run the tests, use the following command:
mvn test

## Built With

- Java
- Maven
- JUnit 5
- Mockito
- AssertJ

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.


## Acknowledgments

- Thanks to all contributors who have helped shape TaskTracker.