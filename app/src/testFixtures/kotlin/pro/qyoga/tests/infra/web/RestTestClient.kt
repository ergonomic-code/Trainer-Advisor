package pro.qyoga.tests.infra.web

import org.apache.hc.client5.http.impl.classic.HttpClients
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.test.web.servlet.client.RestTestClient
import pro.qyoga.tests.infra.test_config.spring.baseUrl


val mainRestTestClient: RestTestClient by lazy { createRestTestClient() }

fun createRestTestClient(context: ConfigurableApplicationContext = pro.qyoga.tests.infra.test_config.spring.context): RestTestClient =
    RestTestClient.bindToServer(
        HttpComponentsClientHttpRequestFactory(
            HttpClients.custom()
                .disableRedirectHandling()
                .build()
        )
    )
        .baseUrl(context.baseUrl)
        .defaultHeader("Content-Type", "application/json;charset=UTF-8")
        .build()
