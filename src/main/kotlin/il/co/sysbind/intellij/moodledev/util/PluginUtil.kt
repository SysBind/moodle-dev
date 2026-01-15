package il.co.sysbind.intellij.moodledev.util

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.extensions.PluginId

object PluginUtil {
    fun isPluginInstalled(id: String): Boolean = try {
        PluginManagerCore.isPluginInstalled(PluginId.getId(id))
    } catch (_: Throwable) {
        false
    }
}
