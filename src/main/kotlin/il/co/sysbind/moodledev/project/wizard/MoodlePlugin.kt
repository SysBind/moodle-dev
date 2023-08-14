package il.co.sysbind.moodledev.project.wizard

import com.intellij.ide.util.projectWizard.ModuleBuilder
import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.roots.ui.configuration.ModulesProvider

class MoodlePlugin : ModuleBuilder() {
    override fun getModuleType(): ModuleType<*> {
        return ModuleType.EMPTY;
    }

    override fun createWizardSteps(
        wizardContext: WizardContext,
        modulesProvider: ModulesProvider
    ): Array<ModuleWizardStep> {
        return super.createWizardSteps(wizardContext, modulesProvider)
    }
}
