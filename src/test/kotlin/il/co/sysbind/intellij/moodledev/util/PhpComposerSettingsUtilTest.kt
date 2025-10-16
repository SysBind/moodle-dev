package il.co.sysbind.intellij.moodledev.util

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.php.composer.ComposerSettings
import org.junit.Test

class PhpComposerSettingsUtilTest : BasePlatformTestCase() {

    @Test
    fun testDisableComposerSync_DisablesOnFakeComposerSettings() {
        // Ensure there's no lingering instance from other tests
        val before = ComposerSettings.getLastInstance()
        assertNull("Precondition: last instance should be null or irrelevant", before)

        // Act
        PhpComposerSettingsUtil.disableComposerSync(project)

        // Assert
        val instance = ComposerSettings.getLastInstance()
        assertNotNull("ComposerSettings instance should be created by reflection", instance)
        assertFalse(
            "Composer synchronization must be disabled when called",
            instance!!.synchronizeWithComposerJson
        )
    }
}
