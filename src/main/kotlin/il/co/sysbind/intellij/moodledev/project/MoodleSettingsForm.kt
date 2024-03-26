package il.co.sysbind.intellij.moodledev.project

import com.intellij.application.options.CodeStyle
import com.intellij.lang.javascript.JavascriptLanguage
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.dsl.builder.*
import com.jetbrains.php.frameworks.PhpFrameworkConfigurable
import com.jetbrains.php.lang.PhpLanguage
import il.co.sysbind.intellij.moodledev.MoodleBundle
import il.co.sysbind.intellij.moodledev.codeStyle.MoodleJavascriptPredefinedCodeStyle
import il.co.sysbind.intellij.moodledev.codeStyle.MoodleLessPredefinedCodeStyle
import il.co.sysbind.intellij.moodledev.codeStyle.MoodlePhpPredefinedCodeStyle
import il.co.sysbind.intellij.moodledev.codeStyle.MoodleScssPredefinedCodeStyle
import il.co.sysbind.intellij.moodledev.util.MoodleCorePathUtil
import org.jetbrains.plugins.less.LESSLanguage
import org.jetbrains.plugins.scss.SCSSLanguage
import javax.swing.JComponent
import javax.swing.JTextField

class MoodleSettingsForm(val project: Project) : PhpFrameworkConfigurable {
    private val settings = project.getService(MoodleProjectSettings::class.java).settings
    private lateinit var pluginEnabled: Cell<JBCheckBox>
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
            moodlePath =
                textFieldWithBrowseButton(
                    MoodleBundle.message("configurable.moodlePath"),
                    project,
                    FileChooserDescriptorFactory.createSingleFolderDescriptor()
                ).bindText(settings::moodlePath).enabledIf(pluginEnabled.selected)
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
        if (settings.moodlePath != "") {
            MoodleCorePathUtil.isMoodlePathValid(settings.moodlePath)
        } else {
            settings.moodlePath = project.guessProjectDir()?.path ?: ""
        }
        if (isBeingUsed) {
            val codeStyleSettings = CodeStyle.getSettings(project)
            MoodlePhpPredefinedCodeStyle().apply(codeStyleSettings, PhpLanguage.INSTANCE)
            MoodleJavascriptPredefinedCodeStyle().apply(codeStyleSettings, JavascriptLanguage.INSTANCE)
            MoodleLessPredefinedCodeStyle().apply(codeStyleSettings, LESSLanguage.INSTANCE)
            MoodleScssPredefinedCodeStyle().apply(codeStyleSettings, SCSSLanguage.INSTANCE)
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
