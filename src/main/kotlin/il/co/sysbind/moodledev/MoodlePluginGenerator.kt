package il.co.sysbind.moodledev

import com.intellij.ide.util.projectWizard.SettingsStep
import com.intellij.ide.util.projectWizard.WebProjectTemplate
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.ProjectGeneratorPeer
import com.intellij.psi.PsiManager
import com.intellij.ui.TextAccessor
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.whenTextChangedFromUi
import il.co.sysbind.moodledev.moodle.Component
import il.co.sysbind.moodledev.project.MoodleProjectSettings
import javax.swing.Icon
import javax.swing.JComponent

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
        val plugin = MoodlePluginGeneratorSettings()
        private lateinit var myPluginType : Cell<ComboBox<String>>
        override fun getComponent(): JComponent {
            val moodleTree = Component()
            val myPanel = panel {
                row("Plugin Type:"){
                    myPluginType = comboBox(moodleTree.getPluginTypes().toList())
                }
                row("Plugin Name:") {
                    textField().whenTextChangedFromUi {
                        myContentRoot!!.text = myContentRoot!!.text.substringBeforeLast('/') +
                                "/" + moodleTree.getPluginPath(myPluginType.component.item) + "/" + it
                    }
                }
            }

            return myPanel
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
