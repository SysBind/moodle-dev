package il.co.sysbind.intellij.moodledev.util

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessListener
import com.intellij.execution.process.ProcessEvent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key

object ComposerUtil {
    private val log = Logger.getInstance(ComposerUtil::class.java)
    private var composerGlobalDir: String? = null
    private var isComposerAvailable: Boolean? = null

    /**
     * Check if composer is available in the system
     * @return true if composer is available, false otherwise
     */
    fun isComposerAvailable(): Boolean {
        if (isComposerAvailable != null) {
            return isComposerAvailable!!
        }

        try {
            val commandLine = GeneralCommandLine("composer")
            commandLine.addParameters(listOf("--version"))

            val processHandler = OSProcessHandler(commandLine)
            processHandler.startNotify()
            processHandler.waitFor()

            isComposerAvailable = processHandler.exitCode == 0
            log.debug("Composer availability check: $isComposerAvailable")
            return isComposerAvailable!!
        } catch (e: Exception) {
            log.warn("Failed to check composer availability: ${e.message}")
            isComposerAvailable = false
            return false
        }
    }

    fun getComposerGlobalDir(): String? {
        if (composerGlobalDir != null) {
            return composerGlobalDir
        }

        // Check if composer is available before trying to run composer commands
        if (isComposerAvailable()) {
            try {
                val commandLine = GeneralCommandLine("composer")
                commandLine.addParameters(listOf("config", "--global", "home"))

                val output = StringBuilder()
                val processHandler = OSProcessHandler(commandLine)
                processHandler.addProcessListener(object : ProcessListener {
                    override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
                        // Only append the actual output, not the command itself
                        val text = event.text.trim()
                        if (!text.startsWith("composer config --global home")) {
                            output.append(text)
                        }
                    }
                })

                processHandler.startNotify()
                processHandler.waitFor()

                if (processHandler.exitCode == 0 && output.isNotEmpty()) {
                    // Clean up the output - remove any extra whitespace or newlines
                    composerGlobalDir = output.toString().trim()
                    log.debug("Found composer global directory: $composerGlobalDir")
                    return composerGlobalDir
                } else {
                    log.warn("Composer command exited with code ${processHandler.exitCode} or empty output")
                }
            } catch (e: Exception) {
                log.error("Failed to get composer global directory: ${e.message}", e)
            }
        } else {
            log.warn("Composer is not available, falling back to default locations")
        }

        // Fallback to default locations if composer command fails
        val userHome = System.getProperty("user.home")
        val possibleLocations = listOf(
            "$userHome/.composer",                // Linux/Mac
            "$userHome/AppData/Roaming/Composer"  // Windows
        )

        for (location in possibleLocations) {
            val dir = java.io.File(location)
            if (dir.exists() && dir.isDirectory) {
                log.debug("Using fallback composer global directory: $location")
                composerGlobalDir = location
                return composerGlobalDir
            }
        }

        log.warn("Could not determine composer global directory")
        return null
    }

    fun getPhpcsPath(): String? {
        return getComposerGlobalDir()?.let { 
            val path = java.io.File(it, "vendor/bin/phpcs")
            if (System.getProperty("os.name").lowercase().contains("windows")) {
                // On Windows, the executable might have a .bat extension
                if (java.io.File("${path}.bat").exists()) {
                    return@let "${path}.bat"
                }
            }
            path.absolutePath
        }
    }

    fun getPhpcbfPath(): String? {
        return getComposerGlobalDir()?.let { 
            val path = java.io.File(it, "vendor/bin/phpcbf")
            if (System.getProperty("os.name").lowercase().contains("windows")) {
                // On Windows, the executable might have a .bat extension
                if (java.io.File("${path}.bat").exists()) {
                    return@let "${path}.bat"
                }
            }
            path.absolutePath
        }
    }

    fun runComposerInstall(project: Project, directory: String): Boolean {
        // First check if composer is available
        if (!isComposerAvailable()) {
            log.warn("Composer is not available, skipping composer install")
            return false
        }

        val workDir = java.io.File(directory)
        if (!workDir.exists() || !workDir.isDirectory) {
            log.debug("Directory does not exist or is not a directory: $directory")
            return false
        }

        try {
            val commandLine = GeneralCommandLine("composer")
            commandLine.addParameter("install")
            commandLine.workDirectory = workDir

            val processHandler = OSProcessHandler(commandLine)
            processHandler.addProcessListener(object : ProcessListener {
                override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
                    log.debug("Composer install output: ${event.text}")
                }

                override fun processTerminated(event: ProcessEvent) {
                    if (event.exitCode != 0) {
                        log.warn("Composer install failed with exit code: ${event.exitCode}")
                    }
                }
            })

            processHandler.startNotify()
            processHandler.waitFor()

            return processHandler.exitCode == 0
        } catch (e: Exception) {
            log.error("Failed to run composer install: ${e.message}", e)
            return false
        }
    }

    fun setupMoodleCs(project: Project): Boolean {
        log.debug("Setting up Moodle CS...")

        // First check if composer is available
        if (!isComposerAvailable()) {
            log.warn("Composer is not available, skipping Moodle CS setup")
            return false
        }

        // Check if phpcs and phpcbf already exist
        val phpcsPath = getPhpcsPath()
        val phpcbfPath = getPhpcbfPath()

        if (phpcsPath != null && phpcbfPath != null) {
            val phpcsFile = java.io.File(phpcsPath)
            val phpcbfFile = java.io.File(phpcbfPath)

            if (phpcsFile.exists() && phpcbfFile.exists()) {
                log.debug("PHPCS and PHPCBF already exist, checking if moodle standard is available")

                // Check if moodle standard is available
                try {
                    val commandLine = GeneralCommandLine(phpcsPath)
                    commandLine.addParameters(listOf("-i"))

                    val output = StringBuilder()
                    val processHandler = OSProcessHandler(commandLine)
                    processHandler.addProcessListener(object : ProcessListener {
                        override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
                            output.append(event.text)
                        }
                    })

                    processHandler.startNotify()
                    processHandler.waitFor()

                    if (processHandler.exitCode == 0 && output.toString().lowercase().contains("moodle")) {
                        log.debug("Moodle standard is already available")
                        return true
                    }
                } catch (e: Exception) {
                    log.warn("Failed to check if moodle standard is available: ${e.message}")
                    // Continue with setup
                }
            }
        }

        try {
            // Set minimum-stability to dev
            log.debug("Setting composer minimum-stability to dev")
            if (!executeComposerCommand(project, listOf("global", "config", "minimum-stability", "dev"))) {
                log.warn("Failed to set composer minimum-stability to dev, but continuing with installation")
            }

            // Install moodlehq/moodle-cs
            log.debug("Installing moodlehq/moodle-cs")
            if (!executeComposerCommand(project, listOf("global", "require", "moodlehq/moodle-cs"))) {
                log.error("Failed to install moodlehq/moodle-cs")

                // Try alternative approach - install with --dev flag
                log.debug("Trying alternative approach with --dev flag")
                if (!executeComposerCommand(project, listOf("global", "require", "--dev", "moodlehq/moodle-cs"))) {
                    log.error("Failed to install moodlehq/moodle-cs with --dev flag")
                    return false
                }
            }

            // Verify installation
            val newPhpcsPath = getPhpcsPath()
            val newPhpcbfPath = getPhpcbfPath()

            if (newPhpcsPath != null && newPhpcbfPath != null) {
                val newPhpcsFile = java.io.File(newPhpcsPath)
                val newPhpcbfFile = java.io.File(newPhpcbfPath)

                if (newPhpcsFile.exists() && newPhpcbfFile.exists()) {
                    log.debug("Successfully installed PHPCS and PHPCBF")
                    return true
                }
            }

            log.warn("PHPCS or PHPCBF not found after installation")
            return false
        } catch (e: Exception) {
            log.error("Error setting up Moodle CS: ${e.message}", e)
            return false
        }
    }

    private fun executeComposerCommand(project: Project, arguments: List<String>): Boolean {
        // First check if composer is available
        if (!isComposerAvailable()) {
            log.warn("Composer is not available, skipping composer command: ${arguments.joinToString(" ")}")
            return false
        }

        val commandLine = GeneralCommandLine("composer")
        commandLine.addParameters(arguments)

        var success = true
        var error = ""

        try {
            val processHandler = OSProcessHandler(commandLine)
            processHandler.addProcessListener(object : ProcessListener {
                override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
                    log.debug("Composer output: ${event.text}")
                }

                override fun processTerminated(event: ProcessEvent) {
                    success = event.exitCode == 0
                    if (!success) {
                        log.warn("Composer command failed with exit code: ${event.exitCode}")
                    }
                }
            })

            processHandler.startNotify()
            processHandler.waitFor()

            return success
        } catch (e: Exception) {
            log.error("Failed to execute composer command: ${e.message}", e)
            return false
        }
    }
}