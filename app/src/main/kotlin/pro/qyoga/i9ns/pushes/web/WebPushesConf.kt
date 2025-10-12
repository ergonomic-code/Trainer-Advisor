package pro.qyoga.i9ns.pushes.web

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration


@Configuration
@ComponentScan
@EnableConfigurationProperties(WebPushesConfProps::class)
class WebPushesConf
