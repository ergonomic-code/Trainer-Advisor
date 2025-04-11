package pro.qyoga.tests.infra

import com.codeborne.selenide.Configuration
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.WebDriverRunner
import com.codeborne.selenide.junit5.ScreenShooterExtension
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.RegisterExtension
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.logging.LogType
import org.openqa.selenium.remote.RemoteWebDriver
import org.slf4j.LoggerFactory
import org.testcontainers.Testcontainers
import pro.qyoga.tests.infra.web.QYogaAppBaseTest


open class QYogaE2EBaseTest : QYogaAppBaseTest() {

    private val log = LoggerFactory.getLogger(javaClass)

    private val headless = true

    private val baseUri =
        if (headless) "http://host.testcontainers.internal:$port"
        else "http://localhost:$port"

    @BeforeEach
    fun setUp() {
        Configuration.baseUrl = baseUri
        if (headless) {
            Testcontainers.exposeHostPorts(port)
            val url = container.seleniumAddress
            WebDriverRunner.setWebDriver(RemoteWebDriver(url, ChromeOptions()))
        } else {
            WebDriverRunner.setWebDriver(ChromeDriver(ChromeOptions()))
        }

        Configuration.timeout = 10_000
        Configuration.pageLoadTimeout = 30_000
    }

    @AfterEach
    fun tearDown() {
        val logs = WebDriverRunner.getWebDriver()
            .manage()
            .logs()
            .get(LogType.BROWSER)
            .all
            .joinToString("\n") { it.toString() }
        log.info("Browser logs:\n{}", logs)
        Selenide.closeWebDriver()
    }

    companion object {

        @Suppress("unused")
        @JvmField
        @RegisterExtension
        val screenshotOnError: ScreenShooterExtension = ScreenShooterExtension(false)

    }

}