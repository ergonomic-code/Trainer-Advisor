import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.spring.dependencyManagement)
    // см. https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/htmlsingle/#managing-dependencies.dependency-management-plugin.using-in-isolation
    alias(libs.plugins.spring.boot) apply false
}

the<DependencyManagementExtension>().apply {
    imports {
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    }
}

group = "pro.qyoga.e2e-tests"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("reflect"))
    testImplementation(project(":app"))
    testImplementation(testFixtures(project(":app")))
    testImplementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    testImplementation(testLibs.selenide.proxy)
    testImplementation(testLibs.testcontainers.selenium)
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all")
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    this.systemProperty("file.encoding", "utf-8")
    useJUnitPlatform()
}

tasks {
    named("test", Test::class) {
        systemProperties["selenide.browser"] = "chrome"
    }
}