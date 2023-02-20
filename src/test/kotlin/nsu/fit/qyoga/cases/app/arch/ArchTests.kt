@file:Suppress("NoBlankLinesInChainedMethodCalls")
package nsu.fit.qyoga.cases.app.arch

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.library.Architectures.layeredArchitecture
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices
import org.junit.jupiter.api.Test

class ArchTests {

    private val qyogaClasses: JavaClasses = ClassFileImporter()
        .withImportOption(
            ImportOption.DoNotIncludeTests()
        )
        .importPackages("nsu.fit")

    @Test
    fun `QYoga should not contain cycles in packages`() {
        slices().matching("nsu.fit.qyoga.(**)")
            .should().beFreeOfCycles()
            .check(qyogaClasses)
    }

    @Test
    fun `QYoga should conform to ergonomic system architecture`() {
        layeredArchitecture()
            .consideringAllDependencies()
            .layer("App").definedBy("nsu.fit.qyoga.app..")
            .layer("Core").definedBy("nsu.fit.qyoga.core..")
            .layer("Platform").definedBy("nsu.fit.platform..")

            .whereLayer("App").mayNotBeAccessedByAnyLayer()
            .whereLayer("Core").mayOnlyBeAccessedByLayers("App")
            .whereLayer("Platform").mayOnlyBeAccessedByLayers("App", "Core")
            .check(qyogaClasses)
    }


    @Test
    fun `QYoga modules should conform to ergonomic structure`() {
        layeredArchitecture()
            .consideringOnlyDependenciesInLayers()
            .layer("Factory").definedBy("nsu.fit.qyoga.core.*")
            .layer("API").definedBy("nsu.fit.qyoga.core..api..")
            .layer("Internal").definedBy("nsu.fit.qyoga.core..internal..")
            .layer("Ports").definedBy("nsu.fit.qyoga.core..ports..")
            .layer("Api").definedBy("nsu.fit.qyoga.core..api..")
            .layer("Platform").definedBy("nsu.fit.platform..")

            .whereLayer("Factory").mayOnlyBeAccessedByLayers("Factory")
            .whereLayer("Internal").mayOnlyBeAccessedByLayers("Factory")
            .whereLayer("Ports").mayOnlyBeAccessedByLayers("Factory")
            .whereLayer("Api").mayOnlyAccessLayers("Api", "Platform")
            .check(qyogaClasses)
    }

}
