package pro.qyoga.infra.auth

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder


@Configuration
class AuthConfig {

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

}