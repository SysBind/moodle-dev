package il.co.sysbind.intellij.moodledev.actions.generation

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ObjectUtils
import com.intellij.util.containers.ContainerUtil
import com.jetbrains.php.actions.PhpNewBaseAction
import com.jetbrains.php.actions.PhpNewClassDataProvider
import com.jetbrains.php.codeInsight.PhpCodeInsightUtil
import com.jetbrains.php.lang.PhpLangUtil
import com.jetbrains.php.lang.inspections.classes.PhpAddMethodStubsQuickFix
import com.jetbrains.php.lang.psi.elements.Method
import com.jetbrains.php.lang.psi.elements.PhpClass
import com.jetbrains.php.lang.psi.elements.PhpPsiElement
import com.jetbrains.php.refactoring.extract.extractInterface.PhpExtractInterfaceProcessor
import com.jetbrains.php.templates.PhpCreateFileFromTemplateDataProvider
import java.util.function.BiConsumer

class MoodlePHPNewClassAction : PhpNewBaseAction(CAPTION, CAPTION) {

    val SUPERCLASSES_IMPLEMENTOR =
        BiConsumer { psiFile: PsiFile?, dataProvider: PhpNewClassDataProvider ->
            if (dataProvider.canInheritSuperClasses()) {
                val phpClass = PsiTreeUtil.findChildOfType(
                    psiFile,
                    PhpClass::class.java
                )
                if (phpClass != null) {
                    inheritSuperClasses(phpClass, dataProvider)
                    implementAbstractMethods(phpClass, dataProvider)
                }
            }
        }

    override fun getDataProvider(p0: Project, p1: PsiDirectory, p2: PsiFile?): PhpCreateFileFromTemplateDataProvider? {
        var dialog = MoodlePhpNewClassDialog(p0, p1)
        return if (!dialog.showAndGet()) null else dialog
    }

    override fun getActionName(): String {
        return CAPTION
    }

    override fun createFile(project: Project, dataProvider: PhpCreateFileFromTemplateDataProvider): PsiFile? {
        val classDataProvider = ObjectUtils.tryCast(
            dataProvider,
            PhpNewClassDataProvider::class.java
        )
        return if (classDataProvider == null) super.createFile(project, dataProvider) else createFile(
            project, dataProvider,
            { file: PsiFile? ->
                SUPERCLASSES_IMPLEMENTOR.accept(
                    file,
                    classDataProvider
                )
            }, this.actionName
        )
    }
    private fun inheritSuperClasses(phpClass: PhpClass, dataProvider: PhpNewClassDataProvider) {

        val scope = PhpCodeInsightUtil.findScopeForUseOperator(phpClass)
        overrideClass(phpClass, scope, dataProvider.superFqn)
        implementInterfaces(phpClass, scope, dataProvider.interfaceFqnsToImplement)
    }

    private fun overrideClass(phpClass: PhpClass, scope: PhpPsiElement?, superFqn: String?) {
        if (!StringUtil.isEmpty(superFqn) && !StringUtil.isNotEmpty(phpClass.superFQN)) {
            val classQualifiedName = if (scope != null) PhpCodeInsightUtil.createQualifiedName(
                scope,
                superFqn!!
            ) else superFqn!!
            PhpLangUtil.addExtendsClause(phpClass, classQualifiedName)
        }
    }

    private fun implementInterfaces(
        phpClass: PhpClass,
        scope: PhpPsiElement?,
        interfacesToImplement: Collection<String>
    ) {
        val implementedInterfaces: MutableSet<String> = ContainerUtil.newHashSet(*phpClass.interfaceNames)
        val var4: Iterator<*> = interfacesToImplement.iterator()
        while (var4.hasNext()) {
            val interfaceToImplement = var4.next() as String
            val interfaceFqn = PhpLangUtil.toFQN(interfaceToImplement)
            if (!StringUtil.isEmpty(interfaceToImplement) && implementedInterfaces.add(interfaceFqn)) {
                val interfaceQualifiedName =
                    if (scope != null) PhpCodeInsightUtil.createQualifiedName(scope, interfaceFqn) else interfaceFqn
                PhpExtractInterfaceProcessor.addImplementClause(phpClass.project, phpClass, interfaceQualifiedName)
            }
        }
    }

    private fun implementAbstractMethods(phpClass: PhpClass, dataProvider: PhpNewClassDataProvider) {
        if (dataProvider.shouldImplementAbstractMethods()) {
            PhpAddMethodStubsQuickFix.addMethodStubs(
                phpClass.project, phpClass,
                getAbstractMethodsToImplement(phpClass)!!
            )
        }
    }

    private fun getAbstractMethodsToImplement(phpClass: PhpClass): Collection<Method?>? {
        return ContainerUtil.filter(phpClass.methods) { method -> method.isAbstract && method.containingClass !== phpClass }
    }
    private companion object {
        private const val CAPTION = "Moodle PHP Class"
    }
}
