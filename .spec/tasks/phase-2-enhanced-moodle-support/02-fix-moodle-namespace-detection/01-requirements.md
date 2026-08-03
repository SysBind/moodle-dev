# Requirements - Fix Moodle Namespace Detection

## Overview & Goals
Fix the issue where Moodle class namespace detection includes PHP remarks/comments from the `version.php` file. Additionally, improve the reliability of namespace generation from directory paths.

## Scope
### In Scope
- Robust parsing of `version.php` to extract `$plugin->component` and `$version` without any trailing PHP remarks or comments.
- Refactoring of namespace generation logic to use reliable file system paths instead of `PsiDirectory.toString()`.
- Ensuring the suggested namespace uses correct PHP backslash (`\\`) delimiters.
- Support for all Moodle class types (Class, Interface, Trait, Enum).

### Out of Scope
- Changing how Moodle core paths are detected outside of the namespace logic.
- Adding new UI elements to the Moodle New Class dialog.

## Decisions
- **Robust Parsing Strategy**: Use Regular Expressions to extract string literal values from PHP lines in `version.php`. This will handle `//`, `/* */`, and `#` comments, as well as varying whitespace and quote types.
- **Path Detection**: Use `directory.virtualFile.path` instead of `directory.toString()` in `MoodleCorePathUtil.getNamespace` to ensure absolute and reliable path information.
- **Delimiter**: Stick to the PHP standard backslash (`\\`) for namespaces, even if the user mentioned forward slashes, as the goal is to produce valid PHP code.

## Context
- The project follows standard IntelliJ Platform plugin patterns.
- Moodle's "Frankenstyle" component names (e.g., `mod_assign`) are the base of the namespace.
- Files under `classes/` directory map directly to namespaces (e.g., `classes/output/file.php` -> `namespace mod_assign\\output;`).
