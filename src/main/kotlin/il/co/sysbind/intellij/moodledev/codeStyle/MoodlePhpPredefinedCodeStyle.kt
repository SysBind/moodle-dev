package il.co.sysbind.intellij.moodledev.codeStyle

import com.intellij.lang.Language
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.codeStyle.PredefinedCodeStyle
import com.jetbrains.php.lang.PhpLanguage
import com.jetbrains.php.lang.formatter.PhpCodeStyleSettings

class MoodlePhpPredefinedCodeStyle : PredefinedCodeStyle("Moodle", PhpLanguage.INSTANCE) {
    override fun apply(settings: CodeStyleSettings, language: Language) {
        val commonSettings: CommonCodeStyleSettings = settings.getCommonSettings(language)
        132.also { commonSettings.RIGHT_MARGIN = it }
        commonSettings.LINE_COMMENT_AT_FIRST_COLUMN = false
        commonSettings.KEEP_FIRST_COLUMN_COMMENT = false
        commonSettings.KEEP_BLANK_LINES_IN_DECLARATIONS = 1
        commonSettings.KEEP_BLANK_LINES_IN_CODE = 1
        commonSettings.KEEP_BLANK_LINES_BEFORE_RBRACE = 1
        commonSettings.CLASS_BRACE_STYLE = 1
        commonSettings.METHOD_BRACE_STYLE = 1
        commonSettings.SPECIAL_ELSE_IF_TREATMENT = true
        commonSettings.ALIGN_MULTILINE_PARAMETERS = false
        commonSettings.ALIGN_MULTILINE_FOR = false
        commonSettings.SPACE_AFTER_TYPE_CAST = true
        commonSettings.CALL_PARAMETERS_WRAP = 1
        commonSettings.METHOD_PARAMETERS_WRAP = 1
        commonSettings.EXTENDS_LIST_WRAP = 1
        commonSettings.EXTENDS_KEYWORD_WRAP = 1
        commonSettings.METHOD_CALL_CHAIN_WRAP = 1
        commonSettings.BINARY_OPERATION_WRAP = 1
        commonSettings.TERNARY_OPERATION_WRAP = 1
        commonSettings.FOR_STATEMENT_WRAP = 1
        commonSettings.ARRAY_INITIALIZER_WRAP = 1
        commonSettings.ASSIGNMENT_WRAP = 1
        3.also { commonSettings.IF_BRACE_FORCE = it }
        3.also { commonSettings.DOWHILE_BRACE_FORCE = it }
        3.also { commonSettings.WHILE_BRACE_FORCE = it }
        3.also { commonSettings.FOR_BRACE_FORCE = it }

        val phpSettings: PhpCodeStyleSettings = settings.getCustomSettings(PhpCodeStyleSettings::class.java)
        phpSettings.PHPDOC_BLANK_LINE_BEFORE_TAGS = true
        phpSettings.PHPDOC_WRAP_LONG_LINES = true
        phpSettings.LOWER_CASE_BOOLEAN_CONST = true
        phpSettings.LOWER_CASE_NULL_CONST = true
        phpSettings.ELSE_IF_STYLE = PhpCodeStyleSettings.ElseIfStyle.SEPARATE
        phpSettings.KEEP_RPAREN_AND_LBRACE_ON_ONE_LINE = true
        phpSettings.SPACE_BEFORE_CLOSURE_LEFT_PARENTHESIS = false
    }
}
