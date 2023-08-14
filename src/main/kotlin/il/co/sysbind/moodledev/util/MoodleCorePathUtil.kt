package il.co.sysbind.moodledev.util

import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import il.co.sysbind.moodledev.moodle.Component

object MoodleCorePathUtil {

    private val MOODLE_VERSION_FILE = "version.php"

    fun isMoodlePathValid(corePath: String): Boolean {
        val moodleTree = Component()
        moodleTree.getPluginTypes().forEach {
            val moodleVersionFile = LocalFileSystem.getInstance().findFileByPath(
                corePath + "/" + moodleTree.getPluginPath(it)
            )
            if (moodleVersionFile == null || !moodleVersionFile.isDirectory) {
                return false
            }
        }
        return true
    }

    @JvmStatic
    fun findMoodleVersion(dir: VirtualFile?): VirtualFile? {
        if (dir == null || !dir.isValid) return null
        val versionfile = dir.findChild(MOODLE_VERSION_FILE)
        if (versionfile != null) {
            return versionfile
        }
        return null
    }
}
