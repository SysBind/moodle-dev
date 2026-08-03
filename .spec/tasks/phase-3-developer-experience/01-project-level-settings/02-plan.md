# Implementation Plan - Project-level Settings Storage

### Task Group 1: Moodle Plugin Settings Storage
1. Modify `MoodleProjectSettings.kt` to change the storage from `StoragePathMacros.WORKSPACE_FILE` to a dedicated project file `moodle-dev.xml`.
2. Update `MoodleProjectSettings.kt` to include `SettingsCategory.TOOLS` and `RoamingType.DEFAULT` for Backup and Sync support.
3. Verify that existing settings are either migrated or correctly handled in the new storage.

### Task Group 2: Project-Level Code Style Configuration
1. Update `MoodleSettingsForm.configurePhpcs` to enable `USE_PER_PROJECT_SETTINGS` in `CodeStyleSettingsManager`.
2. Ensure `MoodleSettingsForm.apply` applies code styles to the project-level settings.

### Task Group 3: Project-Level Inspection Profile Configuration
1. Modify the inspection profile logic in `MoodleSettingsForm.configurePhpcs` to use `ProjectInspectionProfileManager` to set a project-level profile.

### Task Group 4: Verification
1. Verify storage in `.idea/moodle-dev.xml`.
2. Verify Code Style scheme is "Project".
3. Verify Inspection Profile is project-level.
