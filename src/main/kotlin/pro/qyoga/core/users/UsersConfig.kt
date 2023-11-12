package pro.qyoga.core.users

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import pro.qyoga.core.users.internal.UserDetailsServiceImpl
import pro.qyoga.core.users.internal.UsersRepo
import pro.qyoga.infra.auth.AuthConfig
import pro.qyoga.infra.db.SdjConfig


@Import(SdjConfig::class, AuthConfig::class)
@Configuration
class UsersConfig(
    private val sdjConfig: SdjConfig
) {

    @Bean
    fun usersRepo() = UsersRepo(sdjConfig.jdbcAggregateTemplate())

    @Bean
    fun userDetailsService() = UserDetailsServiceImpl(usersRepo())

}