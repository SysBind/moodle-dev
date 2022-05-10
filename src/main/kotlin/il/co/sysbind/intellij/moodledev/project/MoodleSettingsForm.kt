package il.co.sysbind.intellij.moodledev.project

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.dsl.builder.*
import com.jetbrains.php.frameworks.PhpFrameworkConfigurable
import il.co.sysbind.intellij.moodledev.MoodleBundle
import il.co.sysbind.intellij.moodledev.util.MoodleCorePathUtil
import javax.swing.JComponent

class MoodleSettingsForm(val project: Project) : PhpFrameworkConfigurable {
    private val settings = project.getService(MoodleProjectSettings::class.java).settings
    private lateinit var pluginEnabled: Cell<JBCheckBox>
    private lateinit var moodlePath: Cell<TextFieldWithBrowseButton>
    @Suppress("DialogTitleCapitalization")
    private var panel = panel {
        row {
            pluginEnabled =
                checkBox(MoodleBundle.getMessage("configurable.enabled"))
                    .bindSelected(settings::pluginEnabled)
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
        val pathUtil = MoodleCorePathUtil()
        if (settings.moodlePath != "") {
            pathUtil.isMoodlePathValid(settings.moodlePath)
        } else {
            settings.moodlePath = project.guessProjectDir()?.path ?: ""
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
