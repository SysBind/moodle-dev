package il.co.sysbind.intellij.moodledev.codeStyle

import com.intellij.lang.Language
import com.intellij.lang.javascript.JavascriptLanguage
import com.intellij.lang.javascript.formatter.JSCodeStyleSettings
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.PredefinedCodeStyle

class MoodleJavascriptPredefinedCodeStyle : PredefinedCodeStyle("Moodle", JavascriptLanguage.INSTANCE) {
    override fun apply(settings: CodeStyleSettings, language: Language) {
        val commonSettings = settings.getCommonSettings(language)
        180.also { commonSettings.RIGHT_MARGIN = it }

        val customSettings = settings.getCustomSettings(JSCodeStyleSettings::class.java)
        customSettings.SPACES_WITHIN_IMPORTS = true
    }
}
