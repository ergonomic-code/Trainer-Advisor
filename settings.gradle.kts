rootProject.name = "QYoga"

include("app", "e2e-tests")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            // plugin versions
            val kotlinVersion = version("kotlin", "1.9.21")
            val springBootVersion = version("springBoot", "3.2.0")
            val springDependencyManagementVersion = version("springDependencyManagement", "1.1.4")
            val koverVersion = version("kover", "0.6.1")
            val gitPropertiesVersion = version("gitProperties", "2.4.1")

            // lib versions
            val poiVersion = version("poi", "5.2.5")

            // plugins
            plugin("kotlin", "org.jetbrains.kotlin.jvm").versionRef(kotlinVersion)
            plugin("kotlin.spring", "org.jetbrains.kotlin.plugin.spring").versionRef(kotlinVersion)

            plugin("spring.dependencyManagement", "io.spring.dependency-management").versionRef(
                springDependencyManagementVersion
            )
            plugin("spring.boot", "org.springframework.boot").versionRef(springBootVersion)

            plugin("kover", "org.jetbrains.kotlinx.kover").versionRef(koverVersion)
            plugin("gitProperties", "com.gorylenko.gradle-git-properties").versionRef(gitPropertiesVersion)

            // libs
            library("jackarta-validation", "jakarta.validation", "jakarta.validation-api").version("3.0.2")
            library(
                "thymeleaf-extras-java8time",
                "org.thymeleaf.extras",
                "thymeleaf-extras-java8time"
            ).version("3.0.4.RELEASE")

            library("postgres", "org.postgresql", "postgresql").version("42.6.0")
            library("minio", "io.minio", "minio").version("8.5.7")

            library("poi-ooxml", "org.apache.poi", "poi-ooxml").versionRef(poiVersion)
            library("poi-ooxml-lite", "org.apache.poi", "poi-ooxml-lite").versionRef(poiVersion)

            bundle("poi", listOf("poi-ooxml", "poi-ooxml-lite"))
        }

        create("testLibs") {
            val selenideVersion = version("selenide", "7.0.3")
            val testContainersVersion = version("testcontainers", "1.19.3")
            val restAssuredVersion = version("restAssured", "5.3.2")

            library("selenide-proxy", "com.codeborne", "selenide-proxy").versionRef(selenideVersion)
            library("testcontainers-selenium", "org.testcontainers", "selenium").versionRef(testContainersVersion)

            library("kotest-assertions", "io.kotest", "kotest-assertions-core").version("5.7.2")

            library("restassured-core", "io.rest-assured", "rest-assured").versionRef(restAssuredVersion)
            library("restassured-kotlin", "io.rest-assured", "kotlin-extensions").versionRef(restAssuredVersion)
            bundle("restassured", listOf("restassured-core", "restassured-kotlin"))

            library("jsoup", "org.jsoup", "jsoup").version("1.16.2")
            library("datafaker", "net.datafaker", "datafaker").version("2.0.2")
            library("greenmail", "com.icegreen", "greenmail-junit5").version("2.0.0")

            library("testcontainers-minio", "org.testcontainers", "minio").versionRef(testContainersVersion)

            library("mockito-kotlin", "org.mockito.kotlin", "mockito-kotlin").version("5.2.1")
            library("archunit", "com.tngtech.archunit", "archunit").version("1.1.0")
        }
    }
}