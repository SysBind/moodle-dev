package il.co.sysbind.intellij.moodledev.actions.generation

import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.fileTemplates.FileTemplateUtil
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import icons.HandlebarsIcons
import il.co.sysbind.intellij.moodledev.project.MoodleProjectSettings
import il.co.sysbind.intellij.moodledev.util.MoodleCorePathUtil

class MoodleMustacheTemplateAction : CreateFileFromTemplateAction(CAPTION, "", HandlebarsIcons.Handlebars_icon),
    DumbAware {
    override fun getActionName(directory: PsiDirectory?, newName: String, templateName: String?): String = CAPTION

    override fun isAvailable(dataContext: DataContext?): Boolean {
        if (!super.isAvailable(dataContext)) return false
        val project = CommonDataKeys.PROJECT.getData(dataContext!!) ?: return false
        CommonDataKeys.VIRTUAL_FILE.getData(dataContext) ?: return false
        val moodle = project.getService(MoodleProjectSettings::class.java).settings
        return moodle.pluginEnabled
    }

    override fun buildDialog(project: Project, directory: PsiDirectory, builder: CreateFileFromTemplateDialog.Builder) {
        builder.setTitle(PhpCreateFileAction.CAPTION)
            .addKind("Empty file", HandlebarsIcons.Handlebars_icon, "Moodle Mustache Template")
    }

    override fun createFileFromTemplate(name: String, template: FileTemplate, dir: PsiDirectory): PsiFile {
        val project = dir.project
        val settings = project.getService(MoodleProjectSettings::class.java).settings

        // Create properties for the template
        val templateManager = FileTemplateManager.getInstance(project)
        val props = templateManager.defaultProperties

        // Extract PLUGIN_NAME from the closest version.php
        props["PLUGIN_NAME"] = MoodleCorePathUtil.getPluginName(dir)

        return FileTemplateUtil.createFromTemplate(template, name, props, dir).containingFile
    }

    private companion object {
        private const val CAPTION = "Moodle Mustache Template"
    }

}