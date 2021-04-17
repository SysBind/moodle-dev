package il.co.sysbind.intellij.moodledev.listeners

import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import il.co.sysbind.intellij.moodledev.project.MoodleProjectSettings

internal class MoodleManagerListener : ProjectManagerListener {

    override fun projectOpened(project: Project) {
        project.getService(MoodleProjectSettings::class.java)
    }
}
