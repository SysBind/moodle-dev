package il.co.sysbind.intellij.moodledev.project

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.php.composer.ComposerSettings
import il.co.sysbind.intellij.moodledev.MoodleBundle
import org.junit.Test

class MoodleSettingsComposerSyncTest : BasePlatformTestCase() {

    @Test
    fun testApply_DisablesComposerSyncWhenFrameworkEnabled() {
        // Arrange
        val projectSettings = project.getService(MoodleProjectSettings::class.java)
        projectSettings.settings.pluginEnabled = false
        val form = MoodleSettingsForm(project)
        // create UI to initialize components
        val component = form.createComponent()
        assertNotNull("Settings form component should be created", component)
        // Ensure initial state false
        assertFalse("Precondition: plugin should be disabled", form.pluginEnabled.component.isSelected)

        // Act: user enables the framework and clicks Apply
        form.pluginEnabled.component.isSelected = true
        form.apply()

        // Assert: state persisted and composer sync disabled via test double
        assertTrue("Plugin should be enabled after apply", projectSettings.settings.pluginEnabled)
        val instance = ComposerSettings.getLastInstance()
        assertNotNull("ComposerSettings test double should have been instantiated", instance)
        assertFalse("Composer sync should be disabled when enabling framework", instance!!.synchronizeWithComposerJson)

        // UI bits sanity: display name/id available (lightweight UI test)
        assertTrue(MoodleBundle.getMessage("configurable.name").isNotBlank())
        assertTrue(form.getId().isNotBlank())
    }
}
