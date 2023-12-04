package pro.qyoga.app.common

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import pro.qyoga.app.auth.WebSecurityConfig


@Import(WebSecurityConfig::class)
@ComponentScan
@Configuration
class WebAppsCommonConfig