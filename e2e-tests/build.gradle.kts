group = "pro.qyoga.e2e-tests"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(platform("org.springframework.boot:spring-boot-dependencies:${libs.versions.springBoot.get()}"))

    testImplementation(kotlin("reflect"))
    testImplementation(project(":app"))
    testImplementation(testFixtures(project(":app")))
    testImplementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    testImplementation(testLibs.selenide.proxy)
    testImplementation(testLibs.testcontainers.selenium)
}

tasks {
    named("test", Test::class) {
        systemProperties["selenide.browser"] = "chrome"
    }
}