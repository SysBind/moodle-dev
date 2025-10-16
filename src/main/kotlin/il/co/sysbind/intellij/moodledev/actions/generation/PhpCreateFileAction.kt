package il.co.sysbind.intellij.moodledev.actions.generation

import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.fileTemplates.FileTemplateUtil
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.jetbrains.php.PhpIcons
import il.co.sysbind.intellij.moodledev.MoodleBundle
import il.co.sysbind.intellij.moodledev.project.MoodleProjectSettings
import il.co.sysbind.intellij.moodledev.util.MoodleCorePathUtil

class PhpCreateFileAction : CreateFileFromTemplateAction(CAPTION, "", PhpIcons.PHP_FILE), DumbAware {
    override fun getActionName(directory: PsiDirectory?, newName: String, templateName: String?): String = CAPTION

    override fun buildDialog(project: Project, directory: PsiDirectory, builder: CreateFileFromTemplateDialog.Builder) {
        builder.setTitle(CAPTION)
            .addKind(MoodleBundle.getMessage("action.php.empty.file"), PhpIcons.PHP_FILE, MoodleBundle.getMessage("action.php.template"))
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
        internal val CAPTION = MoodleBundle.getMessage("action.php.caption")
    }
}
