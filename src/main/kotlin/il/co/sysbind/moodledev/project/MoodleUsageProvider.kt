package il.co.sysbind.moodledev.project

import com.intellij.openapi.project.Project
import com.jetbrains.php.frameworks.PhpFrameworkUsageProvider
import il.co.sysbind.moodledev.MoodleBundle

class MoodleUsageProvider : PhpFrameworkUsageProvider {
    override fun getName(): String {
        return MoodleBundle.getMessage("name")
    }

    override fun isEnabled(p0: Project): Boolean {
        return p0.getService(MoodleProjectSettings::class.java).state.pluginEnabled
    }
}
