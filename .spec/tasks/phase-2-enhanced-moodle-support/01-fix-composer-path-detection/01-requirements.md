# Requirements - Fix Composer Path Detection

## Overview & Goals
The goal of this task is to fix a bug where the plugin's auto-configuration feature for PHP_CodeSniffer captures incorrect paths containing PHP warnings (e.g., "on line 0..."). Additionally, the task includes improving the user experience by adding path validation and a manual trigger for the auto-configuration logic.

## Scope
- **In Scope**:
    - Fixing path detection in `ComposerUtil.kt`.
    - Adding file existence checks in `MoodleSettingsForm.kt`.
    - Adding a "Rerun Auto-configuration" button in the Moodle Framework settings UI.
    - Implementing SDD (Spec-Driven Development) documentation for this fix.
- **Out of Scope**:
    - Fixing general PHP configuration issues on the user's system.
    - Modifying other CLI tool integrations beyond `ComposerUtil`.

## User Stories
- As a Moodle Developer, I want the plugin to correctly detect my global `phpcs` path even if my PHP environment produces warnings, so that I don't have to manually fix the paths in settings.
- As a Moodle Developer, I want to be able to re-trigger the automatic setup of quality tools from the settings page, so that I can easily fix my environment after installing missing dependencies.

## Context
- The plugin relies on `composer config --global home` to find the global vendor directory.
- PHP warnings on some systems pollute the output, causing the plugin to fail to find the tools.
- Validation ensures that even if a path is detected, it is only suggested if it actually exists.
