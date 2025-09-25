package pro.qyoga.app.infra

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizedClientRepository
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository
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

    @Order(1)
    @Bean
    fun opsSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .securityMatcher("/ops/**")
            .csrf { it.disable() }
            .authorizeHttpRequests { requests ->
                requests

                    // Ops
                    .requestMatchers("/ops/**").hasAuthority(Role.ROLE_ADMIN.toString())
            }
            .httpBasic(withDefaults())
        return http.build()
    }

    @Order(2)
    @Bean
    fun mainSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { requests ->
                requests
                    // Therapist
                    .requestMatchers("/therapist/**").hasAnyAuthority(Role.ROLE_THERAPIST.toString())

                    // Public
                    .requestMatchers(
                        HttpMethod.GET,
                        "/",
                        "/offline.html",
                        "/manifest.json",
                        "/register",
                        "/oauth2/**",
                        "/components/**",
                        "/styles/**",
                        "/img/**",
                        "/js/**",
                        "/fonts/**",
                        "/vendor/**",
                        "/test/*"
                    )
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/login", "/register", "/surveys", "/error-p").permitAll()
                    .requestMatchers("/error/**").permitAll()

                    .requestMatchers("/**").denyAll()

                    .anyRequest().authenticated()
            }
            .formLogin { form: FormLoginConfigurer<HttpSecurity?> ->
                form
                    .loginPage("/login")
                    .defaultSuccessUrl("/therapist")
                    .failureForwardUrl("/error-p")
                    .permitAll()
            }
            .oauth2Client(withDefaults())
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
    fun authorizedClientRepository(): OAuth2AuthorizedClientRepository {
        return HttpSessionOAuth2AuthorizedClientRepository()
    }

    @Bean
    fun tokenRepository(): PersistentTokenRepository {
        val jdbcTokenRepositoryImpl = JdbcTokenRepositoryImpl()
        jdbcTokenRepositoryImpl.setDataSource(dataSource)
        return jdbcTokenRepositoryImpl
    }

}
