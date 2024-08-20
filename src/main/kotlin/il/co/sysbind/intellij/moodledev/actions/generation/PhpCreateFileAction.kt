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
import com.jetbrains.php.PhpIcons
import il.co.sysbind.intellij.moodledev.project.MoodleProjectSettings
import il.co.sysbind.intellij.moodledev.util.MoodleCorePathUtil

class PhpCreateFileAction : CreateFileFromTemplateAction(CAPTION, "", PhpIcons.PHP_FILE), DumbAware {
    override fun getActionName(directory: PsiDirectory?, newName: String, templateName: String?): String = CAPTION

    override fun isAvailable(dataContext: DataContext?): Boolean {
        if (!super.isAvailable(dataContext)) return false
        val project = CommonDataKeys.PROJECT.getData(dataContext!!) ?: return false
        CommonDataKeys.VIRTUAL_FILE.getData(dataContext) ?: return false
        val moodle = project.getService(MoodleProjectSettings::class.java).settings
        return moodle.pluginEnabled
    }
    override fun buildDialog(project: Project, directory: PsiDirectory, builder: CreateFileFromTemplateDialog.Builder) {
        builder.setTitle(CAPTION)
            .addKind("Empty file", PhpIcons.PHP_FILE, "Moodle PHP File")
    }

    override fun createFileFromTemplate(name: String, template: FileTemplate, dir: PsiDirectory): PsiFile {
        val project = dir.project
        val settings = project.getService(MoodleProjectSettings::class.java).settings

        // Create properties for the template
        val templateManager = FileTemplateManager.getInstance(project)
        val props = templateManager.defaultProperties
        props["USER_NAME"] = settings.userName
        props["USER_EMAIL"] = settings.userEmail

        // Extract PLUGIN_NAME from the closest version.php
        props["PLUGIN_NAME"] = MoodleCorePathUtil.getPluginName(dir)

        return FileTemplateUtil.createFromTemplate(template, name, props, dir).containingFile
    }

    companion object {
        internal const val CAPTION = "Moodle PHP File"
    }
}
