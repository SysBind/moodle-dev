package il.co.sysbind.intellij.moodledev.ai

object MoodleAiPrompts {
    // Updated "Write Documentation > PHP" content per Moodle coding style
    val phpDocPrompt: String = buildString {
        appendLine("According to https://moodledev.io/general/development/policies/codingstyle#documentation-and-comments")
        appendLine("Write PHPDoc for the given code.")
        appendLine()
        appendLine("Rules:")
        appendLine("- Use Moodle PHPDoc style and tags.")
        appendLine("- In unit tests only, add @covers for the relevant class and functions actually tested.")
        appendLine("- Do not add @covers in non‑test code.")
    }

    // Updated Built-In Actions > Commit Message generation template
    val commitMessagePrompt: String = buildString {
        appendLine("Make commit according to https://moodledev.io/general/development/policies/codingstyle#git-commits")
        appendLine("Format your commit messages following this structure:")
        appendLine()
        appendLine("<$GIT_BRANCH_NAME> <code_area>: <short_summary>")
        appendLine()
        appendLine("<detailed_explanation>")
        appendLine()
        appendLine("Guidelines:")
        appendLine("- First line should be no more than 72 characters")
        appendLine("- Use imperative form for summary (e.g., \"Add\" not \"Added\")")
        appendLine("- Leave blank line after summary")
        appendLine("- Keep detailed explanation to 2-3 sentences")
        appendLine("- Include motivation and contrast with previous behavior")
        appendLine()
        appendLine("Example:")
        appendLine("issue7685 mod_quiz: Add time extension support for quiz attempts")
        appendLine()
        appendLine("Implements ability to grant individual students extra time for quiz attempts.")
        appendLine("This change allows teachers to accommodate students needing special arrangements")
        appendLine("while maintaining the standard time limits for others.")
        appendLine()
        appendLine("Important Notes:")
        appendLine("- For submodule commits, use the submodule's branch name")
        appendLine("- Code area should be human-readable (e.g., 'gradebook' vs 'local_hujigradebook')")
        appendLine("- Avoid including multiple unrelated changes")
        appendLine("- Don't document the development process, only the final result")
        appendLine()
        appendLine("General Practices:")
        appendLine("- Keep PRs focused and manageable")
        appendLine("- Update documentation with changes")
        appendLine("- Follow branching strategy")
    }
}
