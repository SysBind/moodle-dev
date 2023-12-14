package il.co.sysbind.intellij.moodledev.util

import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import il.co.sysbind.intellij.moodledev.moodle.Component
import java.nio.file.FileSystems
import java.nio.file.Files

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

    fun findFileUpwards(startDir: PsiDirectory, filename: String): VirtualFile? {
        var dir: VirtualFile = startDir.virtualFile
        val project = startDir.project
        while (dir.path.startsWith(project.basePath.toString())) {
            // stop loop if dir is not under baseDir
            val file = VfsUtil.findRelativeFile(dir, filename)
            if (file != null) {
                return file
            }
            dir = dir.parent
        }
        return null
    }

    fun getPluginName(startDir: PsiDirectory): String {
        val versionFilePath = findFileUpwards(startDir, "version.php")?.path
        if (versionFilePath != null && Files.exists(FileSystems.getDefault().getPath(versionFilePath))) {
            val componentLine = Files.readAllLines(FileSystems.getDefault().getPath(versionFilePath))
                .firstOrNull { it.trim().startsWith("\$plugin->component") }
            if (componentLine != null) {
                val pluginName = componentLine.split("=")[1].trim().removeSuffix(";").removeSurrounding("\"", "\"")
                    .removeSurrounding("\'", "\'")
                return pluginName
            }
        }
        return ""
    }
}
