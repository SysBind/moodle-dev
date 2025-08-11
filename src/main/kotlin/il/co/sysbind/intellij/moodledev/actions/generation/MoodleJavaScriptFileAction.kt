package il.co.sysbind.intellij.moodledev.actions.generation

import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.fileTemplates.FileTemplateUtil
import com.intellij.lang.javascript.JavaScriptFileType
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import il.co.sysbind.intellij.moodledev.MoodleBundle
import il.co.sysbind.intellij.moodledev.project.MoodleProjectSettings
import il.co.sysbind.intellij.moodledev.util.MoodleCorePathUtil


class MoodleJavaScriptFileAction: CreateFileFromTemplateAction(CAPTION, "", JavaScriptFileType.icon)
    , DumbAware {

    private companion object {
        private val CAPTION = MoodleBundle.getMessage("action.javascript.caption")
    }

    override fun buildDialog(project: Project, directory: PsiDirectory, builder: CreateFileFromTemplateDialog.Builder) {
        builder.setTitle(PhpCreateFileAction.CAPTION)
            .addKind(MoodleBundle.getMessage("action.javascript.empty.file"), JavaScriptFileType.icon, MoodleBundle.getMessage("action.javascript.template"))
    }

    override fun getActionName(directory: PsiDirectory?, newName: String, templateName: String?): String = CAPTION

    override fun createFileFromTemplate(name: String, template: FileTemplate, dir: PsiDirectory): PsiFile {
        val project = dir.project
        val settings = project.getService(MoodleProjectSettings::class.java).settings

        // Create properties for the template
        val templateManager = FileTemplateManager.getInstance(project)
        val props = templateManager.defaultProperties
        props["USER_NAME"] = settings.userName
        props["USER_EMAIL"] = settings.userEmail

        // Extract PLUGIN_NAME from the closest version.php
        props["MODULE_DIR"] = MoodleCorePathUtil.getModuleName(dir, "js")

        return FileTemplateUtil.createFromTemplate(template, name, props, dir).containingFile
    }
}
