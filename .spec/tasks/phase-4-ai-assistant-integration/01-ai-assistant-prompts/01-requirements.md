# Requirements - AI Assistant Prompts Integration

## Scope
The goal of this feature is to automatically append Moodle-specific instructions to AI Assistant prompts to ensure generated content (commit messages and documentation) complies with Moodle standards.

### Included
- Automatic update of the following prompt IDs:
    - `ij.vcs.commit.generate-message` (Commit messages)
    - `AIAssistant.WriteDocumentation.Php` (PHP Documentation)
    - `AIAssistant.WriteDocumentation.JavaScript` (JavaScript Documentation)
    - `ij.editor.generate-documentation` (Generic documentation)
- Support for multiple IntelliJ Platform versions:
    - Legacy API (e.g., PhpStorm 2023.x) using `PromptLibraryManager`.
    - New API (e.g., PhpStorm 2024.x+) using `AIChatLibraryPromptService`.
- Fallback mechanism to `AICustomizablePrompt` extension point if a prompt hasn't been modified by the user yet.
- Content wrapping support for `PSString` and `PrivacySafe` types required by the new API.

### Excluded
- Modification of non-Moodle related prompts.
- User UI for editing these prompts (handled by AI Assistant's own settings).

## Decisions
- **Version-Aware Abstraction Layer**: To maintain clean code and handle breaking internal API changes between IDE versions, all reflection-based logic will be isolated behind an interface (e.g., `AIPromptService`).
- **Runtime Implementation Factory**: A factory will detect the available AI Assistant API at runtime and instantiate the correct implementation.
- **Minimal Interference**: The plugin will only append Moodle instructions if they are not already present in the prompt.
- **Reflection-Only Dependency**: To avoid hard dependencies on the AI Assistant plugin (which might not be installed), all interactions will continue to use reflection with proper classloader handling.

## Context
- **Moodle Commit Policy**: [https://moodledev.io/general/development/policies/codingstyle#git-commits](https://moodledev.io/general/development/policies/codingstyle#git-commits)
- **Moodle Documentation Policy**: [https://moodledev.io/general/development/policies/codingstyle#documentation-and-comments](https://moodledev.io/general/development/policies/codingstyle#documentation-and-comments)
- **Tone**: Professional and compliant with established Moodle community standards.
