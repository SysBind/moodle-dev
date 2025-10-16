package com.jetbrains.php.composer

import com.intellij.openapi.project.Project

/**
 * Test double for the PHP plugin's ComposerSettings class.
 * Provides a minimal surface for PhpComposerSettingsUtil to reflect on.
 */
class ComposerSettings private constructor(val project: Project) {
    var synchronizeWithComposerJson: Boolean = true

    fun setSynchronizeWithComposerJson(value: Boolean) {
        synchronizeWithComposerJson = value
    }

    // Alternative setter name to ensure our reflection fallback remains covered
    fun setAutoSyncEnabled(value: Boolean) {
        synchronizeWithComposerJson = value
    }

    companion object {
        @JvmStatic
        private var lastInstance: ComposerSettings? = null

        @JvmStatic
        fun getInstance(project: Project): ComposerSettings {
            val inst = ComposerSettings(project)
            lastInstance = inst
            return inst
        }

        @JvmStatic
        fun getLastInstance(): ComposerSettings? = lastInstance
    }
}
