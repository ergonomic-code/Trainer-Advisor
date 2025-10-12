package pro.qyoga.i9ns.pushes.web

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties(prefix = "trainer-advisor.integrations.web-pushes")
data class WebPushesConfProps(
    val publicKey: String,
    val privateKey: String,
)
