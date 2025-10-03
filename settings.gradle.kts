rootProject.name = "QYoga"

include("app", "e2e-tests")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            // plugin versions
            val kotlinVersion = version("kotlin", "2.2.0")
            val springBootVersion = version("springBoot", "3.5.0")
            val springDependencyManagementVersion = version("springDependencyManagement", "1.1.7")
            val koverVersion = version("kover", "0.9.1")
            val gitPropertiesVersion = version("gitProperties", "2.5.3")
            val detektVersion = version("dekekt", "1.23.8")

            // lib versions
            val poiVersion = version("poi", "5.4.1")
            val caffeineVersion = version("caffeine", "3.1.8")
            val googleAuthBomVersion = version("google-auth-bom", "1.30.1")

            // plugins
            plugin("kotlin", "org.jetbrains.kotlin.jvm").versionRef(kotlinVersion)
            plugin("kotlin.spring", "org.jetbrains.kotlin.plugin.spring").versionRef(kotlinVersion)

            plugin("spring.dependencyManagement", "io.spring.dependency-management").versionRef(
                springDependencyManagementVersion
            )
            plugin("spring.boot", "org.springframework.boot").versionRef(springBootVersion)

            plugin("kover", "org.jetbrains.kotlinx.kover").versionRef(koverVersion)
            plugin("gitProperties", "com.gorylenko.gradle-git-properties").versionRef(gitPropertiesVersion)
            plugin("detekt", "io.gitlab.arturbosch.detekt").versionRef(detektVersion)

            // libs
            library("jackarta-validation", "jakarta.validation", "jakarta.validation-api").version("3.1.1")
            library(
                "thymeleaf-extras-java8time",
                "org.thymeleaf.extras",
                "thymeleaf-extras-java8time"
            ).version("3.0.4.RELEASE")

            library("postgres", "org.postgresql", "postgresql").version("42.7.6")
            library("minio", "io.minio", "minio").version("8.5.17")
            library("caffeine", "com.github.ben-manes.caffeine", "caffeine").versionRef(caffeineVersion)

            library("poi-ooxml", "org.apache.poi", "poi-ooxml").versionRef(poiVersion)
            library("poi-ooxml-lite", "org.apache.poi", "poi-ooxml-lite").versionRef(poiVersion)

            bundle("poi", listOf("poi-ooxml", "poi-ooxml-lite"))

            library("nanocaptcha", "net.logicsquad", "nanocaptcha").version("2.1")
            library("ical4j", "org.mnode.ical4j", "ical4j").version("4.1.1")

            library("google-api-client", "com.google.api-client", "google-api-client").version("2.0.0")
            library(
                "google-oauth-client",
                "com.google.oauth-client",
                "google-oauth-client-jetty"
            ).version("1.34.1")
            library(
                "google-calendar-api",
                "com.google.apis",
                "google-api-services-calendar"
            ).version("v3-rev20220715-2.0.0")
            library("google.auth.bom", "com.google.auth", "google-auth-library-bom").versionRef(googleAuthBomVersion)
        }

        create("testLibs") {
            val selenideVersion = version("selenide", "7.9.3")
            val testContainersVersion = version("testcontainers", "1.21.1")
            val restAssuredVersion = version("restAssured", "5.5.5")
            val kotestVersion = version("kotest", "5.9.1")
            val wiremockVersion = version("wiremock", "3.13.0")

            library("selenide-proxy", "com.codeborne", "selenide-proxy").versionRef(selenideVersion)
            library("testcontainers-selenium", "org.testcontainers", "selenium").versionRef(testContainersVersion)

            library("kotest-assertions", "io.kotest", "kotest-assertions-core").versionRef(kotestVersion)
            library("kotest-runner", "io.kotest", "kotest-runner-junit5").versionRef(kotestVersion)
            library("kotest-datatest", "io.kotest", "kotest-framework-datatest").versionRef(kotestVersion)

            library("restassured-core", "io.rest-assured", "rest-assured").versionRef(restAssuredVersion)
            library("restassured-kotlin", "io.rest-assured", "kotlin-extensions").versionRef(restAssuredVersion)
            library(
                "restassured-json-schema",
                "io.rest-assured",
                "json-schema-validator"
            ).versionRef(restAssuredVersion)
            bundle("restassured", listOf("restassured-core", "restassured-kotlin", "restassured-json-schema"))

            library("jsoup", "org.jsoup", "jsoup").version("1.20.1")
            library("datafaker", "net.datafaker", "datafaker").version("2.4.3")
            library("greenmail", "com.icegreen", "greenmail-junit5").version("2.1.3")

            library("testcontainers-minio", "org.testcontainers", "minio").versionRef(testContainersVersion)

            library("mockito-kotlin", "org.mockito.kotlin", "mockito-kotlin").version("5.4.0")
            library("archunit", "com.tngtech.archunit", "archunit").version("1.4.1")
            library("instancio", "org.instancio", "instancio-junit").version("5.4.1")

            library("wiremock", "org.wiremock", "wiremock").versionRef(wiremockVersion)
            library("wiremock-jetty12", "org.wiremock", "wiremock-jetty12").versionRef(wiremockVersion)
        }
    }
}
