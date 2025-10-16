# Moodle Plugin Development Guidelines

## Project Overview
This is an IntelliJ Platform plugin project for Moodle development support.

## Tech Stack
- Kotlin
- Gradle (with Kotlin DSL)
- IntelliJ Platform SDK
- JUnit for testing
- Kover for code coverage
- Qodana for code quality

## Project Structure
```
.
├── src/
│   ├── main/kotlin/      # Source code
│   ├── main/resources/   # Resources (icons, templates)
│   │   └── META-INF/    # Plugin configuration
│   └── test/            # Test files
├── .run/                # Run configurations
├── build.gradle.kts     # Gradle build configuration
├── gradle.properties    # Gradle properties
├── qodana.yml          # Code quality settings
└── README.md           # Project documentation
```

## Configuration Files
- `src/main/resources/META-INF/plugin.xml`: Core plugin configuration
- `qodana.yml`: Code quality analysis settings
- `gradle.properties`: Gradle and plugin properties
- `.run/`: IDE run configurations

## Build & Run
1. Setup:
   ```bash
   ./gradlew clean
   ```
2. Build:
   ```bash
   ./gradlew build
   ```
3. Run IDE with plugin:
   ```bash
   ./gradlew runIde
   ```

## Testing
- Follow [Testing Docs](https://plugins.jetbrains.com/docs/intellij/testing-plugins.html) and his links to learn more about testing and writing tests.
- Run tests:
  ```bash
  ./gradlew test
  ```
- Run with coverage:
  ```bash
  ./gradlew koverReport
  ```
- UI Tests:
  ```bash
  ./gradlew runIdeForUiTests
  ```

## Development Guidelines
1. Use Kotlin coding conventions
2. Follow instructions from [Jetbriain Plugins docs](https://plugins.jetbrains.com/docs/intellij)
2. Add tests for new features or to any old feature that you updated 
3. Update CHANGELOG.md for changes
4. Update Readme.md if the new feature need Users actions.
5. Follow semantic versioning
6. Keep plugin.xml up to date

## Useful Commands
- Clean and build: `./gradlew clean build`
- Run plugin verifier: `./gradlew runPluginVerifier`
- Generate coverage report: `./gradlew koverReport`
- Publish plugin: `./gradlew publishPlugin`
