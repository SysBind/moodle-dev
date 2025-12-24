package il.co.sysbind.intellij.moodledev.ai

import com.intellij.testFramework.LightPlatformTestCase
import com.intellij.testFramework.ServiceContainerUtil
import il.co.sysbind.intellij.moodledev.project.MoodleProjectSettings
import il.co.sysbind.intellij.moodledev.project.MoodleSettings
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue

class MoodleAiLlmConfiguratorTest : LightPlatformTestCase() {

    private class TestConfigurator(private val aiPresent: Boolean) : MoodleAiLlmConfigurator() {
        var appliedCalled = false
        override fun isAiPluginInstalled(): Boolean = aiPresent
        override fun applyAIPrompts() {
            appliedCalled = true
        }
    }

    private fun registerSettings(pluginEnabled: Boolean) {
        val svc = MoodleProjectSettings()
        svc.settings = MoodleSettings().also { it.pluginEnabled = pluginEnabled }
        ServiceContainerUtil.replaceService(project, MoodleProjectSettings::class.java, svc, testRootDisposable)
    }

    fun testNotEnabled_skipsEverything() {
        registerSettings(pluginEnabled = false)
        val cfg = TestConfigurator(aiPresent = true)
        // Should not throw and should not apply
        runCatching { cfg.execute(project) }
        assertFalse(cfg.appliedCalled, "applyAIPrompts should not be called when plugin is disabled")
    }

    fun testEnabledButAiPluginMissing_skipsApply() {
        registerSettings(pluginEnabled = true)
        val cfg = TestConfigurator(aiPresent = false)
        runCatching { cfg.execute(project) }
        assertFalse(cfg.appliedCalled, "applyAIPrompts should not be called when AI plugin missing")
    }

    fun testEnabledAndAiPluginPresent_applies() {
        registerSettings(pluginEnabled = true)
        val cfg = TestConfigurator(aiPresent = true)
        runCatching { cfg.execute(project) }.onFailure { throw it }
        assertTrue(cfg.appliedCalled, "applyAIPrompts should be called when enabled and AI plugin present")
    }
}
