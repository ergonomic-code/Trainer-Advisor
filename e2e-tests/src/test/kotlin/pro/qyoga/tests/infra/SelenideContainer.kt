package pro.qyoga.tests.infra

import org.openqa.selenium.chrome.ChromeOptions
import org.testcontainers.containers.BrowserWebDriverContainer
import org.testcontainers.utility.DockerImageName


val container: BrowserWebDriverContainer<*> by lazy {
    BrowserWebDriverContainer(chromeImage())
        .withCapabilities(ChromeOptions())
        .withAccessToHost(true)
        .apply {
            start()
        }
}

fun chromeImage(): DockerImageName {
    return DockerImageName.parse("selenium/standalone-chrome:4.30.0-20250323")
}
