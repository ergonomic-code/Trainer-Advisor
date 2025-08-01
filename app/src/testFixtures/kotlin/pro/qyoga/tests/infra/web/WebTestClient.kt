package pro.qyoga.tests.infra.web

import org.springframework.context.ConfigurableApplicationContext
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.security.web.FilterChainProxy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.servlet.client.MockMvcWebTestClient
import org.springframework.web.context.WebApplicationContext
import pro.qyoga.tests.infra.test_config.spring.baseUrl


val mainWebTestClient: WebTestClient by lazy { createWebTestClient() }

fun createWebTestClient(context: ConfigurableApplicationContext = pro.qyoga.tests.infra.test_config.spring.context): WebTestClient {
    val mainSecurityFilterChain = context.getBean("mainSecurityFilterChain", SecurityFilterChain::class.java)
    return MockMvcWebTestClient
        .bindToApplicationContext(context as WebApplicationContext)
        .apply(springSecurity(FilterChainProxy(mainSecurityFilterChain)))
        .configureClient()
        .baseUrl(context.baseUrl)
        .defaultHeader("Content-Type", "application/json;charset=UTF-8")
        .build()
}
