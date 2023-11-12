package pro.qyoga.app.publc

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class PublicAppConfig {

    @Bean
    fun loginPageController() =
        LoginPageController()

    @Bean
    fun mainPageController() =
        MainPageController()

}