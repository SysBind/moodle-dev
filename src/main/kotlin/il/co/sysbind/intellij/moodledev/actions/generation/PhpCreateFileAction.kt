package il.co.sysbind.intellij.moodledev.actions.generation

import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import icons.PhpIcons
import icons.PhpIcons.PhpIcon
import il.co.sysbind.intellij.moodledev.project.MoodleProjectSettings

class PhpCreateFileAction : CreateFileFromTemplateAction(CAPTION, "", PhpIcon), DumbAware {
    override fun getActionName(directory: PsiDirectory?, newName: String, templateName: String?): String = CAPTION

    override fun isAvailable(dataContext: DataContext?): Boolean {
        if (!super.isAvailable(dataContext)) return false
        val project = CommonDataKeys.PROJECT.getData(dataContext!!) ?: return false
        val vFile = CommonDataKeys.VIRTUAL_FILE.getData(dataContext) ?: return false
        val moodle = project.getService(MoodleProjectSettings::class.java).settings
        return moodle?.pluginEnabled ?: false
    }
    override fun buildDialog(project: Project, directory: PsiDirectory, builder: CreateFileFromTemplateDialog.Builder) {
        builder.setTitle(CAPTION)
            .addKind("Empty file", PhpIcons.PhpIcon, "Moodle PHP File")
    }

    private companion object {
        private const val CAPTION = "Moodle PHP File"
    }
}
