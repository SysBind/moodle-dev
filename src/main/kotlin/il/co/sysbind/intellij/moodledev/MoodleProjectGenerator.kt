package il.co.sysbind.intellij.moodledev

import com.intellij.ide.IdeBundle
import com.intellij.ide.util.projectWizard.WebProjectTemplate
import com.intellij.openapi.GitRepositoryInitializer
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.module.Module
import com.intellij.openapi.progress.runBackgroundableTask
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import il.co.sysbind.intellij.moodledev.project.MoodleProjectSettings

class MoodleProjectGenerator : WebProjectTemplate<MoodleProjectGeneratorSettings>() {
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

    override fun generateProject(
        project: Project,
        baseDir: VirtualFile,
        settings: MoodleProjectGeneratorSettings,
        module: Module
    ) {
        var dataService: MoodleProjectSettings = project.getService(MoodleProjectSettings::class.java)
        dataService.loadState(settings.getMoodleSettings())

        val generate =
            Runnable {
                ApplicationManager.getApplication().runWriteAction {
                    val baseDirElement = PsiManager
                        .getInstance(project)
                        .findDirectory(baseDir) ?: return@runWriteAction
                    runBackgroundableTask(IdeBundle.message("progress.title.creating.git.repository"), project) {
                        GitRepositoryInitializer.getInstance()!!.initRepository(project, baseDir)
//                        git()
                    }
//                    generateComposerJson(project, baseDirElement, settings)
//                    generateRegistrationPhp(project, baseDirElement, settings)
//                    generateModuleXml(project, baseDirElement, settings)
//                    ConfigurationManager
//                        .getInstance()
//                        .refreshIncludePaths(dataService.state, project)
                }
            }
    }
}