@file:Suppress("DEPRECATION")

package pro.qyoga.tests.infra.test_config.spring.auth

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder


@TestConfiguration
class TestPasswordEncoderConfig {

    // Стандартный BCryptPasswordEncoder кодирует пароли по 300мс, что слишком расточительно для тестов
    @Suppress("DEPRECATION")
    @Primary
    @Bean
    fun fastPasswordEncoder(): PasswordEncoder = NoOpPasswordEncoder.getInstance()

}
