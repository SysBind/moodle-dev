package il.co.sysbind.intellij.moodledev.util

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.Test

class MoodleCorePathUtilTest : BasePlatformTestCase() {

    @Test
    fun testGetPluginNameWithComments() {
        val versionContent = "<?php\n" +
                "\$plugin->component = 'mod_assign'; // some comment\n" +
                "\$plugin->version = 2023052200;"
        
        val file = myFixture.addFileToProject("version.php", versionContent)
        val psiDir = file.containingDirectory
        
        val pluginName = MoodleCorePathUtil.getPluginName(psiDir)
        assertEquals("mod_assign", pluginName)
    }

    @Test
    fun testGetPluginNameWithDoubleQuotesAndComments() {
        val versionContent = "<?php\n" +
                "\$plugin->component = \"block_test\"; # another comment\n"
        
        val file = myFixture.addFileToProject("version.php", versionContent)
        val psiDir = file.containingDirectory
        
        val pluginName = MoodleCorePathUtil.getPluginName(psiDir)
        assertEquals("block_test", pluginName)
    }

    @Test
    fun testGetMoodleVersionWithComments() {
        val versionContent = "<?php\n" +
                "\$plugin->component = 'mod_assign';\n" +
                "\$version = 2023052200.00; // Moodle version comment"
        
        myFixture.addFileToProject("version.php", versionContent)
        val dir = myFixture.findFileInTempDir("")
        
        val version = MoodleCorePathUtil.getMoodleVersion(dir)
        assertEquals("2023052200", version)
    }

    @Test
    fun testGetNamespaceWithSubdirectory() {
        val versionContent = "<?php\n" +
                "\$plugin->component = 'mod_assign';"
        
        myFixture.addFileToProject("version.php", versionContent)
        val classesDir = myFixture.addFileToProject("classes/output/renderer.php", "<?php").containingDirectory
        
        val namespace = MoodleCorePathUtil.getNamespace(classesDir)
        assertEquals("mod_assign\\output", namespace)
    }
}
