import kotlinx.kover.gradle.plugin.dsl.AggregationType
import kotlinx.kover.gradle.plugin.dsl.CoverageUnit

plugins {
	alias(libs.plugins.kotlin.spring)

	alias(libs.plugins.spring.dependencyManagement)
	alias(libs.plugins.spring.boot)

	alias(libs.plugins.gitProperties)

	alias(libs.plugins.kover)
	id("java-test-fixtures")
}

group = "pro.qyoga"
version = "0.0.1-SNAPSHOT"

dependencies {
	implementation(kotlin("reflect"))
	implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
	implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.flywaydb:flyway-database-postgresql")
	implementation(libs.jackarta.validation)
	implementation(libs.thymeleaf.extras.java8time)
	implementation(libs.postgres)
	implementation(libs.minio)
	implementation(libs.bundles.poi)
    implementation(libs.nanocaptcha)
	implementation(libs.ical4j)
    implementation(libs.google.api.client)
    implementation(libs.google.calendar.api)
    implementation(libs.google.oauth.client)
    implementation(platform("com.google.auth:google-auth-library-bom:1.30.1"))
    implementation("com.google.auth:google-auth-library-oauth2-http")

	developmentOnly("org.springframework.boot:spring-boot-docker-compose")
	developmentOnly("org.springframework.boot:spring-boot-devtools")

	testFixturesApi("org.springframework.boot:spring-boot-testcontainers")
	testFixturesApi(testLibs.kotest.assertions)
    testFixturesApi(testLibs.kotest.runner)
	testFixturesApi(testLibs.kotest.datatest)
	testFixturesApi(testLibs.jsoup)
	testFixturesApi(testLibs.datafaker)
	testFixturesApi(testLibs.greenmail)
	testFixturesApi(testLibs.instancio)
	testFixturesApi(testLibs.wiremock) {
		exclude("org.eclipse.jetty", "jetty-servlet")
		exclude("org.eclipse.jetty", "jetty-servlets")
		exclude("org.eclipse.jetty", "jetty-webapp")
		exclude("org.eclipse.jetty.http2", "http2-server")
	}
	testFixturesApi(testLibs.wiremock.jetty12)

	testFixturesImplementation(kotlin("reflect"))
	testFixturesImplementation("org.springframework.boot:spring-boot-starter-data-jdbc")
	testFixturesImplementation("org.springframework.boot:spring-boot-starter-web")
	testFixturesImplementation("org.springframework.boot:spring-boot-starter-security")
	testFixturesImplementation("com.fasterxml.jackson.core:jackson-databind")
	testFixturesImplementation(libs.minio)
	testFixturesImplementation(libs.ical4j)

	testFixturesImplementation("org.springframework.boot:spring-boot-starter-test")
    testFixturesImplementation("org.springframework.security:spring-security-test")
	testFixturesImplementation("org.testcontainers:junit-jupiter")
	testFixturesImplementation("org.testcontainers:postgresql")
	testFixturesImplementation(testLibs.testcontainers.minio)

	testImplementation(testFixtures(project(":app")))
	testImplementation(testLibs.bundles.restassured)
	testImplementation(testLibs.archunit)
	testImplementation(testLibs.mockito.kotlin)
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webflux")

	modules {
		module("org.codehaus.groovy:groovy") {
			replacedBy("org.apache.groovy:groovy", "conflicts in current rest-assured version")
		}
		module("org.codehaus.groovy:groovy-xml") {
			replacedBy("org.apache.groovy:groovy-xml", "conflicts in current rest-assured version")
		}
	}
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
   this.archiveFileName.set("qyoga.jar")
}

kover {
	currentProject {
		createVariant("Endpoints") {
			add("jvm")
		}
		createVariant("Ops") {
			add("jvm")
		}
	}
	reports {
		total {
			html {
				onCheck = true
			}
		}

		verify {
			rule("Line coverage") {
				disabled = false
				groupBy = kotlinx.kover.gradle.plugin.dsl.GroupingEntityType.APPLICATION

				bound {
					minValue = 90
					maxValue = 100
					coverageUnits.set(CoverageUnit.LINE)
					aggregationForGroup.set(AggregationType.COVERED_PERCENTAGE)
				}
			}
		}

		variant("Endpoints") {

			html {
				onCheck = true
			}
			filters {
				includes {
					classes("pro.qyoga.**Controller")
				}
			}

			verify {
				onCheck = true
				rule("Endpoints coverage") {
					disabled = false
					groupBy = kotlinx.kover.gradle.plugin.dsl.GroupingEntityType.CLASS

					bound {
						minValue = 90
						coverageUnits.set(CoverageUnit.INSTRUCTION)
						aggregationForGroup.set(AggregationType.COVERED_PERCENTAGE)
					}
				}
			}
		}

		variant("Ops") {

			html {
				onCheck = true
			}
			filters {
				includes {
					classes("pro.qyoga.**Op")
				}
			}

			verify {
				onCheck = true
				rule("Endpoints coverage") {
					disabled = false
					groupBy = kotlinx.kover.gradle.plugin.dsl.GroupingEntityType.CLASS

					bound {
						minValue = 90
						coverageUnits.set(CoverageUnit.INSTRUCTION)
						aggregationForGroup.set(AggregationType.COVERED_PERCENTAGE)
					}
				}
			}
		}

		variant("Ops") {

			html {
				onCheck = true
			}
			filters {
				includes {
					classes("pro.qyoga.**Op")
				}
			}

			verify {
				onCheck = true
				rule("Endpoints coverage") {
					disabled = false
					groupBy = kotlinx.kover.gradle.plugin.dsl.GroupingEntityType.CLASS

					bound {
						minValue = 87
						coverageUnits.set(CoverageUnit.INSTRUCTION)
						aggregationForGroup.set(AggregationType.COVERED_PERCENTAGE)
					}
				}
			}
		}
	}
}
gitProperties {
	val dotGit = project.rootDir.resolve(".git")
	if (dotGit.isFile) {
		// Костыль для работы gitProperties в директории дополнительного worktree (см. https://git-scm.com/docs/git-worktree)
		// ВАЖНО: данные для git.properties будут взяты из основной директории репозитория
		val actualGitDir = dotGit.readText().substringAfter("gitdir: ").substringBefore("/worktrees")
		this.dotGitDirectory.set(File(actualGitDir))
	} else {
		this.dotGitDirectory.set(dotGit)
	}
}

// см. https://detekt.dev/docs/gettingstarted/gradle/#dependencies
// https://github.com/detekt/detekt/issues/7384
configurations.matching { it.name == "detekt" }.all {
	resolutionStrategy.eachDependency {
		if (requested.group == "org.jetbrains.kotlin") {
			@Suppress("UnstableApiUsage")
			useVersion(io.gitlab.arturbosch.detekt.getSupportedKotlinVersion())
		}
	}
}
