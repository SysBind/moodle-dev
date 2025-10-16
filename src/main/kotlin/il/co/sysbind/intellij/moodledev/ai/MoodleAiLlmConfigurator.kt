package il.co.sysbind.intellij.moodledev.ai

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import il.co.sysbind.intellij.moodledev.project.MoodleProjectSettings
import il.co.sysbind.intellij.moodledev.util.PluginUtil

class MoodleAiLlmConfigurator : ProjectActivity {
    private val log = Logger.getInstance(MoodleAiLlmConfigurator::class.java)

    override suspend fun execute(project: Project) {
        val settingsService = project.getService(MoodleProjectSettings::class.java) ?: return
        val enabled = settingsService.settings.pluginEnabled
        if (!enabled) return

        if (!PluginUtil.isPluginInstalled("com.intellij.ml.llm")) {
            log.debug("LLM plugin not installed; skipping Moodle AI prompt configuration")
            return
        }

        tryApplyAIPrompts()
    }

    private fun tryApplyAIPrompts() {
        val phpDoc = MoodleAiPrompts.phpDocPrompt
        val commit = MoodleAiPrompts.commitMessagePrompt

        var applied = false

        // Attempt 1: Hypothetical PromptRepository in com.intellij.ml.llm
        applied = applied or runCatching {
            val repoClass = Class.forName("com.intellij.ml.llm.settings.prompts.PromptRepository")
            val getInstance = repoClass.getMethod("getInstance")
            val instance = getInstance.invoke(null)
            val setTemplate = repoClass.getMethod("setTemplate", String::class.java, String::class.java, String::class.java)
            setTemplate.invoke(instance, "Write Documentation", "PHP", phpDoc)
            setTemplate.invoke(instance, "Commit Message", "", commit)
            true
        }.getOrElse { false }

        // Attempt 2: Hypothetical PromptTemplateRegistry in AI Actions
        applied = applied or runCatching {
            val registryClass = Class.forName("com.intellij.aiactions.prompt.PromptTemplateRegistry")
            val getInstance = registryClass.getMethod("getInstance")
            val instance = getInstance.invoke(null)
            val setById = registryClass.getMethod("setTemplate", String::class.java, String::class.java)
            setById.invoke(instance, "write.documentation.php", phpDoc)
            setById.invoke(instance, "commit.message.generate", commit)
            true
        }.getOrElse { false }

        if (!applied) {
            log.info("Moodle AI prompts could not be applied (no known registry found). This is safe to ignore if the AI plugin does not expose public APIs for templates.")
        } else {
            log.info("Moodle AI prompts applied successfully (best-effort).")
        }
    }
}
