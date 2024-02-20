import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot")
	id("io.spring.dependency-management")
	kotlin("jvm")
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

	testFixturesApi("com.icegreen:greenmail-junit5:2.0.0")
	testFixturesApi("org.springframework.boot:spring-boot-testcontainers")
	testFixturesApi("io.kotest:kotest-assertions-core:5.7.2")
	testFixturesApi("org.jsoup:jsoup:1.16.2")
	testFixturesApi("net.datafaker:datafaker:2.0.2")
	testFixturesImplementation("org.springframework.boot:spring-boot-starter-test")
	testFixturesImplementation("org.testcontainers:junit-jupiter")
	testFixturesImplementation("org.testcontainers:postgresql")
	testFixturesImplementation("org.testcontainers:minio:1.19.3")

	testImplementation(testFixtures(project(":app")))
	testImplementation("io.rest-assured:rest-assured:5.3.2")
	testImplementation("io.rest-assured:kotlin-extensions:5.3.2")
	testImplementation("com.tngtech.archunit:archunit:1.1.0")
	testImplementation("io.github.ulfs:assertj-jsoup:0.1.4")
	testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")

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

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
   this.archiveFileName.set("qyoga.jar")
}

tasks.withType<Test> {
	this.systemProperty("file.encoding", "utf-8")
	useJUnitPlatform()
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
