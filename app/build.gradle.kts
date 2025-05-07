import kotlinx.kover.gradle.plugin.dsl.AggregationType
import kotlinx.kover.gradle.plugin.dsl.CoverageUnit
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	alias(libs.plugins.kotlin)
	alias(libs.plugins.kotlin.spring)

	alias(libs.plugins.spring.dependencyManagement)
	alias(libs.plugins.spring.boot)

	alias(libs.plugins.gitProperties)

	alias(libs.plugins.kover)
	id("java-test-fixtures")
}

group = "pro.qyoga"
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
	implementation(kotlin("reflect"))
	implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")
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
	testFixturesApi(testLibs.wiremock.kotlin)

	testFixturesImplementation(kotlin("reflect"))
	testFixturesImplementation("org.springframework.boot:spring-boot-starter-data-jdbc")
	testFixturesImplementation("org.springframework.boot:spring-boot-starter-web")
	testFixturesImplementation("org.springframework.boot:spring-boot-starter-security")
	testFixturesImplementation("com.fasterxml.jackson.core:jackson-databind")
	testFixturesImplementation(libs.minio)
	testFixturesImplementation(libs.ical4j)

	testFixturesImplementation("org.springframework.boot:spring-boot-starter-test")
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

tasks.withType<KotlinCompile> {
	kotlin {
		compilerOptions {
			freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all", "-Xwhen-guards")
			jvmTarget.set(JvmTarget.JVM_21)
		}
	}
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
   this.archiveFileName.set("qyoga.jar")
}

tasks.withType<Test> {
	this.systemProperty("file.encoding", "utf-8")
	useJUnitPlatform()
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