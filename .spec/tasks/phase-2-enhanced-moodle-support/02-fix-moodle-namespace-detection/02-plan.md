# Plan - Fix Moodle Namespace Detection

## Task Groups

### 1. Robust Version Parsing
Implement regex-based parsing for `version.php`.

1. Update `MoodleCorePathUtil.getPluginName` to use a Regular Expression for extracting `$plugin->component`.
2. Update `MoodleCorePathUtil.getMoodleVersion` to use a Regular Expression for extracting `$version`.
3. Add helper method or constant for the PHP string literal extraction regex.

### 2. Namespace Logic Refactoring
Improve `getNamespace` and related path utilities.

1. Refactor `MoodleCorePathUtil.getNamespace` to use `directory.virtualFile.path`.
2. Ensure `substringAfter(MOODLE_CLASSES_DIR)` handles path separators correctly across OSs.
3. Verify that `replace("/", "\\")` covers all necessary cases for namespace delimiters.
4. Refactor `MoodleCorePathUtil.getModuleName` to also use `directory.virtualFile.path` for consistency.

### 3. Verification & Cleanup
Ensure everything works as expected.

1. Verify that the "New Moodle Class" dialog now suggests the correct namespace without comments.
2. Verify that the suggested namespace correctly reflects the subdirectory structure under `classes/`.
3. Run existing tests to ensure no regressions in Moodle settings or path detection.
