package il.co.sysbind.intellij.moodledev.actions.generation

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ObjectUtils
import com.intellij.util.containers.ContainerUtil
import com.jetbrains.php.PhpIcons
import com.jetbrains.php.actions.PhpNewBaseAction
import com.jetbrains.php.actions.PhpNewClassDataProvider
import com.jetbrains.php.actions.PhpNewClassDialog
import com.jetbrains.php.actions.newClassDataProvider.ClassCreationType
import com.jetbrains.php.actions.statistics.PhpNewClassUsageLogger
import com.jetbrains.php.codeInsight.PhpCodeInsightUtil
import com.jetbrains.php.completion.insert.PhpReferenceInsertHandler
import com.jetbrains.php.lang.PhpLangUtil
import com.jetbrains.php.lang.inspections.classes.PhpAddMethodStubsQuickFix
import com.jetbrains.php.lang.intentions.PhpImportClassIntention
import com.jetbrains.php.lang.psi.PhpGroupUseElement.PhpUseKeyword
import com.jetbrains.php.lang.psi.PhpPsiElementFactory
import com.jetbrains.php.lang.psi.elements.Method
import com.jetbrains.php.lang.psi.elements.PhpClass
import com.jetbrains.php.lang.psi.elements.PhpPsiElement
import com.jetbrains.php.templates.PhpCreateFileFromTemplateDataProvider
import com.jetbrains.php.templates.PhpTemplatesSettings
import il.co.sysbind.intellij.moodledev.MoodleBundle
import il.co.sysbind.intellij.moodledev.project.MoodleProjectSettings
import il.co.sysbind.intellij.moodledev.util.MoodleCorePathUtil
import java.util.*
import java.util.function.BiConsumer

class MoodlePHPNewClassAction : PhpNewBaseAction(CAPTION, "", PhpIcons.PHP_FILE), DumbAware {

    val SUPERCLASSES_IMPLEMENTOR: BiConsumer<PsiFile, PhpNewClassDialog> =
        BiConsumer { psiFile: PsiFile?, dataProvider: PhpNewClassDialog ->
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

    private companion object {
        private val CAPTION = MoodleBundle.getMessage("action.class.caption")
    }

    private fun generateDialog(p0: Project, p1: PsiDirectory): PhpNewClassDialog {
        return object : PhpNewClassDialog(p0, p1) {

            private var myTemplateName: String = MoodleBundle.getMessage("action.class.template.default")
            private var myCustomProperty : Properties = Properties()

            init {
                classCreationTypeChanged()
            }

            private fun classCreationTypeChanged() {
                val selectedItem = selectedClassCreationType
                when (selectedItem) {
                    ClassCreationType.CLASS -> {
                        myTemplateName = MoodleBundle.getMessage("action.class.template.class")
                    }
                    ClassCreationType.INTERFACE -> {
                        myTemplateName = MoodleBundle.getMessage("action.class.template.interface")
                    }
                    ClassCreationType.TRAIT -> {
                        myTemplateName = MoodleBundle.getMessage("action.class.template.trait")
                    }
                    ClassCreationType.ENUM -> {
                        myTemplateName = MoodleBundle.getMessage("action.class.template.enum")
                    }
                }
            }

            override fun updateNamespacesSuggestions(namespaces: List<String?>) {
                val pluginName = MoodleCorePathUtil.getPluginName(p1)
                if (this.myNamespaceCombobox != null) {
                    val mainSuggestion = if (!namespaces.toString().startsWith(pluginName)) MoodleCorePathUtil.getNamespace(p1) else namespaces[0]!!
                    val suggestions: List<String?>? =
                        if (namespaces.size > 1) namespaces.subList(1, namespaces.size) else null
                    myNamespaceCombobox.updateItems(mainSuggestion, suggestions)
                }
            }

            override fun getTemplateName(): String {
                return myTemplateName
            }

            override fun hasCustomProperties(): Boolean {
                return false
            }

            // Get namespace only after the user submits the dialog
            override fun doOKAction() {
                PhpTemplatesSettings.getInstance(this.myProject).NEW_PHP_CLASS_LAST_EXTENSION = this.extension
                PhpNewClassUsageLogger.logNewClass(this.myProject, this)

                if (this.okAction.isEnabled) {
                    this.applyFields()
                    this.close(0)
                }
            }

            override fun getProperties(directory: PsiDirectory): Properties {
                myCustomProperty = super.getProperties(directory)
                val settings = project.getService(MoodleProjectSettings::class.java).settings
                myCustomProperty.setProperty("PLUGIN_NAME", MoodleCorePathUtil.getPluginName(directory))
                myCustomProperty.setProperty("USER_NAME", settings.userName)
                myCustomProperty.setProperty("USER_EMAIL", settings.userEmail)
                return myCustomProperty
            }
        }
    }

    override fun getDataProvider(p0: Project, p1: PsiDirectory, p2: PsiFile?): PhpNewClassDialog? {
        val dialog = generateDialog(p0, p1)
        return if (!dialog.showAndGet()) null else dialog
    }

    override fun createFile(project: Project, dataProvider: PhpCreateFileFromTemplateDataProvider): PsiFile? {
        val classDataProvider = ObjectUtils.tryCast(
            dataProvider,
            PhpNewClassDialog::class.java
        )
        return if (classDataProvider == null) super.createFile(project, dataProvider) else createFile(
            project, dataProvider,
            { file: PsiFile ->
                SUPERCLASSES_IMPLEMENTOR.accept(file, classDataProvider)
            },
            this.actionName
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
            if (PhpReferenceInsertHandler.shouldInsertImport(phpClass, phpClass, superFqn)) {
                PhpImportClassIntention.apply(
                    phpClass.project,
                    scope!!, superFqn, null as String?, null as String?, PhpUseKeyword.CLASS
                )
            }
        }
    }

    private fun implementInterfaces(
        phpClass: PhpClass,
        scope: PhpPsiElement?,
        interfacesToImplement: Collection<String>
    ) {

        val implementedInterfaces: MutableSet<String> = ContainerUtil.newHashSet(*phpClass.interfaceNames)
        val project = phpClass.project
        val var5: Iterator<*> = interfacesToImplement.iterator()

        while (var5.hasNext()) {
            val interfaceToImplement = var5.next() as String
            val interfaceFqn = PhpLangUtil.toFQN(interfaceToImplement)
            if (!StringUtil.isEmpty(interfaceToImplement) && implementedInterfaces.add(interfaceFqn)) {
                val interfaceQualifiedName =
                    if (scope != null) PhpCodeInsightUtil.createQualifiedName(scope, interfaceFqn) else interfaceFqn
                val implementsClause = PhpPsiElementFactory.createImplementsList(project, interfaceQualifiedName)
                phpClass.implementsList.add(implementsClause)
                if (PhpReferenceInsertHandler.shouldInsertImport(phpClass, phpClass, interfaceFqn)) {
                    PhpImportClassIntention.apply(
                        project,
                        scope!!, interfaceFqn, null as String?, null as String?, PhpUseKeyword.CLASS
                    )
                }
            }
        }
    }

    private fun implementAbstractMethods(phpClass: PhpClass, dataProvider: PhpNewClassDataProvider) {

        if (dataProvider.shouldImplementAbstractMethods()) {
            PhpAddMethodStubsQuickFix.addMethodStubs(
                phpClass.project,
                phpClass,
                getAbstractMethodsToImplement(phpClass)
            )
        }
    }

    private fun getAbstractMethodsToImplement(phpClass: PhpClass): Collection<Method> {

        return ContainerUtil.filter(
            phpClass.methods
        ) { method: Method -> method.isAbstract && method.containingClass !== phpClass }
    }
}
