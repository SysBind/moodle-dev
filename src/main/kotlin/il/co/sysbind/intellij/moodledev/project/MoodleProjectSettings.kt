package il.co.sysbind.intellij.moodledev.project

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.StoragePathMacros

@State(
    name = "il.co.sysbind.intellij.moodledev.settings.MoodleSettings",
    storages = [Storage(StoragePathMacros.WORKSPACE_FILE)]
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
