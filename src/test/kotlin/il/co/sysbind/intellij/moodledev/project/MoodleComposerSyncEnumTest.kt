package il.co.sysbind.intellij.moodledev.project

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.php.composer.ComposerSettings
import il.co.sysbind.intellij.moodledev.util.PhpComposerSettingsUtil
import org.junit.Test

class MoodleComposerSyncEnumTest : BasePlatformTestCase() {

    @Test
    fun testDisableComposerSync_UsesEnumWhenAvailable() {
        // Precondition: test double provides enum-based API
        ComposerSettings.clearLastInstanceForTests()

        // Act
        PhpComposerSettingsUtil.disableComposerSync(project)

        // Assert
        val settings = ComposerSettings.getLastInstance()
        assertNotNull("ComposerSettings should have been instantiated via reflection", settings)
        assertEquals(
            "SynchronizationState should be DONT_SYNCHRONIZE",
            ComposerSettings.SynchronizationState.DONT_SYNCHRONIZE,
            settings!!.synchronizationState
        )
        assertFalse("Boolean mirror should be false when enum is DONT_SYNCHRONIZE", settings.synchronizeWithComposerJson)
    }
}
