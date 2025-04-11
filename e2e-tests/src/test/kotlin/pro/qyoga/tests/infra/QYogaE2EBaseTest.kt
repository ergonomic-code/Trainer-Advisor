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
import org.openqa.selenium.remote.RemoteWebDriver
import org.testcontainers.Testcontainers
import pro.qyoga.tests.infra.web.QYogaAppBaseTest


open class QYogaE2EBaseTest : QYogaAppBaseTest() {

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
    }

    @AfterEach
    fun tearDown() {
        // test
        Selenide.closeWebDriver()
    }

    companion object {

        @Suppress("unused")
        @JvmField
        @RegisterExtension
        val screenshotOnError: ScreenShooterExtension = ScreenShooterExtension(false)

    }

}