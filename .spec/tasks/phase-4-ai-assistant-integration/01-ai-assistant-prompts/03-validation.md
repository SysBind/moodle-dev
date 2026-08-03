# Validation - AI Assistant Prompts Integration

## Automated Tests
- [ ] `AIPromptServiceFactoryTest`: Verify correct implementation is returned based on available classes.
- [ ] `LegacyAIPromptServiceTest`: Verify reflection logic for old API (using mocks).
- [ ] `NewAIPromptServiceTest`: Verify reflection logic for new API (using mocks).
- [ ] `compileKotlin` must pass.
- [ ] `test` task must pass.

## Manual Verification
- [ ] **Legacy Version**: Run in PhpStorm 2023.x, click "Auto Settings", verify prompts are updated in AI Assistant settings.
- [ ] **New Version**: Run in PhpStorm 2024.x/2026.x, click "Auto Settings", verify prompts are updated in AI Assistant settings.
- [ ] **No Plugin**: Run without AI Assistant installed, verify no errors occur and "Auto Settings" completes successfully.
- [ ] **Already Updated**: Verify that clicking "Auto Settings" multiple times doesn't duplicate Moodle instructions in the prompts.

## Definition of Done
- [ ] Code follows Kotlin coding conventions.
- [ ] No hard dependencies on `com.intellij.ml.llm`.
- [ ] Abstraction layer implemented and used.
- [ ] Documentation (`README.md`, `CHANGELOG.md`) updated.
- [ ] All tests pass.
- [ ] User sign-off on the implemented behavior.
