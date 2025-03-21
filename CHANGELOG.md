<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# moodle-dev Changelog

All Moodle Dev plugin changes will be documented here
The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0).

## [Unreleased]

### Added

- Automatically setup Moodle Code Sniffer via Composer when enabling Moodle framework
- Automatically run composer install when project directory matches Moodle directory
- Added user settings (username and email) in Moodle Settings
- New ComposerUtil for managing Composer operations
- Added tests for MoodleSettingsForm and ComposerUtil
- Added notification for PHP_Codesniffer configuration with automatic path detection and easy setup guidance

### Changed

- Improved PHP include path management in MoodleSettingsForm
- Added support for setting the tool_path attribute for phpcs_by_interpreter configuration

### Fixed

- Fixed issue in ComposerUtil.getComposerGlobalDir() where it was returning the command instead of the command results


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

[Unreleased]: https://github.com/SysBind/moodle-dev/compare/v1.3.3...HEAD
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
