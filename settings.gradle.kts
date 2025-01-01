rootProject.name = "QYoga"

include("app", "e2e-tests")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            // plugin versions
            val kotlinVersion = version("kotlin", "2.1.0")
            val springBootVersion = version("springBoot", "3.4.0")
            val springDependencyManagementVersion = version("springDependencyManagement", "1.1.6")
            val koverVersion = version("kover", "0.8.3")
            val gitPropertiesVersion = version("gitProperties", "2.4.2")

            // lib versions
            val poiVersion = version("poi", "5.3.0")

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
            library("jackarta-validation", "jakarta.validation", "jakarta.validation-api").version("3.1.0")
            library(
                "thymeleaf-extras-java8time",
                "org.thymeleaf.extras",
                "thymeleaf-extras-java8time"
            ).version("3.0.4.RELEASE")

            library("postgres", "org.postgresql", "postgresql").version("42.7.4")
            library("minio", "io.minio", "minio").version("8.5.14")

            library("poi-ooxml", "org.apache.poi", "poi-ooxml").versionRef(poiVersion)
            library("poi-ooxml-lite", "org.apache.poi", "poi-ooxml-lite").versionRef(poiVersion)

            bundle("poi", listOf("poi-ooxml", "poi-ooxml-lite"))

            library("nanocaptcha", "net.logicsquad", "nanocaptcha").version("2.1")
        }

        create("testLibs") {
            val selenideVersion = version("selenide", "7.6.1")
            val testContainersVersion = version("testcontainers", "1.20.4")
            val restAssuredVersion = version("restAssured", "5.5.0")

            library("selenide-proxy", "com.codeborne", "selenide-proxy").versionRef(selenideVersion)
            library("testcontainers-selenium", "org.testcontainers", "selenium").versionRef(testContainersVersion)

            library("kotest-assertions", "io.kotest", "kotest-assertions-core").version("5.9.1")

            library("restassured-core", "io.rest-assured", "rest-assured").versionRef(restAssuredVersion)
            library("restassured-kotlin", "io.rest-assured", "kotlin-extensions").versionRef(restAssuredVersion)
            library(
                "restassured-json-schema",
                "io.rest-assured",
                "json-schema-validator"
            ).versionRef(restAssuredVersion)
            bundle("restassured", listOf("restassured-core", "restassured-kotlin", "restassured-json-schema"))

            library("jsoup", "org.jsoup", "jsoup").version("1.18.1")
            library("datafaker", "net.datafaker", "datafaker").version("2.4.2")
            library("greenmail", "com.icegreen", "greenmail-junit5").version("2.1.2")

            library("testcontainers-minio", "org.testcontainers", "minio").versionRef(testContainersVersion)

            library("mockito-kotlin", "org.mockito.kotlin", "mockito-kotlin").version("5.4.0")
            library("archunit", "com.tngtech.archunit", "archunit").version("1.3.0")
        }
    }
}