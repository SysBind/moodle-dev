package il.co.sysbind.intellij.moodledev.ai

import com.intellij.testFramework.LightPlatformTestCase
import com.intellij.testFramework.ServiceContainerUtil
import il.co.sysbind.intellij.moodledev.project.MoodleProjectSettings
import il.co.sysbind.intellij.moodledev.project.MoodleSettings

/**
 * A lightweight UI-ish test that runs inside the IntelliJ test environment and ensures
 * the MoodleAiLlmConfigurator executes without throwing when the plugin is enabled.
 * This does not require the AI plugin to be installed in tests.
 */
class MoodleAiUiTest : LightPlatformTestCase() {
    private class NoOpConfigurator : MoodleAiLlmConfigurator() {
        var executed = false
        override fun isAiPluginInstalled(): Boolean = false // simulate missing AI plugin
        override fun applyAIPrompts() { executed = true }
    }

    fun testProjectActivityRunsSafelyWhenPluginEnabled() {
        val svc = MoodleProjectSettings()
        svc.settings = MoodleSettings().also { it.pluginEnabled = true }
        ServiceContainerUtil.replaceService(project, MoodleProjectSettings::class.java, svc, testRootDisposable)

        val cfg = NoOpConfigurator()
        // Should complete without exceptions; with AI plugin missing it should skip applyAIPrompts
        runCatching { cfg.execute(project) }.onFailure { throw it }
        // Since AI plugin missing, prompts should not be applied
        assertFalse("applyAIPrompts should not be called without AI plugin") { cfg.executed }
    }
}
