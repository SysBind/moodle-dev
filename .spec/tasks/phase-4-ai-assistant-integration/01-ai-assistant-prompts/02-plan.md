# Plan - AI Assistant Prompts Integration

## 1. Core Infrastructure
1. Define `AIPromptService` interface in `il.co.sysbind.intellij.moodledev.util`.
    - Methods: `updatePrompts()`, `isAvailable(): Boolean`.
2. Create `AIPromptServiceFactory` to detect IDE version/API presence.
    - Logic to check for `AIChatLibraryPromptService` vs `PromptLibraryManager`.
3. Move prompt constants and Moodle instruction texts to a shared location or keep them in the interface.

## 2. Legacy Implementation
1. Implement `LegacyAIPromptServiceImpl`.
2. Encapsulate reflection logic for `PromptLibraryManager`.
3. Verify it correctly identifies and updates `ij.vcs.commit.generate-message` and documentation prompts.

## 3. New API Implementation (2024.x+)
1. Implement `NewAIPromptServiceImpl`.
2. Encapsulate reflection logic for `AIChatLibraryPromptService`.
3. Implement helpers for `PSString` and `PrivacySafe` creation and extraction.
4. Implement discovery logic for existing prompts via `getAllPrompts()`.
5. Implement fallback logic using `AICustomizablePrompt` extension point to handle built-in prompts that haven't been user-modified.

## 4. Integration & UI
1. Update `MoodleSettingsForm.configurePhpcs` to invoke the abstraction layer.
2. Ensure proper logging for each step of the update process.
3. Update `README.md` and `CHANGELOG.md` once implemented.

## 5. Testing & Validation
1. Create unit tests for `AIPromptServiceFactory`.
2. Create unit tests for implementations using mocks for the reflection targets.
3. Manual verification in both older (2023.x) and newer (2024.x+) IDE versions.
