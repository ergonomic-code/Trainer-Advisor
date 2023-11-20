@file:Suppress("DEPRECATION")

package pro.qyoga.infra.test_config.spring.auth

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import pro.qyoga.core.users.UsersConfig


@Import(UsersConfig::class)
@TestConfiguration
class TestPasswordEncoderConfig(
    private val userDetailsService: UserDetailsService
) {

    @Suppress("DEPRECATION")
    @Bean
    fun fastPasswordEncoder(): PasswordEncoder = NoOpPasswordEncoder.getInstance()

    @Bean
    fun daoAuthenticationProvider(): DaoAuthenticationProvider {
        val daoAuthenticationProvider = DaoAuthenticationProvider(fastPasswordEncoder())
        daoAuthenticationProvider.setUserDetailsService(userDetailsService)
        return daoAuthenticationProvider
    }

}