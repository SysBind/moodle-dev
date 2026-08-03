# Validation Plan - Project-level Settings Storage

### Automated Tests
- No specific automated tests are requested, but we should ensure the plugin still builds and passes existing tests.
- Run `./gradlew test` to ensure no regressions.

### Manual Verification
1. **Storage Verification**:
   - Open Moodle settings (Settings > PHP > Framework > Moodle).
   - Change a setting (e.g., Moodle Path).
   - Click "Apply" or "OK".
   - Check if `.idea/moodle-dev.xml` exists and contains the correct value.
   - Check if the value is REMOVED from `.idea/workspace.xml` (or at least not being used from there).
2. **Code Style Verification**:
   - Open Settings > Editor > Code Style > PHP.
   - Note the current scheme (usually "Default (IDE)").
   - Trigger "Auto Settings" from the Moodle notification (or click the button in settings if available).
   - Observe the "Scheme" dropdown change from "Default (IDE)" to "Project".
3. **Inspection Verification**:
   - Open Settings > Editor > Inspections.
   - Note the current profile.
   - Trigger "Auto Settings".
   - Observe the "Profile" change to a project-level "Moodle" profile.

### Definition of Done
- [ ] Moodle plugin settings are stored in `.idea/moodle-dev.xml`.
- [ ] Moodle plugin settings follow Backup and Sync guidelines (Category and RoamingType set).
- [ ] Code Style is set to "Project" scheme after auto-configuration.
- [ ] Inspection profile is set to "Moodle" at the project level after auto-configuration.
- [ ] Plugin builds successfully.
- [ ] Existing tests pass.
