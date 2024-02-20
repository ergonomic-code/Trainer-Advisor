package pro.qyoga.tests.cases.arch

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.library.Architectures
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import pro.qyoga.tests.infra.junit.SLOW_TEST

@Tag(SLOW_TEST)
class ArchTest {

    private val qyogaClasses: JavaClasses = ClassFileImporter()
        .withImportOption(
            ImportOption.DoNotIncludeTests()
        )
        .importPackages("pro")

    @Test
    fun `QYoga should conform to ergonomic system architecture`() {
        val testsAbstractionLayer = "Tests abstraction layer"
        Architectures.layeredArchitecture()
            .consideringAllDependencies()
            .layer(testsAbstractionLayer).definedBy("pro.qyoga.tests.(assertions|clients|fixture|infra|pages|platform)..")
            .layer("App").definedBy("pro.qyoga.app..")
            .layer("Core").definedBy("pro.qyoga.core..")
            .layer("Infra").definedBy("pro.qyoga.infra..")
            .layer("Platform").definedBy("pro.azhidkov.platform..")

            .whereLayer("App").mayOnlyBeAccessedByLayers(testsAbstractionLayer)
            .whereLayer("Core").mayOnlyBeAccessedByLayers("App", testsAbstractionLayer)
            .whereLayer("Infra").mayOnlyBeAccessedByLayers("Core", "App", testsAbstractionLayer)
            .whereLayer("Platform").mayOnlyBeAccessedByLayers("App", "Core", "Infra", testsAbstractionLayer)
            .check(qyogaClasses)
    }

}