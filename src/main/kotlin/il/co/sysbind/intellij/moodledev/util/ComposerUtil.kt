package il.co.sysbind.intellij.moodledev.util

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessAdapter
import com.intellij.execution.process.ProcessEvent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key

object ComposerUtil {
    private val LOG = Logger.getInstance(ComposerUtil::class.java)
    private var composerGlobalDir: String? = null

    fun getComposerGlobalDir(): String? {
        if (composerGlobalDir != null) {
            return composerGlobalDir
        }

        try {
            val commandLine = GeneralCommandLine("composer")
            commandLine.addParameters(listOf("config", "--global", "home"))

            val output = StringBuilder()
            val processHandler = OSProcessHandler(commandLine)
            processHandler.addProcessListener(object : ProcessAdapter() {
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
                composerGlobalDir = output.toString()
                return composerGlobalDir
            }
        } catch (e: Exception) {
            LOG.error("Failed to get composer global directory: ${e.message}", e)
        }
        return null
    }

    fun getPhpcsPath(): String? {
        return getComposerGlobalDir()?.let { "$it/vendor/bin/phpcs" }
    }

    fun getPhpcbfPath(): String? {
        return getComposerGlobalDir()?.let { "$it/vendor/bin/phpcbf" }
    }

    fun runComposerInstall(project: Project, directory: String): Boolean {
        val workDir = java.io.File(directory)
        if (!workDir.exists() || !workDir.isDirectory) {
            LOG.debug("Directory does not exist or is not a directory: $directory")
            return false
        }

        try {
            val commandLine = GeneralCommandLine("composer")
            commandLine.addParameter("install")
            commandLine.workDirectory = workDir

            val processHandler = OSProcessHandler(commandLine)
            processHandler.addProcessListener(object : ProcessAdapter() {
                override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
                    LOG.debug("Composer install output: ${event.text}")
                }

                override fun processTerminated(event: ProcessEvent) {
                    if (event.exitCode != 0) {
                        LOG.warn("Composer install failed with exit code: ${event.exitCode}")
                    }
                }
            })

            processHandler.startNotify()
            processHandler.waitFor()

            return processHandler.exitCode == 0
        } catch (e: Exception) {
            LOG.error("Failed to run composer install: ${e.message}", e)
            return false
        }
    }

    fun setupMoodleCs(project: Project): Boolean {
        try {
            // Set minimum-stability to dev
            if (!executeComposerCommand(project, listOf("global", "config", "minimum-stability", "dev"))) {
                LOG.error("Failed to set composer minimum-stability to dev")
                return false
            }

            // Install moodlehq/moodle-cs
            if (!executeComposerCommand(project, listOf("global", "require", "moodlehq/moodle-cs"))) {
                LOG.error("Failed to install moodlehq/moodle-cs")
                return false
            }

            return true
        } catch (e: Exception) {
            LOG.error("Error setting up Moodle CS: ${e.message}", e)
            return false
        }
    }

    private fun executeComposerCommand(project: Project, arguments: List<String>): Boolean {
        val commandLine = GeneralCommandLine("composer")
        commandLine.addParameters(arguments)

        var success = true
        var error = ""

        try {
            val processHandler = OSProcessHandler(commandLine)
            processHandler.addProcessListener(object : ProcessAdapter() {
                override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
                    LOG.debug("Composer output: ${event.text}")
                }

                override fun processTerminated(event: ProcessEvent) {
                    success = event.exitCode == 0
                    if (!success) {
                        LOG.warn("Composer command failed with exit code: ${event.exitCode}")
                    }
                }
            })

            processHandler.startNotify()
            processHandler.waitFor()

            return success
        } catch (e: Exception) {
            LOG.error("Failed to execute composer command: ${e.message}", e)
            return false
        }
    }
}
