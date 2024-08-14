package il.co.sysbind.moodledev

import com.intellij.ide.projectView.actions.MarkRootActionBase
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.util.Ref
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.DirectoryProjectConfigurator
import il.co.sysbind.moodledev.util.MoodleCorePathUtil

class MoodleProjectConfiguration : DirectoryProjectConfigurator {
    override fun configureProject(
        project: Project,
        baseDir: VirtualFile,
        moduleRef: Ref<Module>,
        isProjectCreatedWithWizard: Boolean
    ) {
        val module = moduleRef.get() ?: return

        val moodleversion = MoodleCorePathUtil.findMoodleVersion(baseDir)
        val model = ModuleRootManager.getInstance(module).modifiableModel
        val entry = MarkRootActionBase.findContentEntry(model, baseDir)
        if (entry != null && moodleversion != null) {
            ApplicationManager.getApplication().runWriteAction { model.commit() }
            project.save()
        } else {
            model.dispose()
        }
    }
}
