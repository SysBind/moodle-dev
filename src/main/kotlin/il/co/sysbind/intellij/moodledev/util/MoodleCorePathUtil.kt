package il.co.sysbind.intellij.moodledev.util

import com.intellij.openapi.vfs.LocalFileSystem
import il.co.sysbind.intellij.moodledev.moodle.Component

class MoodleCorePathUtil {

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
}
