package il.co.sysbind.intellij.moodledev

import il.co.sysbind.intellij.moodledev.project.MoodleSettings

class MoodlePluginGeneratorSettings(moodleSettings: MoodleSettings) {
    private var myMoodleSettings: MoodleSettings = moodleSettings
    private var myMoodlePath: String = ""
    private var myMoodleVersion: String = "master"
    private var myPluginEnabled: Boolean = true;

    fun getMoodleVersion(): String {
        return this.myMoodleVersion
    }

    fun getMoodlePath(): String {
        return myMoodlePath
    }
    fun getPluginEnabled(): Boolean {
        return this.myPluginEnabled
    }

    fun getMoodleSettings(): MoodleSettings {
        return myMoodleSettings
    }

}
