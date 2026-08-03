# Requirements

### Overview & Goals
Currently, Moodle plugin settings and some auto-configured settings (Code Style, Inspections) are stored at the IDE level or in the non-shareable `workspace.xml`. This makes it difficult to share consistent development environments across a team. This task aims to move these settings to the project level so they can be committed to VCS.

### Scope
- **In Scope**:
  - General Moodle plugin settings (username, email, path).
  - PHP, JavaScript, LESS, and SCSS Code Style settings applied via "Auto Settings".
  - Inspection profile activation for Moodle-specific checks.
- **Out of Scope**:
  - Modifying IDE-wide default settings.
  - Adding new inspections or code style rules beyond what currently exists.

### Decisions
- **Shareable Storage**: Use a dedicated file `.idea/moodle-dev.xml` for Moodle plugin settings.
- **Project-Level Code Style**: Automatically switch the project to use per-project settings when "Auto Settings" is clicked.
- **Project-Level Inspection Profile**: Use a project-level inspection profile instead of an application-level one.
- **Backup and Sync Support**: Assign `SettingsCategory.TOOLS` and `RoamingType.DEFAULT` to ensure settings are eligible for IDE backup and sync.

### Context
- **No Privacy Concerns**: Username and email are considered part of the Moodle code standard requirements and can be shared in VCS.
- **Tech Stack**: Kotlin, IntelliJ Platform SDK.
