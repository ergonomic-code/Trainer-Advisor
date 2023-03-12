package nsu.fit.qyoga.app

import nsu.fit.qyoga.core.users.api.Role
import nsu.fit.qyoga.core.users.internal.UserDetailsServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
class WebSecurityConfig(
    private val userDetailsService: UserDetailsServiceImpl,
    private val encoder: PasswordEncoder,
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        // @formatter:off
        http
            .anonymous().principal(Role.ROLE_ANONYMOUS.toString())
            .and()
            .csrf().disable()
            .authorizeHttpRequests { authz ->
                authz
                    .requestMatchers("/therapist/**").hasAuthority(Role.ROLE_THERAPIST.toString())
                    .requestMatchers("/users/login", "/error").permitAll()
                    .requestMatchers("/exercises/**").permitAll()
                    .requestMatchers("//questionnaires**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/styles/**", "/img/**").permitAll()
                    .anyRequest().authenticated()
            }
            .formLogin()
            .loginPage("/users/login")
            .defaultSuccessUrl("/therapist/main")
            .permitAll()
        http.authenticationProvider(authenticationProvider())
        // @formatter:on
        return http.build()
    }

    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userDetailsService)
        authProvider.setPasswordEncoder(encoder)
        return authProvider
    }

}
