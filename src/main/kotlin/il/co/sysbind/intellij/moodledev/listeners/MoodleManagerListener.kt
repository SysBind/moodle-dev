package il.co.sysbind.intellij.moodledev.listeners


import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import il.co.sysbind.intellij.moodledev.project.MoodleProjectSettings

internal class MoodleManagerListener :  StartupActivity{

    override fun runActivity(project: Project) {
        project.getService(MoodleProjectSettings::class.java)
    }
}
