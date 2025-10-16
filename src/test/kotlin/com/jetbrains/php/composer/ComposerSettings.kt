package com.jetbrains.php.composer

import com.intellij.openapi.project.Project

/**
 * Test double for the PHP plugin's ComposerSettings class.
 * Provides a minimal surface for PhpComposerSettingsUtil to reflect on.
 */
class ComposerSettings private constructor(val project: Project? = null) {
    var synchronizeWithComposerJson: Boolean = true
    var synchronizationState: SynchronizationState = SynchronizationState.SYNCHRONIZE

    // Enum mirrors real plugin concept for workspace.xml persistence
    enum class SynchronizationState { SYNCHRONIZE, DONT_SYNCHRONIZE }

    // Preferred newer API
    fun setSynchronizationState(state: SynchronizationState) {
        synchronizationState = state
        // Keep boolean flag consistent with enum semantics
        synchronizeWithComposerJson = state == SynchronizationState.SYNCHRONIZE
    }

    // Alternative setter name to ensure our reflection fallback remains covered
    fun setAutoSyncEnabled(value: Boolean) {
        synchronizeWithComposerJson = value
        synchronizationState = if (value) SynchronizationState.SYNCHRONIZE else SynchronizationState.DONT_SYNCHRONIZE
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
        fun getInstance(): ComposerSettings {
            val inst = ComposerSettings(null)
            lastInstance = inst
            return inst
        }

        @JvmStatic
        fun getLastInstance(): ComposerSettings? = lastInstance

        // Test-only utility to reset singleton-like state between tests
        @JvmStatic
        fun clearLastInstanceForTests() {
            lastInstance = null
        }
    }
}
