package pro.qyoga.app.infra

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository
import pro.qyoga.core.users.auth.model.Role
import java.time.Duration
import javax.sql.DataSource


@Configuration
class WebSecurityConfig(
    private val dataSource: DataSource,
    @Value("\${trainer-advisor.auth.remember-me-time:9d}") private val rememberMeTime: Duration
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { requests ->
                requests
                    // Therapist
                    .requestMatchers("/therapist/**").hasAnyAuthority(Role.ROLE_THERAPIST.toString())

                    // Ops
                    .requestMatchers("/ops/**").hasAuthority(Role.ROLE_ADMIN.toString())

                    // Public
                    .requestMatchers(
                        HttpMethod.GET,
                        "/",
                        "/register",
                        "/components/**",
                        "/styles/**",
                        "/img/**",
                        "/js/**",
                        "/fonts/**",
                        "/test/*"
                    )
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/login", "/register", "/error-p").permitAll()
                    .requestMatchers("/error").permitAll()

                    .requestMatchers("/**").denyAll()

                    .anyRequest().authenticated()
            }
            .formLogin { form: FormLoginConfigurer<HttpSecurity?> ->
                form
                    .loginPage("/login")
                    .defaultSuccessUrl("/")
                    .failureForwardUrl("/error-p")
                    .permitAll()
            }
            .httpBasic(withDefaults())
            .logout { logout: LogoutConfigurer<HttpSecurity?> -> logout.permitAll() }
            .rememberMe { rememberMeConfigurer ->
                rememberMeConfigurer
                    .alwaysRemember(true)
                    .tokenRepository(tokenRepository())
                    .tokenValiditySeconds(rememberMeTime.toSeconds().toInt())
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