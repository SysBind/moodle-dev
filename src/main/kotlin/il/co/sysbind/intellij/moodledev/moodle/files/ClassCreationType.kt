package il.co.sysbind.intellij.moodledev.moodle.files

import com.intellij.openapi.util.Trinity
import com.jetbrains.php.PhpIcons
import javax.swing.Icon

data class ClassCreationType(val name: String, val icon: Icon, val templateName: String) :
    Trinity<String, Icon, String>(name, icon, templateName) {
    companion object {
        val CLASS = ClassCreationType("Class", PhpIcons.CLASS, "Moodle PHP Class")
        val INTERFACE = ClassCreationType("Interface", PhpIcons.INTERFACE, "Moodle PHP Interface")
        val TARIT = ClassCreationType("Tarit", PhpIcons.TRAIT, "Moodle PHP Trait")
        val BUNDLED = com.intellij.util.SmartList(CLASS, TARIT, INTERFACE)
    }

}
