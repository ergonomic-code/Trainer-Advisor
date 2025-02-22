package pro.qyoga.tests.infra.web

import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.security.web.FilterChainProxy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.servlet.client.MockMvcWebTestClient
import org.springframework.web.context.WebApplicationContext
import pro.qyoga.tests.infra.test_config.spring.context


abstract class QYogaAppIntegrationBaseKoTest(body: QYogaAppIntegrationBaseKoTest.() -> Unit = {}) :
    QYogaAppBaseKoTest() {

    val baseUri = "http://localhost:$port"

    lateinit var client: WebTestClient

    var securityFilterChain: SecurityFilterChain = getBean("mainSecurityFilterChain")

    init {
        beforeSpec {
            client = MockMvcWebTestClient
                .bindToApplicationContext(context as WebApplicationContext)
                .apply(springSecurity(FilterChainProxy(securityFilterChain)))
                .configureClient()
                // без этого фильтра Spring Rest Docs пересоздаёт урл и попутно ломает киррелические символы в нём
                .baseUrl(baseUri)
                .defaultHeader("Content-Type", "application/json;charset=UTF-8")
                .build()
        }
        body()
    }

}