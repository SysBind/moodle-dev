package il.co.sysbind.intellij.moodledev.util

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project

/**
 * Utilities for interacting with the PHP Composer settings without taking a hard compile-time dependency
 * on internal APIs that may change between PHP plugin versions.
 *
 * Goal: ensure "Synchronize IDE settings with composer.json" is disabled when Moodle framework is enabled.
 */
object PhpComposerSettingsUtil {
    private val log = Logger.getInstance(PhpComposerSettingsUtil::class.java)

    /**
     * Try to disable Composer auto-synchronization of IDE settings with composer.json.
     * Uses reflection to remain compatible across PHP plugin versions where the API name may differ.
     */
    fun disableComposerSync(project: Project) {
        try {
            // Try primary known settings class
            val clazz = try {
                Class.forName("com.jetbrains.php.composer.ComposerSettings")
            } catch (cnf: ClassNotFoundException) {
                // Fallback: some versions might use different package/name; keep room for expansion
                log.warn("ComposerSettings class not found: ${cnf.message}")
                null
            } ?: return

            // Obtain getInstance(Project) if available
            val instance = try {
                val m = clazz.methods.firstOrNull { it.name == "getInstance" && it.parameterCount == 1 && it.parameterTypes[0] == Project::class.java }
                    ?: clazz.methods.firstOrNull { it.name.equals("getInstance", true) }
                if (m != null) m.invoke(null, project) else null
            } catch (e: Exception) {
                log.warn("Failed to obtain ComposerSettings instance: ${e.message}")
                null
            } ?: return

            // Look for a setter that likely controls synchronization; prefer names containing "sync"
            val candidates = clazz.methods.filter { method ->
                method.name.startsWith("set") &&
                        method.parameterCount == 1 &&
                        (method.parameterTypes[0] == java.lang.Boolean.TYPE || method.parameterTypes[0] == java.lang.Boolean::class.java) &&
                        method.name.contains("sync", ignoreCase = true)
            }

            // If no obvious candidates, try some well-known names explicitly to be safe in future refactors
            val preferredNames = listOf(
                "setSynchronizeWithComposerJson",
                "setSynchronizeIdeSettingsWithComposerJson",
                "setSyncWithComposerJson",
                "setAutoSyncEnabled",
                "setSynchronizeSettings"
            )

            val method = candidates.firstOrNull { m -> preferredNames.any { pn -> m.name.equals(pn, ignoreCase = true) } }
                ?: candidates.firstOrNull()

            if (method != null) {
                method.isAccessible = true
                method.invoke(instance, false)
                log.info("Disabled PHP Composer synchronization with composer.json via ${method.name}().")
            } else {
                log.warn("Could not find a suitable Composer sync setting method to invoke.")
            }
        } catch (t: Throwable) {
            log.warn("Failed to disable Composer sync: ${t.message}")
        }
    }
}
