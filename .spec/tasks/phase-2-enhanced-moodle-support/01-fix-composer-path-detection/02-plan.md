# Plan - Fix Composer Path Detection

## Technical Design

### Current Implementation
- `ComposerUtil.getComposerGlobalDir()` used `OSProcessHandler` with a custom listener that appended almost all output to a `StringBuilder`, leading to the inclusion of PHP warnings in the detected path.
- `MoodleSettingsForm.kt` triggered auto-configuration detection only during the `apply()` phase and didn't verify if the detected paths actually existed before notifying the user.

### Key Decisions
- **Use CapturingProcessHandler**: Switch from `OSProcessHandler` to `CapturingProcessHandler` to easily separate `STDOUT` and `STDERR`.
- **Parse Last Line**: Take the last non-empty line of `STDOUT` as the composer home directory, as this is the standard output format for `composer config --global home`.
- **Validation**: Perform `java.io.File.exists()` checks before presenting paths to the user.

### Proposed Changes
- **ComposerUtil.kt**:
    - Update `getComposerGlobalDir()` to use `CapturingProcessHandler`.
    - Implement logic to extract the last line of `STDOUT`.
- **MoodleSettingsForm.kt**:
    - Wrap detected path usage in existence checks.
    - Extract `configurePhpcs()` and `detectAndNotifyPhpcs()` logic.
    - Add a `button` to the DSL-based panel for manual trigger.

## Implementation Steps

1.  **Refactor ComposerUtil**: (Completed)
    *   Switch to `CapturingProcessHandler`.
    *   Parse last line of output.
2.  **Refactor MoodleSettingsForm**: (Completed)
    *   Extract `configurePhpcs` and `detectAndNotifyPhpcs`.
    *   Add "Rerun" button.
    *   Add existence checks for detected paths.
3.  **Update Bundle**: (Completed)
    *   Add `configurable.phpcs.rerun` and `configurable.phpcs.notfound` messages.
4.  **Verification**: (In Progress)
    *   Compile check.
    *   SDD Documentation.
