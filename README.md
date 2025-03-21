# Moodle Development Plugin for IntelliJ IDEA

![Build](https://github.com/SysBind/moodle-dev/workflows/Build/badge.svg)
![Version](https://img.shields.io/jetbrains/plugin/v/16702)
![Downloads](https://img.shields.io/jetbrains/plugin/d/16702)
![Rating](https://img.shields.io/jetbrains/plugin/r/rating/16702)

<!-- Plugin description -->
This plugin helps Moodle Developers work efficiently by providing development tools and ensuring compliance with Moodle coding standards and requirements.
<!-- Plugin description end -->

## Requirements

Before installing the plugin, ensure you have:

1. PHP 7.4 or later installed and available in PATH
2. Composer installed globally and available in PATH
3. Git (optional, but recommended for version control)

## Installation

### Using IDE built-in plugin system:

1. Open IntelliJ IDEA
2. Go to <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd>
3. Search for "moodle-dev"
4. Click <kbd>Install Plugin</kbd>
5. Restart IDE when prompted

### Manual Installation:

1. Download the [latest release](https://github.com/SysBind/moodle-dev/releases/latest)
2. Open IntelliJ IDEA
3. Go to <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>
4. Select the downloaded plugin file
5. Restart IDE when prompted

## Configuration

### Option 1: Auto Settings (Recommended)

1. Open your Moodle project in IntelliJ IDEA
2. Right-click on the project in the Project Explorer
3. Select "Moodle" > "Auto Settings"
4. The plugin will automatically:
   - Enable all required features
   - Configure your developer information
   - Set up PHP_CodeSniffer with Moodle standards
   - Set Composer's minimum-stability to dev
   - Install moodlehq/moodle-cs globally
   - Configure all necessary paths

### Option 2: Manual Configuration

If you need to adjust settings manually:

1. For PHP_CodeSniffer settings:
   - Go to <kbd>Settings/Preferences</kbd> > <kbd>PHP</kbd> > <kbd>Quality Tools</kbd> > <kbd>PHP_CodeSniffer</kbd>
   - Configure paths to phpcs and phpcbf executables
   - Set up coding standards

2. For other Moodle-specific settings:
   - Go to <kbd>Settings/Preferences</kbd> > <kbd>PHP</kbd> > <kbd>Frameworks</kbd>
   - Find the "Moodle" section
   - Adjust settings as needed

## Features

- Moodle-specific code templates and generators
- Automatic code style configuration for:
  - PHP
  - JavaScript
  - LESS
  - SCSS
- Integration with Moodle Code Sniffer
- Support for Moodle project structure:
  - PHP classes in 'classes/' directory
  - Mustache templates in 'templates/'
  - Database files in 'db/'
  - Language files in 'lang/'
  - JavaScript files in 'amd/src/'
  - CLI scripts in 'cli/'
  - Backup files in 'backup/'
  - Images in 'pix/'

## Troubleshooting

### Common Issues

1. **Plugin not detecting Moodle directory**
   - Ensure the directory contains a valid Moodle installation
   - Check if version.php exists in the root directory
   - Verify directory permissions

2. **PHP_Codesniffer not working**
   - Verify Composer is installed and in PATH
   - Check Composer global installation directory permissions
   - Try running `composer global require moodlehq/moodle-cs` manually

3. **Code style not applying**
   - Ensure the plugin is enabled
   - Verify the Moodle project directory is correctly set
   - Try restarting the IDE

### Getting Help

- Check the [plugin issues page](https://github.com/SysBind/moodle-dev/issues)
- Submit a new issue if you encounter problems
- Include relevant logs and configuration details when reporting issues

---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
