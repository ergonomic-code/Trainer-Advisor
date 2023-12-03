package pro.qyoga.app.auth

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository
import pro.qyoga.core.users.UsersConfig
import pro.qyoga.core.users.api.Role
import pro.qyoga.infra.auth.AuthConfig
import java.time.Duration
import javax.sql.DataSource


@Import(AuthConfig::class, UsersConfig::class)
@Configuration
class WebSecurityConfig(
    private val dataSource: DataSource
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { requests ->
                requests
                    .requestMatchers("/therapist/**").hasAnyAuthority(Role.ROLE_THERAPIST.toString())
                    .requestMatchers("/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/styles/**", "/img/**", "/js/**").permitAll()
                    .anyRequest().authenticated()
            }
            .formLogin { form: FormLoginConfigurer<HttpSecurity?> ->
                form
                    .loginPage("/login")
                    .defaultSuccessUrl("/")
                    .failureForwardUrl("/error-p")
                    .permitAll()
            }
            .logout { logout: LogoutConfigurer<HttpSecurity?> -> logout.permitAll() }
            .rememberMe { rememberMeConfigurer ->
                rememberMeConfigurer
                    .alwaysRemember(true)
                    .tokenRepository(tokenRepository())
                    .tokenValiditySeconds(Duration.ofDays(9).toSeconds().toInt())
            }
        return http.build()
    }

    @Bean
    fun tokenRepository(): PersistentTokenRepository {
        val jdbcTokenRepositoryImpl = JdbcTokenRepositoryImpl()
        jdbcTokenRepositoryImpl.setDataSource(dataSource)
        return jdbcTokenRepositoryImpl
    }

}