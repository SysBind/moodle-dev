package il.co.sysbind.moodledev.codeStyle

import com.intellij.lang.Language
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.PredefinedCodeStyle
import org.jetbrains.plugins.less.LESSFileType
import org.jetbrains.plugins.less.LESSLanguage
import org.jetbrains.plugins.less.settings.LessCodeStyleSettings

class MoodleLessPredefinedCodeStyle : PredefinedCodeStyle("Moodle", LESSLanguage.INSTANCE) {
    override fun apply(settings: CodeStyleSettings, language: Language) {
        val customSettings = settings.getCustomSettings(LessCodeStyleSettings::class.java)
        customSettings.HEX_COLOR_LOWER_CASE = true
        customSettings.HEX_COLOR_SHORT_FORMAT = true

        val indentSettings = settings.getIndentOptions(LESSFileType.LESS)
        4.also { indentSettings.INDENT_SIZE = it }
    }
}
