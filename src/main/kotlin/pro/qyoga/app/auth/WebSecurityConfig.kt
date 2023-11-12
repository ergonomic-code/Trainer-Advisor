package pro.qyoga.app.auth

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer
import org.springframework.security.web.SecurityFilterChain
import pro.qyoga.core.users.UsersConfig
import pro.qyoga.core.users.api.Role


@Import(UsersConfig::class)
@Configuration
@EnableWebSecurity
class WebSecurityConfig {

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
        return http.build()
    }

}