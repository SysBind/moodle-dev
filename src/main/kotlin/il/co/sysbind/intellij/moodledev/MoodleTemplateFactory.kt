package il.co.sysbind.intellij.moodledev

import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.platform.ProjectTemplate
import com.intellij.platform.ProjectTemplatesFactory
import com.jetbrains.php.config.generation.PhpEmptyProjectGenerator

class MoodleTemplateFactory : ProjectTemplatesFactory() {
    override fun getGroups(): Array<String> {
        return arrayOf(PhpEmptyProjectGenerator.PHP_PROJECT_TEMPLATE_GROUP)
    }

    override fun createTemplates(group: String?, context: WizardContext): Array<ProjectTemplate> {
        return arrayOf(MoodlePluginGenerator())
    }
}
