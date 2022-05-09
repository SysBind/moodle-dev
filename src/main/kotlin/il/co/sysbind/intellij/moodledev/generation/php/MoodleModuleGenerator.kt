package il.co.sysbind.intellij.moodledev.generation.php

import com.intellij.facet.ui.ValidationResult
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.DirectoryProjectGenerator
import javax.swing.Icon

class MoodleModuleGenerator : DirectoryProjectGenerator<Any?> {
    var actionName: String = "Moodle Plugin"

    override fun getName(): String {
        return this.actionName
    }

    override fun getLogo(): Icon? {
        TODO("Not yet implemented")
    }

    override fun generateProject(project: Project, baseDir: VirtualFile, settings: Any, module: Module) {
        TODO("Not yet implemented")
    }

    override fun validate(baseDirPath: String): ValidationResult {
        TODO("Not yet implemented")
    }
}
