package il.co.sysbind.intellij.moodledev.project

import com.intellij.openapi.components.*

@State(
    name = "il.co.sysbind.intellij.moodledev.settings.MoodleSettings",
    storages = [Storage("moodle-dev.xml", roamingType = RoamingType.DEFAULT)],
    category = SettingsCategory.TOOLS
)
class MoodleProjectSettings : PersistentStateComponent<MoodleSettings> {
    var settings: MoodleSettings = MoodleSettings()

    override fun getState(): MoodleSettings {
        return settings
    }

    override fun loadState(state: MoodleSettings) {
        settings = state
    }
}
