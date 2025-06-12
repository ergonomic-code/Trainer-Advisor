import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.spring.dependencyManagement) apply false
    alias(libs.plugins.spring.boot) apply false
}

// Почему-то внутри subprojects libs не доступен
val kotlinPlugin = libs.plugins.kotlin.asProvider().get().pluginId

subprojects {
    apply(plugin = kotlinPlugin)

    repositories {
        mavenCentral()
    }

    configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }

    tasks.withType<KotlinCompile> {
        compilerOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all", "-Xwhen-guards")
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    tasks.withType<Test> {
        this.systemProperty("file.encoding", "utf-8")
        useJUnitPlatform()
    }

}