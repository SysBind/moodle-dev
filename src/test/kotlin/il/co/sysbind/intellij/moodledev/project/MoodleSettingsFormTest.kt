package il.co.sysbind.intellij.moodledev.project

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import il.co.sysbind.intellij.moodledev.util.ComposerUtil
import org.junit.Test
import java.io.File

class MoodleSettingsFormTest : BasePlatformTestCase() {
    private var composerSetupCalled = false
    private lateinit var settingsForm: MoodleSettingsForm

    @Test
    fun testComposerSetupOnFrameworkEnable() {
        println("[DEBUG_LOG] Starting testComposerSetupOnFrameworkEnable")

        // Get current settings
        val projectSettings = project.getService(MoodleProjectSettings::class.java)
        println("[DEBUG_LOG] Before enable - plugin enabled: ${projectSettings.settings.pluginEnabled}")

        // Enable the plugin and persist the change
        projectSettings.settings.pluginEnabled = true
        val newState = projectSettings.getState()
        projectSettings.loadState(newState)
        println("[DEBUG_LOG] After enable - plugin enabled: ${projectSettings.settings.pluginEnabled}")
        println("[DEBUG_LOG] State after enable: ${projectSettings.getState()}")

        // Reset form to pick up new settings
        settingsForm = MoodleSettingsForm(project)
        settingsForm.createComponent() // Initialize the form components

        // Verify and log UI state
        println("[DEBUG_LOG] Form checkbox component created: ${settingsForm.pluginEnabled.component != null}")
        println("[DEBUG_LOG] Form checkbox selected: ${settingsForm.pluginEnabled.component.isSelected}")

        // Ensure checkbox is selected
        settingsForm.pluginEnabled.component.isSelected = true
        println("[DEBUG_LOG] Form checkbox selected after update: ${settingsForm.pluginEnabled.component.isSelected}")

        // Apply settings
        println("[DEBUG_LOG] Applying settings with plugin enabled")
        settingsForm.apply()

        // Verify the plugin is enabled and composer setup was attempted
        assertTrue("Plugin should be enabled", settingsForm.isBeingUsed)
        println("[DEBUG_LOG] Framework enabled status: ${settingsForm.isBeingUsed}")

        // Verify that the Moodle path is set correctly
        assertNotNull("Moodle path should be set", projectSettings.settings.moodlePath)
        println("[DEBUG_LOG] Moodle path: ${projectSettings.settings.moodlePath}")
        println("[DEBUG_LOG] Final plugin enabled status: ${projectSettings.settings.pluginEnabled}")
    }

    override fun setUp() {
        super.setUp()

        // Initialize settings and form
        val projectSettings = project.getService(MoodleProjectSettings::class.java)
        projectSettings.settings.pluginEnabled = false
        projectSettings.settings.moodlePath = project.basePath ?: ""
        settingsForm = MoodleSettingsForm(project)

        println("[DEBUG_LOG] Test setup completed")
        println("[DEBUG_LOG] Initial plugin enabled: ${projectSettings.settings.pluginEnabled}")
        println("[DEBUG_LOG] Initial moodle path: ${projectSettings.settings.moodlePath}")
    }

    override fun tearDown() {
        println("[DEBUG_LOG] Test teardown started")
        super.tearDown()
    }

    @Test
    fun testPhpCodesnifferPathDetection() {
        println("[DEBUG_LOG] Starting testPhpCodesnifferPathDetection")

        // Verify paths can be detected
        val phpcsPath = ComposerUtil.getPhpcsPath()
        val phpcbfPath = ComposerUtil.getPhpcbfPath()

        println("[DEBUG_LOG] Detected PHPCS path: $phpcsPath")
        println("[DEBUG_LOG] Detected PHPCBF path: $phpcbfPath")

        // Verify paths were found
        assertNotNull("PHPCS path should be detected", phpcsPath)
        assertNotNull("PHPCBF path should be detected", phpcbfPath)

        // Verify paths exist
        assertTrue("PHPCS executable should exist", File(phpcsPath!!).exists())
        assertTrue("PHPCBF executable should exist", File(phpcbfPath!!).exists())
    }
}
