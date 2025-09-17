package pro.qyoga.tests.infra.test_config.spring

import com.github.tomakehurst.wiremock.WireMockServer
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pro.qyoga.tests.infra.wiremock.WireMock


@TestConfiguration
@Configuration
class WireMockConf {

    @Bean
    fun wireMockServer(): WireMockServer =
        WireMock.wiremock

}