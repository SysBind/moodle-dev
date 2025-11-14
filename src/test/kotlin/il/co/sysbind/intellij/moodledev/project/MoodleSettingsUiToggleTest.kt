package il.co.sysbind.intellij.moodledev.project

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.php.composer.ComposerSettings
import org.junit.Test

/**
 * Lightweight UI-ish test exercising the Configurable form apply() path.
 * It simulates a user enabling the Moodle framework via the Settings page
 * and verifies that Composer sync is disabled using our reflection bridge.
 */
class MoodleSettingsUiToggleTest : BasePlatformTestCase() {

    @Test
    fun testEnablingFrameworkDisablesComposerSync_Idempotent() {
        // Arrange
        val form = MoodleSettingsForm(project)
        form.createComponent() // initialize form components

        // Enable and apply twice to ensure idempotency
        form.pluginEnabled.component.isSelected = true
        form.apply()
        form.apply()

        val cs = ComposerSettings.getLastInstance()
        assertNotNull("ComposerSettings test instance should exist after apply()", cs)
        assertFalse("Composer sync should be disabled after enabling framework",
            cs!!.synchronizeWithComposerJson)
        assertEquals("Enum state should be DONT_SYNCHRONIZE",
            ComposerSettings.SynchronizationState.DONT_SYNCHRONIZE,
            cs.synchronizationState)

        // Now disable framework and apply; we do not re-enable sync automatically.
        form.pluginEnabled.component.isSelected = false
        form.apply()

        // Composer sync should remain disabled (plugin does not auto-enable it on disable)
        val cs2 = ComposerSettings.getLastInstance()
        assertFalse("Composer sync should remain disabled after disabling framework",
            cs2!!.synchronizeWithComposerJson)
    }
}
