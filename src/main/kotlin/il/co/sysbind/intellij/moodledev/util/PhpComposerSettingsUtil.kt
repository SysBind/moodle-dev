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

            // Obtain getInstance(Project) if available, else fall back to no-arg getInstance()
            val instance = try {
                val withProject = clazz.methods.firstOrNull {
                    it.name.equals("getInstance", true) && it.parameterCount == 1 && Project::class.java.isAssignableFrom(it.parameterTypes[0])
                }
                val noArg = clazz.methods.firstOrNull { it.name.equals("getInstance", true) && it.parameterCount == 0 }
                when {
                    withProject != null -> withProject.invoke(null, project)
                    noArg != null -> noArg.invoke(null)
                    else -> null
                }
            } catch (e: Exception) {
                log.warn("Failed to obtain ComposerSettings instance: ${e.message}")
                null
            } ?: return

            // 1) Prefer enum-based API: setSynchronizationState(SynchronizationState.DONT_SYNCHRONIZE)
            try {
                val enumClass = try {
                    Class.forName("com.jetbrains.php.composer.SynchronizationState")
                } catch (e: ClassNotFoundException) {
                    null
                }
                val setState = clazz.methods.firstOrNull { m ->
                    m.name.equals("setSynchronizationState", true) && m.parameterCount == 1 && (enumClass == null || m.parameterTypes[0].isEnum)
                }
                if (setState != null) {
                    val dont = enumClass?.enumConstants?.firstOrNull { (it as Enum<*>).name.equals("DONT_SYNCHRONIZE", true) }
                        ?: setState.parameterTypes[0].enumConstants.firstOrNull { (it as Enum<*>).name.contains("DONT", true) }
                    if (dont != null) {
                        setState.isAccessible = true
                        setState.invoke(instance, dont)
                        log.info("Disabled Composer sync via setSynchronizationState(DONT_SYNCHRONIZE).")
                        return
                    }
                }
            } catch (ignore: Throwable) {
                // fall through to boolean-based API
            }

            // 2) Boolean-based API fallbacks: look for setter containing "sync"
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
