import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin)
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
    implementation(platform("org.springframework.boot:spring-boot-dependencies:${libs.versions.springBoot.get()}"))

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