package il.co.sysbind.intellij.moodledev.ai

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MoodleAiPromptsTest {
    @Test
    fun `php doc prompt mentions Moodle coding style and PHPDoc`() {
        val p = MoodleAiPrompts.phpDocPrompt
        assertTrue(p.contains("moodledev.io/general/development/policies/codingstyle"))
        assertTrue(p.contains("PHPDoc"))
        assertTrue(p.contains("@covers") || p.contains("@cover"))
    }

    @Test
    fun `commit message prompt includes structure and example`() {
        val p = MoodleAiPrompts.commitMessagePrompt
        assertTrue(p.contains("<\$GIT_BRANCH_NAME> <code_area>: <short_summary>"))
        assertTrue(p.contains("issue7685 mod_quiz"))
        assertTrue(p.contains("First line should be no more than 72 characters"))
    }
}
