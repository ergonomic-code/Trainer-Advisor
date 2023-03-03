package nsu.fit.qyoga.app

import nsu.fit.qyoga.core.users.api.Role
import nsu.fit.qyoga.core.users.internal.UserDetailsServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter

@Configuration
class WebSecurityConfig(
    private val authenticationManager: AuthenticationManager,
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
            .addFilter(requestHeaderAuthenticationFilter(authenticationManager))
            .exceptionHandling().authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            .and()
            .authorizeHttpRequests { authz ->
                authz
                    .antMatchers(HttpMethod.POST, "/users").hasAuthority(Role.ROLE_ADMIN.toString())
                    .antMatchers("/users/login", "/error", "/").permitAll()
                    .antMatchers(HttpMethod.GET, "/css/**", "/img/**").permitAll()
                    .anyRequest().authenticated()
            }
            .formLogin()
            .loginPage("/users/login")
            .defaultSuccessUrl("/users")
            .permitAll()
        http.authenticationProvider(authenticationProvider())
        // @formatter:on
        return http.build()
    }

    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider {
        val authProvider: DaoAuthenticationProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userDetailsService)
        authProvider.setPasswordEncoder(encoder)
        return authProvider
    }
    private fun requestHeaderAuthenticationFilter(authenticationManager: AuthenticationManager): RequestHeaderAuthenticationFilter {
        val requestHeaderAuthenticationFilter = RequestHeaderAuthenticationFilter()
        requestHeaderAuthenticationFilter.setPrincipalRequestHeader("Authorization")
        requestHeaderAuthenticationFilter.setAuthenticationManager(authenticationManager)
        requestHeaderAuthenticationFilter.setExceptionIfHeaderMissing(false)
        return requestHeaderAuthenticationFilter
    }

}
