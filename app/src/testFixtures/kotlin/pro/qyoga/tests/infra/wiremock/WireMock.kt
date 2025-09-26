package pro.qyoga.tests.infra.wiremock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import org.slf4j.LoggerFactory


private val log = LoggerFactory.getLogger(WireMock::class.java)

object WireMock {

    private val wiremockDelegate = lazy {
        log.warn("Starting WireMock server on port 8089")
        val wireMockServer = WireMockServer(options().port(8089))
        wireMockServer.start()
        wireMockServer
    }

    val wiremock by wiremockDelegate

    fun reset() {
        if (wiremockDelegate.isInitialized()) {
            wiremock.resetAll()
        }
    }

}