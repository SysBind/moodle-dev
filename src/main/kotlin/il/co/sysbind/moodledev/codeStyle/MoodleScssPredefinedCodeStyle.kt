package il.co.sysbind.moodledev.codeStyle

import com.intellij.lang.Language
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.PredefinedCodeStyle
import org.jetbrains.plugins.scss.SCSSFileType
import org.jetbrains.plugins.scss.SCSSLanguage
import org.jetbrains.plugins.scss.settings.ScssCodeStyleSettings

class MoodleScssPredefinedCodeStyle : PredefinedCodeStyle("Moodle", SCSSLanguage.INSTANCE) {
    override fun apply(settings: CodeStyleSettings, language: Language) {
        val customSettings = settings.getCustomSettings(ScssCodeStyleSettings::class.java)
        customSettings.HEX_COLOR_LOWER_CASE = true
        customSettings.HEX_COLOR_SHORT_FORMAT = true

        val indentSettings = settings.getIndentOptions(SCSSFileType.SCSS)
        4.also { indentSettings.INDENT_SIZE = it }
    }
}
