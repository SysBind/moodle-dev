package il.co.sysbind.intellij.moodledev.project

import com.intellij.application.options.CodeStyle
import com.intellij.lang.javascript.JavascriptLanguage
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.dsl.builder.*
import com.jetbrains.php.config.interpreters.PhpInterpretersManagerImpl
import com.jetbrains.php.config.library.PhpIncludePathManager
import com.jetbrains.php.frameworks.PhpFrameworkConfigurable
import com.jetbrains.php.lang.PhpLanguage
import com.jetbrains.php.tools.quality.phpcs.PhpCSConfigurationManager
import il.co.sysbind.intellij.moodledev.MoodleBundle
import il.co.sysbind.intellij.moodledev.codeStyle.MoodleJavascriptPredefinedCodeStyle
import il.co.sysbind.intellij.moodledev.codeStyle.MoodleLessPredefinedCodeStyle
import il.co.sysbind.intellij.moodledev.codeStyle.MoodlePhpPredefinedCodeStyle
import il.co.sysbind.intellij.moodledev.codeStyle.MoodleScssPredefinedCodeStyle
import il.co.sysbind.intellij.moodledev.util.ComposerUtil
import il.co.sysbind.intellij.moodledev.util.MoodleCorePathUtil
import org.jetbrains.plugins.less.LESSLanguage
import org.jetbrains.plugins.scss.SCSSLanguage
import javax.swing.JComponent
import javax.swing.JTextField

class MoodleSettingsForm(val project: Project) : PhpFrameworkConfigurable {
    private val LOG = Logger.getInstance(MoodleSettingsForm::class.java)
    private val settings = project.getService(MoodleProjectSettings::class.java).settings
    lateinit var pluginEnabled: Cell<JBCheckBox>
        private set
    private lateinit var moodlePath: Cell<TextFieldWithBrowseButton>
    private lateinit var userName: Cell<JTextField>
    private lateinit var userEmail: Cell<JTextField>
    @Suppress("DialogTitleCapitalization")
    private var panel = panel {
        row {
            pluginEnabled =
                checkBox(MoodleBundle.getMessage("configurable.enabled"))
                    .bindSelected(settings::pluginEnabled)
        }

        row(MoodleBundle.message("configurable.username")) {
            userName = textField().bindText(settings::userName)
        }

        row(MoodleBundle.message("configurable.useremail")) {
            userEmail = textField().bindText(settings::userEmail)
        }

        row("Moodle project directory:") {
            moodlePath = textFieldWithBrowseButton(
                FileChooserDescriptorFactory.createSingleFolderDescriptor()
                    .withTitle(MoodleBundle.message("configurable.moodlePath")),
                project
            ).bindText(settings::moodlePath)
             .enabledIf(pluginEnabled.selected)
        }
    }

    override fun createComponent(): JComponent {
        return panel
    }

    override fun isModified(): Boolean {
        return panel.isModified()
    }

    override fun apply() {
        settings.moodlePath = moodlePath.component.text
        settings.pluginEnabled = pluginEnabled.component.isSelected
        settings.userName = userName.component.text
        settings.userEmail = userEmail.component.text

        // Configure PHP_Codesniffer if plugin is enabled
        if (settings.pluginEnabled) {
            val detectedPhpcsPath = ComposerUtil.getPhpcsPath()
            val detectedPhpcbfPath = ComposerUtil.getPhpcbfPath()

            if (detectedPhpcsPath != null && detectedPhpcbfPath != null) {
                // Show notification for PHP_Codesniffer configuration
                NotificationGroupManager.getInstance()
                    .getNotificationGroup("Moodle.Notifications")
                    .createNotification(
                        "PHP_Codesniffer Configuration",
                        """
                        Moodle Code Sniffer tools have been detected at:
                        PHPCS: $detectedPhpcsPath
                        PHPCBF: $detectedPhpcbfPath

                        Would you like to configure PHP_Codesniffer settings?
                        """.trimIndent(),
                        NotificationType.INFORMATION
                    )
                    .addAction(NotificationAction.createSimple("Auto Settings") {
                        try {
                            ApplicationManager.getApplication().runWriteAction {
                                // Configure PHP_Codesniffer settings
                                val phpInterpreterManager = PhpInterpretersManagerImpl.getInstance(project)
                                val manager = PhpCSConfigurationManager.getInstance(project)
                                val interprter = phpInterpreterManager.findInterpreter("System PHP")
                                val configuration = manager.getOrCreateConfigurationByInterpreter(interprter, true)

                                configuration.phpCodeBeautifierPath = detectedPhpcbfPath
                                configuration.toolPath = detectedPhpcsPath

                                // Try to set the configuration for phpcs_by_interpreter
                                try {
                                    manager.markAndSetNewSettings(listOf(configuration))
                                    LOG.info("Successfully set tool path for phpcs_by_interpreter")
                                } catch (e: Exception) {
                                    LOG.warn("Could not set tool path for phpcs_by_interpreter: ${e.message}")
                                }

                                // Show success notification
                                NotificationGroupManager.getInstance()
                                    .getNotificationGroup("Moodle.Notifications")
                                    .createNotification(
                                        "PHP_Codesniffer Configuration",
                                        """
                                        Successfully configured PHP_Codesniffer for System PHP interpreter:
                                        - PHPCS Path: $detectedPhpcsPath (direct path to bin file)
                                        - PHPCBF Path: $detectedPhpcbfPath
                                        - Coding Standard: moodle
                                        - Show Sniffs: enabled
                                        - Highlight Level: WARNING
                                        """.trimIndent(),
                                        NotificationType.INFORMATION
                                    )
                                    .notify(project)

                                LOG.info("Successfully configured PHP_Codesniffer automatically")
                            }
                        } catch (e: Exception) {
                            LOG.error("Failed to configure PHP_Codesniffer automatically: ${e.message}", e)
                        }
                    })
                    .addAction(NotificationAction.createSimple("Open Settings") {
                        ShowSettingsUtil.getInstance().showSettingsDialog(
                            project,
                            "Languages & Frameworks | PHP | Quality Tools | PHP_CodeSniffer"
                        )
                        LOG.info("Opened PHP_Codesniffer settings")
                    })
                    .addAction(NotificationAction.createSimple("Copy Paths") {
                        val content = """
                            PHPCS Path: $detectedPhpcsPath
                            PHPCBF Path: $detectedPhpcbfPath
                        """.trimIndent()
                        val clipboard = com.intellij.openapi.ide.CopyPasteManager.getInstance()
                        clipboard.setContents(java.awt.datatransfer.StringSelection(content))
                        LOG.info("Copied PHP_Codesniffer paths to clipboard")
                    })
                    .addAction(NotificationAction.createSimple("Ignore") {
                        LOG.info("User chose to ignore PHP_Codesniffer configuration")
                    })
                    .notify(project)

                LOG.info("Showed PHP_Codesniffer configuration options to user")
            } else {
                LOG.warn("Failed to get PHP_Codesniffer paths from composer global directory")
            }
        }
        if (settings.moodlePath != "") {
            MoodleCorePathUtil.isMoodlePathValid(settings.moodlePath)
        } else {
            settings.moodlePath = project.guessProjectDir()?.path ?: ""
        }
        val moodlePathStr = settings.moodlePath
        if (isBeingUsed) {
            // Setup Moodle CS via Composer
            if (!ComposerUtil.setupMoodleCs(project)) {
                LOG.warn("Failed to setup Moodle CS via Composer")
            }

            val codeStyleSettings = CodeStyle.getSettings(project)
            MoodlePhpPredefinedCodeStyle().apply(codeStyleSettings, PhpLanguage.INSTANCE)
            MoodleJavascriptPredefinedCodeStyle().apply(codeStyleSettings, JavascriptLanguage)
            MoodleLessPredefinedCodeStyle().apply(codeStyleSettings, LESSLanguage.INSTANCE)
            MoodleScssPredefinedCodeStyle().apply(codeStyleSettings, SCSSLanguage.INSTANCE)
            if (settings.moodlePath.isNotBlank()) {
                // Get the PhpIncludePathManager for the current project
                val includePathManager = PhpIncludePathManager.getInstance(project)

                // Add the Moodle path to the PHP include paths
                val includePathList = includePathManager.includePath

                // Compare the input Moodle path with the project root directory
                val projectRootPath = project.basePath ?: ""

                if (moodlePathStr == projectRootPath) {
                    // If paths are equal, run composer install
                    if (!ComposerUtil.runComposerInstall(project, moodlePathStr)) {
                        LOG.warn("Failed to run composer install in $moodlePathStr")
                    }
                } else if (!includePathList.contains(moodlePathStr)) {
                    // If paths are different and the Moodle path is not in include paths, add it
                    includePathList.add(moodlePathStr)
                    includePathManager.includePath = includePathList
                }
            }
        } else {
            if (moodlePathStr.isNotBlank() && moodlePathStr != project.guessProjectDir()?.path) {
                val includePathManager = PhpIncludePathManager.getInstance(project)
                val includePathList = includePathManager.includePath
                if (includePathList.contains(moodlePathStr)) {
                    includePathList.remove(moodlePathStr)
                    includePathManager.includePath = includePathList
                }
            }
        }
    }

    override fun getDisplayName(): String {
        return MoodleBundle.getMessage("configurable.name")
    }

    override fun getId(): String {
        return "moodle.project.MoodleSettingsForm"
    }

    override fun isBeingUsed(): Boolean {
        return settings.pluginEnabled
    }
}
