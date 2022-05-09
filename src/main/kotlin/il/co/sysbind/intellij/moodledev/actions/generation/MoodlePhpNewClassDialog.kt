package il.co.sysbind.intellij.moodledev.actions.generation

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiDirectory
import com.intellij.ui.EditorTextField
import com.jetbrains.php.actions.PhpNewClassDialog
import com.jetbrains.php.actions.newClassDataProvider.ClassCreationType
import com.jetbrains.php.lang.PhpLangUtil
import com.jetbrains.php.templates.PhpFileTemplateUtil
import java.util.*

class MoodlePhpNewClassDialog(project: Project, directory: PsiDirectory) : PhpNewClassDialog(project, directory) {

    private val mySuperFqnTextField = EditorTextField()
    override fun init() {
        super.init()
        var template = ClassCreationType.BUNDLED.iterator()
        while (template.hasNext()) {
            val it = template.next() as ClassCreationType
            myTemplateComboBox.removeItem(it)
        }
        myTemplateComboBox.addItem(
            ClassCreationType(
                "Class", com.jetbrains.php.PhpIcons.CLASS, "Moodle PHP Class"
            )
        )
        myTemplateComboBox.addItem(
            ClassCreationType(
                "Interface", com.jetbrains.php.PhpIcons.INTERFACE, "Moodle PHP Interface"
            )
        )
        myTemplateComboBox.addItem(
            ClassCreationType(
            "Trait", com.jetbrains.php.PhpIcons.TRAIT, "Moodle PHP Trait"
            )
        )
        updateSuperClassesPanel()
    }

    private fun updateSuperClassesPanel() {
        mySuperFqnTextField.isEnabled = true
    }

    private fun getIgnoredProperties(): Properties {
        val properties = Properties()
        properties.setProperty("NAME", className)
        properties.setProperty("NAMESPACE", namespaceName)
        properties.setProperty("FILE_NAME", fileName)
        properties.setProperty("CARET", "")
        PhpFileTemplateUtil.fillDefaultProperties(
            FileTemplateManager.getInstance(myProject),
            properties,
            baseDirectory
        )

        return properties
    }

    override fun getSuperFqn(): String? {
        val superFqn = mySuperFqnTextField.text
        return if (StringUtil.isNotEmpty(superFqn)) PhpLangUtil.toFQN(superFqn!!) else null
    }
}
