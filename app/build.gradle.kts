import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.0"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.21"
	kotlin("plugin.spring") version "1.9.21"
	id("org.jetbrains.kotlinx.kover") version "0.6.1"
	id("java-test-fixtures")
	id("com.gorylenko.gradle-git-properties") version "2.4.1"
}

group = "pro.qyoga"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(21))
	}
}

testing {
	suites {
		val e2eTest by registering(JvmTestSuite::class) {
			testType = "e2e"
			dependencies {
				implementation(project())
				implementation(testFixtures(project()))
				implementation("com.codeborne:selenide-proxy:7.0.3")
				implementation("org.testcontainers:selenium:1.19.3")
				implementation("com.icegreen:greenmail-junit5:2.0.0")
			}
			sources {
				output.dir("e2eTest")
			}
		}
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
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.flywaydb:flyway-core")
	implementation("jakarta.validation:jakarta.validation-api:3.0.2")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.thymeleaf.extras:thymeleaf-extras-java8time:3.0.4.RELEASE")
	implementation("org.postgresql:postgresql:42.6.0")
	implementation("io.minio:minio:8.5.7")
	implementation("org.apache.poi:poi-ooxml:5.2.5")
	implementation("org.apache.poi:poi-ooxml-lite:5.2.5")

	developmentOnly("org.springframework.boot:spring-boot-docker-compose")
	developmentOnly("org.springframework.boot:spring-boot-devtools")

	testFixturesImplementation("org.springframework.boot:spring-boot-starter-test")
	testFixturesApi("org.springframework.boot:spring-boot-testcontainers")
	testFixturesImplementation("org.testcontainers:junit-jupiter")
	testFixturesImplementation("org.testcontainers:postgresql")
	testFixturesApi("io.kotest:kotest-assertions-core:5.7.2")
	testFixturesImplementation("org.testcontainers:minio:1.19.3")
	testFixturesApi("org.jsoup:jsoup:1.16.2")

	testImplementation(testFixtures(project(":")))
	testImplementation("io.rest-assured:rest-assured:5.3.2")
	testImplementation("io.rest-assured:kotlin-extensions:5.3.2")
	testImplementation("com.tngtech.archunit:archunit:1.1.0")
	testImplementation("io.github.ulfs:assertj-jsoup:0.1.4")
	testImplementation("com.icegreen:greenmail-junit5:2.0.0")
	implementation("org.mockito.kotlin:mockito-kotlin:5.2.1")

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
	named("e2eTest", Test::class) {
		systemProperties["selenide.browser"] = "chrome"
	}
}

kover {
	htmlReport {
		onCheck.set(true)
	}
	verify {
		onCheck.set(true)

		rule {
			isEnabled = true
			name = "Line coverage"

			target = kotlinx.kover.api.VerificationTarget.ALL

			bound {
				minValue = 85
				maxValue = 100
				counter = kotlinx.kover.api.CounterType.LINE
				valueType = kotlinx.kover.api.VerificationValueType.COVERED_PERCENTAGE
			}
		}


		rule {
			isEnabled = true
			name = "Endpoints coverage"

			target = kotlinx.kover.api.VerificationTarget.CLASS

			overrideClassFilter {
				includes += "pro.qyoga.**Controller"
			}

			bound {
				minValue = 100
				counter = kotlinx.kover.api.CounterType.INSTRUCTION
				valueType = kotlinx.kover.api.VerificationValueType.COVERED_PERCENTAGE
			}
		}
	}
}
