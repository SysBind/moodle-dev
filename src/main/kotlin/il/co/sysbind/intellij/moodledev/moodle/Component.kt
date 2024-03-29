package il.co.sysbind.intellij.moodledev.moodle

import il.co.sysbind.intellij.moodledev.MoodleBundle
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

class Component {
    private val pluginTypesMap: JsonObject
    init {
        val pluginsObj = Json.parseToJsonElement(MoodleBundle.message("plugintypes"))
        pluginTypesMap = pluginsObj.jsonObject
    }

    fun getPluginPath(type: String): String {
        val path = pluginTypesMap[type]
        if (path != null) {
            return path.toString()
        }
        return ""
    }

    fun getPluginTypes(): Set<String> {
        return pluginTypesMap.keys
    }
}
