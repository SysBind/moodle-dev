package com.github.avimoto.moodledev.services

import com.intellij.openapi.project.Project
import com.github.avimoto.moodledev.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
