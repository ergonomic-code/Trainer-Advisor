package pro.qyoga.tests.infra.web

import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.web.reactive.server.WebTestClient
import pro.qyoga.tests.infra.test_config.spring.baseUrl


val mainWebTestClient: WebTestClient by lazy { createWebTestClient() }

fun createWebTestClient(context: ConfigurableApplicationContext = pro.qyoga.tests.infra.test_config.spring.context): WebTestClient =
    WebTestClient.bindToServer()
        .baseUrl(context.baseUrl)
        .defaultHeader("Content-Type", "application/json;charset=UTF-8")
        .build()