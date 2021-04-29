package il.co.sysbind.intellij.moodledev.project

import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.layout.panel
import com.jetbrains.php.frameworks.PhpFrameworkConfigurable
import il.co.sysbind.intellij.moodledev.MoodleBundle
import javax.swing.JComponent

class MoodleSettingsForm(private val project: Project) : PhpFrameworkConfigurable {
    private val settings = project.getService(MoodleProjectSettings::class.java).settings

    @Suppress("DialogTitleCapitalization")
    private val pluginEnabled: JBCheckBox = JBCheckBox(
        MoodleBundle.getMessage("configurable.enabled"),
        settings?.pluginEnabled == true
    )

    override fun createComponent(): JComponent {
        return panel {
            row {
                pluginEnabled()
            }
        }
    }

    override fun isModified(): Boolean {
        return settings?.pluginEnabled != pluginEnabled.isSelected
    }

    override fun apply() {
        settings?.pluginEnabled = pluginEnabled.isSelected
    }

    override fun getDisplayName(): String {
        return MoodleBundle.getMessage("configurable.name")
    }

    override fun getId(): String {
        return "moodle.project.MoodleSettingsForm"
    }

    override fun isBeingUsed(): Boolean {
        return pluginEnabled.isSelected
    }
}
