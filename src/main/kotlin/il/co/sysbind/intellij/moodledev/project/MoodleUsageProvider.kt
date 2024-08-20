package il.co.sysbind.intellij.moodledev.project

import com.intellij.openapi.project.Project
import com.jetbrains.php.frameworks.PhpFrameworkUsageProvider
import il.co.sysbind.intellij.moodledev.MoodleBundle

class MoodleUsageProvider : PhpFrameworkUsageProvider {
    override fun getName(): String {
        return MoodleBundle.getMessage("name")
    }

    override fun isEnabled(p0: Project): Boolean {
        return p0.getService(MoodleProjectSettings::class.java).state.pluginEnabled
    }
}
