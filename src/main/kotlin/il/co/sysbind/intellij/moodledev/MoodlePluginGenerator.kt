package il.co.sysbind.intellij.moodledev

import com.intellij.ide.util.projectWizard.SettingsStep
import com.intellij.ide.util.projectWizard.WebProjectTemplate
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.ProjectGeneratorPeer
import com.intellij.psi.PsiManager
import com.intellij.ui.TextAccessor
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.panel
import il.co.sysbind.intellij.moodledev.moodle.Component
import il.co.sysbind.intellij.moodledev.project.MoodleProjectSettings
import java.awt.event.ActionListener
import javax.swing.Icon
import javax.swing.JComponent
import javax.swing.JTextField
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class MoodlePluginGenerator : WebProjectTemplate<MoodlePluginGeneratorSettings>() {
    override fun getName(): String {
        return buildString {
            append("Moodle Project")
        }
    }
    override fun getId(): String {
        return "MoodleDev"
    }

    override fun getDescription(): String {
        return buildString {
            append("Create New Moodle Project")
        }
    }

    override fun getIcon(): Icon {
        return MoodleIcons.LOGO
    }


    override fun createPeer(): ProjectGeneratorPeer<MoodlePluginGeneratorSettings> {
        return MoodlePluginGeneratorPeer()
    }

    override fun generateProject(
        project: Project,
        baseDir: VirtualFile,
        settings: MoodlePluginGeneratorSettings,
        module: Module
    ) {
        val dataService: MoodleProjectSettings = project.getService(MoodleProjectSettings::class.java)
//        dataService.loadState(settings)

        Runnable {
            ApplicationManager.getApplication().runWriteAction {
                PsiManager
                    .getInstance(project)
                    .findDirectory(baseDir) ?: return@runWriteAction
            }
        }
    }

    private inner class MoodlePluginGeneratorPeer : ProjectGeneratorPeer<MoodlePluginGeneratorSettings> {
        private var myContentRoot: TextAccessor? = null
        private lateinit var myPluginType : Cell<ComboBox<String>>
        override fun getComponent(
            myLocationField: TextFieldWithBrowseButton,
            checkValid: Runnable
        ): JComponent {
            val moodleTree = Component()

            val myPanel = panel {
                row("Plugin Type:") {
                    val pluginTypeComboBox = comboBox(moodleTree.getPluginTypes().toList())
                    pluginTypeComboBox.component.addActionListener(ActionListener {
                        // Call checkValid whenever plugin type changes
                        checkValid.run()
                    })
                }
                row("Plugin Name:") {
                    val pluginNameTextField = JTextField()
                    pluginNameTextField.document.addDocumentListener(object : DocumentListener {
                        override fun insertUpdate(e: DocumentEvent?) = updateLocationField(pluginNameTextField, moodleTree)
                        override fun removeUpdate(e: DocumentEvent?) = updateLocationField(pluginNameTextField, moodleTree)
                        override fun changedUpdate(e: DocumentEvent?) = updateLocationField(pluginNameTextField, moodleTree)
                    })
                }
            }

            myLocationField.textField.document.addDocumentListener(object : DocumentListener {
                override fun insertUpdate(e: DocumentEvent?) = checkValid.run()
                override fun removeUpdate(e: DocumentEvent?) = checkValid.run()
                override fun changedUpdate(e: DocumentEvent?) = checkValid.run()
            })

            return myPanel
        }

        private fun updateLocationField(textField: JTextField, moodleTree: Component) {
            if (myContentRoot != null && myPluginType.component.selectedItem != null) {
                val pluginType = myPluginType.component.selectedItem.toString()
                val updatedPath = myContentRoot!!.text.substringBeforeLast('/') +
                        "/" + moodleTree.getPluginPath(pluginType) + "/" + textField.text
                myContentRoot!!.text = updatedPath
            }
        }

        override fun buildUI(settingsStep: SettingsStep) {
            val field = settingsStep.moduleNameLocationSettings
            if (field != null) {
                myContentRoot = object : TextAccessor {
                    override fun setText(text: String) {
                        field.moduleContentRoot = text
                    }

                    override fun getText(): String {
                        return field.moduleContentRoot
                    }
                }
            }
        }



        override fun getSettings(): MoodlePluginGeneratorSettings {
            return MoodlePluginGeneratorSettings("pluginname", "antivirus")
        }

        override fun validate(): ValidationInfo? {
            TODO("Not yet implemented")
        }

        override fun isBackgroundJobRunning(): Boolean {
            TODO("Not yet implemented")
        }
    }
}
