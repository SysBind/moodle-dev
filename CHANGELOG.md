<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# moodle-dev Changelog

All Moodle Dev plugin changes will be documented here
The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0).

## [Unreleased] 

### Added
- Unit and UI tests to verify Composer "Synchronize IDE settings with composer.json" is disabled when enabling the Moodle framework.

## [2.2.1] - 2025-10-16

### Fixed
- When enabling the Moodle framework, the plugin now programmatically disables PHP > Composer > "Synchronize IDE settings with composer.json" to prevent unintended overwrites of IDE configuration.

### Added

- Bundled Moodle inspection profile and registered it in `plugin.xml` so it becomes available after plugin installation.
- Automatically set the bundled "Moodle" inspection profile as the Project profile on first project open (non‑intrusive: won’t override an existing custom selection).
- On project open, construct `InspectionProfileImpl` from bundled `resources/inspectionProfiles/Moodle.xml` and set it as the project profile via `ProjectInspectionProfileManager#setCurrentProfile`.

### Changed

- Dependency updates and build tooling:
  - IntelliJ Platform Gradle plugin: 2.7.2 → 2.9.0.
  - Kotlin JVM: 2.2.10 → 2.2.20.
  - Kover: 0.9.1 → 0.9.2.
  - GitHub Actions: `actions/setup-java` 4 → 5, `gradle/actions` 4 → 5.
- Removed `MoodleManagerListener` (registration moved to `plugin.xml`) and registered bundled inspection profile via `<bundledInspectionProfile/>`.

### Fixed

- Corrected automatic selection of the bundled "Moodle" inspection profile as the Project profile so it reliably appears in Settings | Editor | Inspections.
- Replaced deprecated `ProcessAdapter` with `ProcessListener` in `ComposerUtil.kt` to maintain compatibility with the latest IntelliJ Platform SDK.
- Removed usage of unstable UI DSL `textFieldWithBrowseButton` in `MoodleSettingsForm`; used Swing `TextFieldWithBrowseButton` within `cell(...)` to satisfy `UnstableApiUsage` inspection.

## [2.1.1] - 2025-08-15

### Changed

- Bump dependencies: IntelliJ Platform Gradle plugin 2.7.1 → 2.7.2, Kotlin 2.2.0 → 2.2.10, Qodana plugin/action, and `actions/checkout` 4 → 5.

### Fixed

- Replace deprecated `ProcessAdapter` with `ProcessListener` for IntelliJ SDK compatibility.
- Refactor `MoodleSettingsForm` to replace unstable `textFieldWithBrowseButton` with `TextFieldWithBrowseButton`.

## [2.1.0] - 2025-08-12

### Added

- Development tooling: add `.devcontainer.json` and `.junie` workflow.

### Changed

- Remove unnecessary `isAvailable` overrides in actions.
- Dependency updates: IntelliJ Platform Gradle plugin to 2.7.1, Kotlin to 2.2.0, Gradle toolchains Foojay resolver, and JetBrains changelog plugin updates.
- CI/CD and quality: update Qodana versions and GitHub workflows.

## [2.0.0] - 2025-04-15

### Added

- Automatically setup Moodle Code Sniffer via Composer when enabling Moodle framework
- Automatically run composer install when project directory matches Moodle directory
- Added user settings (username and email) in Moodle Settings
- New ComposerUtil for managing Composer operations
- Added tests for MoodleSettingsForm and ComposerUtil
- Added notification for PHP_Codesniffer configuration with automatic path detection and easy setup guidance
- Added fallback to default Composer directories when command-line detection fails
- Added Windows compatibility with automatic detection of .bat extensions for executables
- Added composer availability check to prevent errors when composer is not installed

### Changed

- Improved PHP include path management in MoodleSettingsForm
- Added support for setting the tool_path attribute for phpcs_by_interpreter configuration
- Updated platform version from 251.23536.39 to 251.23774.350
- Updated handlebars plugin version from 251.23536.38 to 251.23774.318
- Enhanced error handling and logging in Composer operations
- Improved verification of Moodle CS installation
- Improved handling of composer operations when composer is not available
- Enhanced test stability by skipping composer-dependent tests when composer is not available

### Fixed

- Fixed issue in ComposerUtil.getComposerGlobalDir() where it was returning the command instead of the command results
- Fixed path handling in getPhpcsPath() and getPhpcbfPath() for cross-platform compatibility
- Fixed output handling in getComposerGlobalDir() by properly trimming whitespace and newlines

## [1.3.3] - 2025-02-28

### Changed

- Add some fix to compatible with Next PHPStorm Versions

## [1.3.2] - 2025-01-20

### Changed

- Add support to PHPStorm 2025.1

### Fixed

- Looping in include path of php when add the currnt project as Moodle project

## [1.3.1] - 2024-11-16

### Changed

- Add support to PHPStorm 2024.3

### Removed

- Support old version of PHPStorm

## [1.3.0] - 2024-08-20

### Changed

- Return to old plugin id in Jetbrain marketplace.

## [1.2.9]

### Added

- Auto complete to Database object dependency injection
- Add Moodle directory to PHP include path

## [1.2.8] - 2024-08-14

### Changed

- Add support for build 242

## [1.2.7] - 2024-03-26

### Changed

- Update build libraries
- Add support for build 241

## [1.2.6] - 2023-12-15

- Changelog update - `v1.2.5` by @github-actions in https://github.com/SysBind/moodle-dev/pull/68
- Bump JetBrains/qodana-action from 2023.2.9 to 2023.3.0 by @dependabot in https://github.com/SysBind/moodle-dev/pull/69
- Get user detailes by @AviMoto in https://github.com/SysBind/moodle-dev/pull/70

## [1.2.5] - 2023-12-07

Make aduptation to PHPStorm 2023.3 and Improve Moodle PHP class creating

## [1.2.3] - 2023-08-14

### Changed

- Support PHPStorm 2023.2

## [1.2.2] - 2023-04-30

### Changed

- Auto assign predefined Moodle CodeStyle if framework enabled

## [1.2.1] - 2023-04-04

### Changed

- Support PHPStorm 2023.1

## [1.2.0] - 2023-02-07

### Added

- Add new Action to create Moodle php class

### Changed

- Remove deprecated functions

## [1.1.8]

### Changed

- Create new action for create Moodle PHP files

## [1.1.7]

Add support for PHPStorm 2022.2

## [1.1.6] - 2022-05-10

### Added

- Support for PHPStorm 2022.1

## [1.1.4] - 2021-12-13

### Added

- Support for PHPStorm 2021.3

## [1.1.2] - 2021-07-29

### Changed

- Fix SCSS indent
- Fix plugin.xml type error

## [1.1.0] - 2021-05-04

### Added

- Add setting for select location of Moodle Core project directory
- Add Validate that the Moodle core project contain all plugin type directories and approve it as Moodle Core

## [1.0.1] - 2021-04-30

### Changed

- Update the ChangeLog

## [1.0.0] - 2021-04-29

### Added

- Add live Template for Moodle define internal or die by type defm
- Add live Template for Moodle $ADMIN by type ADMIN
- Add Moodle code style for predefined code styles for PHP/Javascript/SCSS/LESS

[Unreleased]: https://github.com/SysBind/moodle-dev/compare/2.2.1...HEAD
[2.2.1]: https://github.com/SysBind/moodle-dev/compare/2.2.0...2.2.1
[2.2.0]: https://github.com/SysBind/moodle-dev/compare/2.1.1...2.2.0
[2.1.1]: https://github.com/SysBind/moodle-dev/compare/2.1.0...2.1.1
[2.1.0]: https://github.com/SysBind/moodle-dev/compare/v2.0.0...2.1.0
[2.0.0]: https://github.com/SysBind/moodle-dev/compare/v1.3.3...v2.0.0
[1.3.3]: https://github.com/SysBind/moodle-dev/compare/v1.3.2...v1.3.3
[1.3.2]: https://github.com/SysBind/moodle-dev/compare/v1.3.1...v1.3.2
[1.3.1]: https://github.com/SysBind/moodle-dev/compare/v1.3.0...v1.3.1
[1.3.0]: https://github.com/SysBind/moodle-dev/compare/v1.2.9...v1.3.0
[1.2.9]: https://github.com/SysBind/moodle-dev/compare/v1.2.8...v1.2.9
[1.2.8]: https://github.com/SysBind/moodle-dev/compare/v1.2.7...v1.2.8
[1.2.7]: https://github.com/SysBind/moodle-dev/compare/v1.2.6...v1.2.7
[1.2.6]: https://github.com/SysBind/moodle-dev/compare/v1.2.5...v1.2.6
[1.2.5]: https://github.com/SysBind/moodle-dev/compare/v1.2.3...v1.2.5
[1.2.3]: https://github.com/SysBind/moodle-dev/compare/v1.2.2...v1.2.3
[1.2.2]: https://github.com/SysBind/moodle-dev/compare/v1.2.1...v1.2.2
[1.2.1]: https://github.com/SysBind/moodle-dev/compare/v1.2.0...v1.2.1
[1.2.0]: https://github.com/SysBind/moodle-dev/compare/v1.1.8...v1.2.0
[1.1.8]: https://github.com/SysBind/moodle-dev/compare/v1.1.7...v1.1.8
[1.1.7]: https://github.com/SysBind/moodle-dev/compare/v1.1.6...v1.1.7
[1.1.6]: https://github.com/SysBind/moodle-dev/compare/v1.1.4...v1.1.6
[1.1.4]: https://github.com/SysBind/moodle-dev/compare/v1.1.2...v1.1.4
[1.1.2]: https://github.com/SysBind/moodle-dev/compare/v1.1.0...v1.1.2
[1.1.0]: https://github.com/SysBind/moodle-dev/compare/v1.0.1...v1.1.0
[1.0.1]: https://github.com/SysBind/moodle-dev/compare/v1.0.0...v1.0.1
[1.0.0]: https://github.com/SysBind/moodle-dev/commits/v1.0.0
