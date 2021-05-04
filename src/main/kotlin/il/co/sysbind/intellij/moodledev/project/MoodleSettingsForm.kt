package il.co.sysbind.intellij.moodledev.project

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.TextBrowseFolderListener
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.layout.panel
import com.jetbrains.php.frameworks.PhpFrameworkConfigurable
import il.co.sysbind.intellij.moodledev.MoodleBundle
import il.co.sysbind.intellij.moodledev.util.MoodleCorePathUtil
import java.awt.event.ActionListener
import javax.swing.JComponent

class MoodleSettingsForm(project: Project) : PhpFrameworkConfigurable {
    private val settings = project.getService(MoodleProjectSettings::class.java).settings

    @Suppress("DialogTitleCapitalization")
    private val pluginEnabled: JBCheckBox = JBCheckBox(
        MoodleBundle.getMessage("configurable.enabled"),
        settings?.pluginEnabled == true
    )

    private val moodlePath = TextFieldWithBrowseButton()
    private val browserListener = TextBrowseFolderListener(FileChooserDescriptorFactory.createSingleFileDescriptor())

    init {
        moodlePath.addBrowseFolderListener(browserListener)
        moodlePath.toolTipText = MoodleBundle.message("configurable.moodlePath.description")
        moodlePath.text = settings?.moodlePath ?: "."
        pluginEnabled.addActionListener(ActionListener { e -> refreshStatus(pluginEnabled.isSelected) })
    }
    override fun createComponent(): JComponent {
        return panel {
            row {
                pluginEnabled()
            }
            row {
                label(MoodleBundle.message("configurable.moodlePath"))
                moodlePath()
            }
        }
    }

    override fun isModified(): Boolean {
        return (settings?.pluginEnabled != pluginEnabled.isSelected) || (settings?.moodlePath != moodlePath.text)

    }

    override fun apply() {
        settings?.pluginEnabled = pluginEnabled.isSelected
        settings?.moodlePath = moodlePath.text
        var pathUtil = MoodleCorePathUtil()
        if (settings?.moodlePath != null) {
            pathUtil.isMoodlePathValid(settings.moodlePath)
        }
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

    private fun refreshStatus(isEnabled: Boolean) {
        moodlePath.isEnabled = isEnabled
    }
}
