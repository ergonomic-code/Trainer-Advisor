import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.1.5"
	id("io.spring.dependency-management") version "1.1.3"
	kotlin("jvm") version "1.9.10"
	kotlin("plugin.spring") version "1.9.10"
	id("io.gitlab.arturbosch.detekt") version "1.23.3"
	id("org.jetbrains.kotlinx.kover") version "0.6.1"
}

group = "pro.qyoga"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(17))
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
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.flywaydb:flyway-core")
	implementation("jakarta.validation:jakarta.validation-api:3.0.2")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.postgresql:postgresql:42.6.0")
	implementation("io.minio:minio:8.5.7")

	developmentOnly("org.springframework.boot:spring-boot-docker-compose")

	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		// Когда этот jar есть в класспасе, спринг инициализирует Мокито, что добавляет 0.5 секунды ко времени теста
		exclude(group = "org.mockito")
	}
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:postgresql")
	testImplementation("io.rest-assured:rest-assured:5.3.2")
	testImplementation("io.rest-assured:kotlin-extensions:5.3.2")
	testImplementation("io.kotest:kotest-assertions-core:5.7.2")
	testImplementation("com.tngtech.archunit:archunit:1.1.0")
	testImplementation("org.jsoup:jsoup:1.16.2")
	testImplementation("io.github.ulfs:assertj-jsoup:0.1.4")

	detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.1")

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
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	this.systemProperty("file.encoding", "utf-8")
	useJUnitPlatform()
}

detekt {
	buildUponDefaultConfig = true
	allRules = false
	config.from(files("$projectDir/conf/qyoga-detekt.yaml"))
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
	reports {
		html.required.set(true)
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
