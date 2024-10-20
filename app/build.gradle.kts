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

	developmentOnly("org.springframework.boot:spring-boot-docker-compose")
	developmentOnly("org.springframework.boot:spring-boot-devtools")

	testFixturesApi("org.springframework.boot:spring-boot-testcontainers")
	testFixturesApi(testLibs.kotest.assertions)
	testFixturesApi(testLibs.jsoup)
	testFixturesApi(testLibs.datafaker)
	testFixturesApi(testLibs.greenmail)

	testFixturesImplementation("org.springframework.boot:spring-boot-starter-test")
	testFixturesImplementation("org.testcontainers:junit-jupiter")
	testFixturesImplementation("org.testcontainers:postgresql")
	testFixturesImplementation(testLibs.testcontainers.minio)

	testImplementation(testFixtures(project(":app")))
	testImplementation(testLibs.bundles.restassured)
	testImplementation(testLibs.archunit)
	testImplementation(testLibs.mockito.kotlin)

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
