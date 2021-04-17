package il.co.sysbind.intellij.moodledev.project

import com.intellij.openapi.project.Project
import com.jetbrains.php.frameworks.PhpFrameworkConfigurable
import com.jetbrains.php.frameworks.PhpFrameworkConfigurableProvider
import il.co.sysbind.intellij.moodledev.MoodleBundle

class MoodleConfigurableProvider : PhpFrameworkConfigurableProvider {
    override fun getName(): String {
        return MoodleBundle.getMessage("name")
    }

    override fun createConfigurable(project: Project): PhpFrameworkConfigurable {
        return MoodleSettingsForm(project)
    }
}
