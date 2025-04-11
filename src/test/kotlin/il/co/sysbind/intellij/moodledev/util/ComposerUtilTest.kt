package il.co.sysbind.intellij.moodledev.util

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.application.WriteAction
import com.intellij.testFramework.TempFiles
import java.io.File
import org.junit.Assume
import org.junit.Before
import org.junit.Test

class ComposerUtilTest : BasePlatformTestCase() {
    @Before
    fun checkComposerAvailability() {
        // Log composer availability for debugging
        println("[DEBUG_LOG] Composer available: ${ComposerUtil.isComposerAvailable()}")
    }

    @Test
    fun testSetupMoodleCs() {
        println("[DEBUG_LOG] Starting testSetupMoodleCs")

        // Check if running in GitHub Actions
        val isGitHubActions = System.getenv("GITHUB_ACTIONS") == "true"
        if (isGitHubActions) {
            println("[DEBUG_LOG] Running in GitHub Actions environment, skipping test")
            Assume.assumeTrue("Running in GitHub Actions environment, skipping test", false)
            return
        }

        // Check if composer is available
        val composerAvailable = ComposerUtil.isComposerAvailable()
        println("[DEBUG_LOG] Composer available in test: $composerAvailable")

        // Skip test if composer is not available
        if (!composerAvailable) {
            println("[DEBUG_LOG] Skipping Moodle CS setup test as composer is not available")
            Assume.assumeTrue("Composer is not available, skipping test", false)
            return
        }

        // Test the setup
        val result = ComposerUtil.setupMoodleCs(project)

        // Log the result
        println("[DEBUG_LOG] setupMoodleCs result: $result")

        // Verify composer commands were executed successfully
        assertTrue("Moodle CS setup should succeed", result)
    }

    @Test
    fun testRunComposerInstall() {
        println("[DEBUG_LOG] Starting testRunComposerInstall")

        // Check if running in GitHub Actions
        val isGitHubActions = System.getenv("GITHUB_ACTIONS") == "true"
        if (isGitHubActions) {
            println("[DEBUG_LOG] Running in GitHub Actions environment, skipping test")
            Assume.assumeTrue("Running in GitHub Actions environment, skipping test", false)
            return
        }

        // Check if composer is available
        val composerAvailable = ComposerUtil.isComposerAvailable()
        println("[DEBUG_LOG] Composer available in test: $composerAvailable")

        // Skip test if composer is not available
        if (!composerAvailable) {
            println("[DEBUG_LOG] Skipping composer install test as composer is not available")
            Assume.assumeTrue("Composer is not available, skipping test", false)
            return
        }

        // Create a real filesystem directory for testing
        val tempDir = File(System.getProperty("java.io.tmpdir"), "moodle_test_${System.currentTimeMillis()}")
        tempDir.mkdirs()
        println("[DEBUG_LOG] Created test directory: ${tempDir.absolutePath}")

        // Create composer.json in the test directory
        val composerJson = File(tempDir, "composer.json")
        composerJson.writeText("""
            {
                "name": "test/project",
                "description": "Test project",
                "type": "project",
                "require": {}
            }
        """.trimIndent())
        println("[DEBUG_LOG] Created composer.json in test directory")

        try {
            // Test composer install in test directory
            val result = ComposerUtil.runComposerInstall(project, tempDir.absolutePath)
            println("[DEBUG_LOG] runComposerInstall result: $result")
            assertTrue("Composer install should succeed in test directory", result)

            // Test with invalid directory
            val invalidResult = ComposerUtil.runComposerInstall(project, "/invalid/path")
            println("[DEBUG_LOG] runComposerInstall with invalid path result: $invalidResult")
            assertFalse("Composer install should fail with invalid directory", invalidResult)
        } finally {
            // Clean up
            tempDir.deleteRecursively()
            println("[DEBUG_LOG] Cleaned up test directory")
        }
    }

    override fun setUp() {
        super.setUp()
        println("[DEBUG_LOG] Test setup completed")
    }

    override fun tearDown() {
        println("[DEBUG_LOG] Test teardown started")
        super.tearDown()
    }

    @Test
    fun testGetComposerGlobalDir() {
        println("[DEBUG_LOG] Starting testGetComposerGlobalDir")

        // Check if running in GitHub Actions
        val isGitHubActions = System.getenv("GITHUB_ACTIONS") == "true"
        if (isGitHubActions) {
            println("[DEBUG_LOG] Running in GitHub Actions environment, skipping test")
            Assume.assumeTrue("Running in GitHub Actions environment, skipping test", false)
            return
        }

        // Check if composer is available
        val composerAvailable = ComposerUtil.isComposerAvailable()
        println("[DEBUG_LOG] Composer available in test: $composerAvailable")

        // First call should execute composer command or use fallback
        val globalDir = ComposerUtil.getComposerGlobalDir()
        println("[DEBUG_LOG] First call to getComposerGlobalDir: $globalDir")

        // Even if composer is not available, we should still get a directory from fallback
        assertNotNull("Composer global directory should be found", globalDir)

        // Check if the directory exists
        if (globalDir != null) {
            val dirExists = File(globalDir).exists()
            println("[DEBUG_LOG] Global directory exists: $dirExists")
            assertTrue("Global directory should exist", dirExists)
        }

        // Second call should use cached value
        val cachedDir = ComposerUtil.getComposerGlobalDir()
        println("[DEBUG_LOG] Second call to getComposerGlobalDir: $cachedDir")
        assertEquals("Cached directory should match first call", globalDir, cachedDir)
    }

    @Test
    fun testGetPhpcsAndPhpcbfPaths() {
        println("[DEBUG_LOG] Starting testGetPhpcsAndPhpcbfPaths")

        // Check if running in GitHub Actions
        val isGitHubActions = System.getenv("GITHUB_ACTIONS") == "true"
        if (isGitHubActions) {
            println("[DEBUG_LOG] Running in GitHub Actions environment, skipping test")
            Assume.assumeTrue("Running in GitHub Actions environment, skipping test", false)
            return
        }

        // Check if composer is available
        val composerAvailable = ComposerUtil.isComposerAvailable()
        println("[DEBUG_LOG] Composer available in test: $composerAvailable")

        // Skip test if composer is not available
        if (!composerAvailable) {
            println("[DEBUG_LOG] Skipping PHPCS and PHPCBF path test as composer is not available")
            Assume.assumeTrue("Composer is not available, skipping test", false)
            return
        }

        val phpcsPath = ComposerUtil.getPhpcsPath()
        val phpcbfPath = ComposerUtil.getPhpcbfPath()

        println("[DEBUG_LOG] PHPCS path: $phpcsPath")
        println("[DEBUG_LOG] PHPCBF path: $phpcbfPath")

        assertNotNull("PHPCS path should be found", phpcsPath)
        assertNotNull("PHPCBF path should be found", phpcbfPath)

        // Verify paths are in composer global directory
        val globalDir = ComposerUtil.getComposerGlobalDir()
        assertTrue("PHPCS path should be in global directory", 
            phpcsPath!!.startsWith(globalDir!!))
        assertTrue("PHPCBF path should be in global directory", 
            phpcbfPath!!.startsWith(globalDir))

        // Verify paths point to actual files
        assertTrue("PHPCS executable should exist", File(phpcsPath).exists())
        assertTrue("PHPCBF executable should exist", File(phpcbfPath).exists())
    }
}
