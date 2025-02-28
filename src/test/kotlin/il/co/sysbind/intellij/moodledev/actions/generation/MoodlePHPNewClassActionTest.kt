package il.co.sysbind.intellij.moodledev.actions.generation

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.php.lang.psi.elements.PhpClass
import com.jetbrains.php.lang.psi.elements.PhpPsiElement

class MoodlePHPNewClassActionTest : BasePlatformTestCase() {
    fun testImplementInterfaces() {
        // Create a PHP file with a class
        val phpFile = myFixture.configureByText(
            "test.php",
            """<?php
            class TestClass {
            }
            """
        )

        // Get the class from the file
        val phpClass = PsiTreeUtil.findChildOfType(phpFile, PhpClass::class.java)
        assertNotNull("PHP class should be created", phpClass)

        // Create an instance of MoodlePHPNewClassAction
        val action = MoodlePHPNewClassAction()

        // Call implementInterfaces through reflection since it's private
        val method = MoodlePHPNewClassAction::class.java.getDeclaredMethod(
            "implementInterfaces",
            PhpClass::class.java,
            PhpPsiElement::class.java,
            Collection::class.java
        )
        method.isAccessible = true

        // Test implementing an interface
        WriteCommandAction.runWriteCommandAction(project) {
            method.invoke(action, phpClass, phpClass, listOf("\\TestInterface"))
        }

        // Verify the interface was added
        assertEquals(
            "Interface should be implemented",
            "\\TestInterface",
            phpClass?.implementsList?.referenceElements?.firstOrNull()?.fqn
        )
    }
}
