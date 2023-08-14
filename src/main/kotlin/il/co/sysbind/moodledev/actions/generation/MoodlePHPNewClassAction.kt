package il.co.sysbind.moodledev.actions.generation

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.util.PlatformIcons
import com.jetbrains.php.PhpIcons
import com.jetbrains.php.actions.PhpNewBaseAction
import com.jetbrains.php.actions.PhpNewClassDialog
import com.jetbrains.php.actions.newClassDataProvider.ClassCreationType
import com.jetbrains.php.config.PhpLanguageFeature
import com.jetbrains.php.lang.PhpFileType
import com.jetbrains.php.templates.PhpCreateFileFromTemplateDataProvider

class MoodlePHPNewClassAction : PhpNewBaseAction(CAPTION, "", PhpFileType.INSTANCE.icon), DumbAware {


    private companion object {
        private const val CAPTION = "Moodle Class File"
    }

    override fun getDataProvider(p0: Project, p1: PsiDirectory, p2: PsiFile?): PhpCreateFileFromTemplateDataProvider? {
        val dialog = object : PhpNewClassDialog(p0, p1) {

            override fun init() {
                super.init()
                this.myTemplateComboBox.removeAllItems()
                this.myTemplateComboBox.addItem(ClassCreationType("Class", PhpIcons.CLASS, "Moodle PHP Class"))
                this.myTemplateComboBox.addItem(ClassCreationType("Interface", PhpIcons.INTERFACE, "Moodle PHP Interface"))
                if (PhpLanguageFeature.TRAITS.isSupported(myProject)) {
                    this.myTemplateComboBox.addItem(ClassCreationType("Tarit", PhpIcons.TRAIT, "Moodle PHP Trait"))
                }
                if (PhpLanguageFeature.ENUM_CLASSES.isSupported(myProject)) {
                    this.myTemplateComboBox.addItem(ClassCreationType("Enum",
                        PlatformIcons.ENUM_ICON, "Moodle PHP Enum"))
                }

            }

            override fun canInheritSuperClasses(): Boolean {
                return false
            }
        };
        return if (!dialog.showAndGet()) null else dialog
    }
}
