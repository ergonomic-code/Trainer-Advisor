package pro.qyoga.tests.infra

import com.codeborne.selenide.WebDriverRunner
import com.codeborne.selenide.junit5.ScreenShooterExtension
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.RegisterExtension
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.RemoteWebDriver
import org.testcontainers.Testcontainers
import pro.qyoga.tests.infra.web.QYogaAppBaseTest


open class QYogaE2EBaseTest : QYogaAppBaseTest() {

    protected val baseUri = "http://host.testcontainers.internal:$port"

    @BeforeEach
    fun setUp() {
        Testcontainers.exposeHostPorts(port)
        val url = container.seleniumAddress
        WebDriverRunner.setWebDriver(RemoteWebDriver(url, ChromeOptions()))
    }

    @AfterEach
    fun tearDown() {
        WebDriverRunner.closeWebDriver()
    }

    companion object {

        @JvmField
        @RegisterExtension
        val screenshotOnError: ScreenShooterExtension = ScreenShooterExtension(false)

    }

}