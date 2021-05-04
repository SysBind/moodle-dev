package il.co.sysbind.intellij.moodledev.util

import com.intellij.openapi.vfs.LocalFileSystem
import il.co.sysbind.intellij.moodledev.moodle.Component

class MoodleCorePathUtil {

    public fun isMoodlePathValid(corePath: String) : Boolean {
        if (corePath.isEmpty()){
            return false
        }
        val moodleTree = Component()
        moodleTree.getPluginTypes().forEach(action = {
            val moodleVersionFile = LocalFileSystem.getInstance().findFileByPath( corePath +
                    "/" + moodleTree.getPluginPath(it))
            if (moodleVersionFile == null || !moodleVersionFile?.isDirectory) {
                return false
            }
        })
        return true
    }
}
