package il.co.sysbind.intellij.moodledev

import com.intellij.ide.util.projectWizard.WebProjectTemplate
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import il.co.sysbind.intellij.moodledev.project.MoodleProjectSettings
import javax.swing.Icon

class MoodlePluginGenerator : WebProjectTemplate<MoodlePluginGeneratorSettings>() {
    override fun getName(): String {
        return buildString {
            append("Moodle Project")
        }
    }

    override fun getDescription(): String {
        return buildString {
            append("Create New Moodle Project")
        }
    }

    override fun getIcon(): Icon {
        return MoodleIcons.LOGO
    }



    override fun generateProject(
        project: Project,
        baseDir: VirtualFile,
        settings: MoodlePluginGeneratorSettings,
        module: Module
    ) {
        var dataService: MoodleProjectSettings = project.getService(MoodleProjectSettings::class.java)
        dataService.loadState(settings.getMoodleSettings())

        Runnable {
            ApplicationManager.getApplication().runWriteAction {
                PsiManager
                    .getInstance(project)
                    .findDirectory(baseDir) ?: return@runWriteAction
            }
        }
    }
}
