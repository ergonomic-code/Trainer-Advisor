@file:Suppress("DEPRECATION")

package pro.qyoga.infra.test_config.spring.auth

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import pro.qyoga.core.users.UsersConfig


@Import(UsersConfig::class)
@TestConfiguration
class TestPasswordEncoderConfig(
    private val usersConfig: UsersConfig
) {

    @Suppress("DEPRECATION")
    @Bean
    fun fastPasswordEncoder() = NoOpPasswordEncoder.getInstance()

    @Bean
    fun daoAuthenticationProvider(): DaoAuthenticationProvider {
        val daoAuthenticationProvider = DaoAuthenticationProvider(fastPasswordEncoder())
        daoAuthenticationProvider.setUserDetailsService(usersConfig.userDetailsService())
        return daoAuthenticationProvider
    }

}