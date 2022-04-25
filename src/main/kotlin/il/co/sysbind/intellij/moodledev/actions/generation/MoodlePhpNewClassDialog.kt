package il.co.sysbind.intellij.moodledev.actions.generation

import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.jetbrains.php.actions.PhpNewClassDialog
import com.jetbrains.php.actions.newClassDataProvider.ClassCreationType

class MoodlePhpNewClassDialog(project: Project, directory: PsiDirectory) : PhpNewClassDialog(project, directory) {

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
    }

    override fun canInheritSuperClasses(): Boolean {
        return if (DumbService.isDumb(myProject)) {
            false
        } else {
            val selectedClassCreationType = this.selectedClassCreationType
            canOverrideSuperClass(selectedClassCreationType) || canImplementInterface(selectedClassCreationType) ||
                    selectedClassCreationType.templateName == "Moodle PHP Class" || selectedClassCreationType.templateName == "Moodle PHP Interface"
        }
    }
}
