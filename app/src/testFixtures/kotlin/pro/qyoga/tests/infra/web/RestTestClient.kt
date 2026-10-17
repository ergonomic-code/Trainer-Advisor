package pro.qyoga.tests.infra.web

import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.client.JdkClientHttpRequestFactory
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter
import org.springframework.test.web.servlet.client.RestTestClient
import pro.qyoga.tests.infra.test_config.spring.baseUrl
import pro.qyoga.tests.infra.test_config.spring.context as testContext
import tools.jackson.databind.json.JsonMapper
import java.net.http.HttpClient


val mainRestTestClient: RestTestClient by lazy { createRestTestClient() }

val pageRestTestClient: RestTestClient by lazy { createRestTestClient(followRedirects = true) }

fun createRestTestClient(
    context: ConfigurableApplicationContext = testContext,
    followRedirects: Boolean = false
): RestTestClient {
    val httpClient = HttpClient.newBuilder()
        .followRedirects(if (followRedirects) HttpClient.Redirect.NORMAL else HttpClient.Redirect.NEVER)
        .build()
    return RestTestClient.bindToServer(JdkClientHttpRequestFactory(httpClient))
        .baseUrl(context.baseUrl)
        .defaultHeader("Content-Type", "application/json;charset=UTF-8")
        .configureMessageConverters {
            it.registerDefaults().withJsonConverter(JacksonJsonHttpMessageConverter(context.getBean(JsonMapper::class.java)))
        }
        .build()
}
