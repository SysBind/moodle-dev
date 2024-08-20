package il.co.sysbind.intellij.moodledev.project.wizard

import com.intellij.ide.util.projectWizard.ModuleBuilder
import com.intellij.openapi.module.ModuleType

class MoodlePlugin : ModuleBuilder() {
    override fun getModuleType(): ModuleType<*> {
        return ModuleType.EMPTY
    }
}
