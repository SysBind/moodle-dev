package il.co.sysbind.intellij.moodledev.project

import com.intellij.application.options.codeStyle.CodeStyleSchemesModel
import com.intellij.openapi.application.ApplicationBundle
import com.intellij.openapi.module.ResourceFileUtil
import com.intellij.openapi.options.SchemeFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.codeStyle.CodeStyleScheme
import com.intellij.psi.impl.source.codeStyle.CodeStyleSchemeImpl
import com.intellij.psi.impl.source.codeStyle.CodeStyleSchemeXmlImporter
import com.intellij.psi.search.GlobalSearchScope
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
    private val moodlePath: TextFieldWithBrowseButton = TextFieldWithBrowseButton()

    override fun createComponent(): JComponent? {
        return panel {
            row {
                pluginEnabled()
            }
//            row(MoodleBundle.getMessage("configurable.moodlePath")) {
//                moodlePath()
//            }
        }
    }

    override fun isModified(): Boolean {
        return settings?.pluginEnabled != pluginEnabled.isSelected
    }

    override fun apply() {
        settings?.pluginEnabled = pluginEnabled.isSelected
        settings?.moodlePath = moodlePath.text
//        loadMoodleDefaultSettings()
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

    fun loadMoodleDefaultSettings() {
        importCodeStyleScheme()
    }

    fun importCodeStyleScheme() {
        var codeStyleSchemeFile = ResourceFileUtil.findResourceFileInScope(
            "codeScheme/Moodle.xml",
            project, GlobalSearchScope.allScope(project)
        )
        var importer = CodeStyleSchemeXmlImporter()
        CodeStyleSchemeImpl(MoodleBundle.getMessage("codeScheme.name"), false, null)
        val schemeCreator = SchemeCreator(project)
        if (codeStyleSchemeFile is VirtualFile) {
            importer.importScheme(
                project,
                codeStyleSchemeFile,
                schemeCreator.createNewScheme(MoodleBundle.getMessage("codeScheme.name")),
                schemeCreator
            )
        }
    }

    private class SchemeCreator(p0: Project) : SchemeFactory<CodeStyleScheme?> {
        var project = p0
        var isSchemeWasCreated = false
            private set

        override fun createNewScheme(targetName: String?): CodeStyleScheme {
            var targetName = targetName
            isSchemeWasCreated = true
            if (targetName == null) targetName = ApplicationBundle.message("code.style.scheme.import.unnamed")
            val newScheme: CodeStyleScheme = CodeStyleSchemesModel(project).createNewScheme(targetName, null)
            CodeStyleSchemesModel(project).addScheme(newScheme, true)
            return newScheme
        }
    }
}
