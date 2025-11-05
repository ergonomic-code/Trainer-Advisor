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
import pro.qyoga.tests.infra.test_config.spring.baseUrl
import pro.qyoga.tests.infra.test_config.spring.context
import pro.qyoga.tests.infra.web.QYogaAppBaseTest


open class QYogaE2EBaseTest : QYogaAppBaseTest() {

    private val log = LoggerFactory.getLogger(javaClass)

    private val headless = true

    private val baseUri =
        if (headless) "http://host.testcontainers.internal:$port"
        else context.baseUrl

    @BeforeEach
    fun setUp() {
        Configuration.baseUrl = baseUri
        Configuration.timeout = 5_000
        Configuration.pageLoadTimeout = 3_000

        val webDriver =
            if (headless) {
                Testcontainers.exposeHostPorts(port)
                RemoteWebDriver(container.seleniumAddress, ChromeOptions())
            } else {
                ChromeDriver(ChromeOptions())
            }
        WebDriverRunner.setWebDriver(webDriver)
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
