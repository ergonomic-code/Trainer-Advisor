package pro.qyoga.tests.infra.wiremock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options


object WireMock {

    private val wiremockDelegate = lazy {
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