# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot 4.0.0-SNAPSHOT application named **policlicaBine** (clinic management system) using:
- Java 25
- Spring Data JPA for data persistence
- H2 in-memory database
- Lombok for boilerplate reduction
- Maven for build management

## Common Commands

### Build and Run
```bash
# Clean and build the project
mvn clean install

# Run the application
mvn spring-boot:run

# Package as JAR
mvn clean package
```

### Testing
```bash
# Run all tests
mvn test

# Run a single test class
mvn test -Dtest=ClassName

# Run a specific test method
mvn test -Dtest=ClassName#methodName
```

### Development
```bash
# Compile without running tests
mvn compile

# Clean build artifacts
mvn clean
```

## Architecture

### Package Structure
- **Base package**: `com.example.policlicabine`
- Standard Spring Boot application with main class: `PoliclicaBineApplication`

### Technology Stack
- **JPA Entities**: Use Lombok annotations for getters/setters/constructors
- **Database**: H2 (in-memory) configured via `application.properties`
- **Maven compiler**: Configured with Lombok annotation processor

### Configuration
- Main config: `src/main/resources/application.properties`
- Application name: `policlicaBine`

## Development Notes

- The project uses Spring Boot 4.0.0-SNAPSHOT, which requires the Spring Snapshots repository
- Lombok is configured as an annotation processor in Maven compiler plugin
- Java version is set to 25