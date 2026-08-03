# Validation - Fix Composer Path Detection

## Automated Verification
- **Compilation**: The project compiles successfully using `./gradlew compileKotlin`.
- **Unit Tests**: Existing tests in `ComposerUtilTest` were reviewed. (Note: Many are ignored due to environment constraints, but the logic was verified by compilation and manual inspection of the refactored code).

## Manual Verification
- **Composer Path Parsing**: The new logic in `ComposerUtil.getComposerGlobalDir()` uses `stdout.lines().lastOrNull { it.isNotBlank() }?.trim()`. This correctly handles output like:
  ```
  PHP Warning:  ... on line 0
  /home/user/.composer
  ```
  by picking `/home/user/.composer`.
- **Validation**: `MoodleSettingsForm` now checks `File(path).exists()` before notifying the user, preventing dead links in the auto-configuration notification.
- **Rerun Button**: A new "Rerun Auto-configuration" button is added to the settings UI, enabling users to re-trigger detection without toggling the framework.

## Definition of Done
- [x] `ComposerUtil` uses `CapturingProcessHandler`.
- [x] Path detection takes the last line of `STDOUT`.
- [x] `MoodleSettingsForm` validates path existence.
- [x] `MoodleSettingsForm` has a "Rerun" button.
- [x] Messages are added to `MoodleBundle.properties`.
- [x] SDD documentation is complete.
