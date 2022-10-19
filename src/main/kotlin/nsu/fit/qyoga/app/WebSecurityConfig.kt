package nsu.fit.qyoga.app

import nsu.fit.qyoga.core.users.model.Role
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter


@Configuration
class WebSecurityConfig(
    private val authenticationManager: AuthenticationManager
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
                    .antMatchers("/users/login").permitAll()
                    .anyRequest().authenticated()
            }
        // @formatter:on
        return http.build()
    }

    private fun requestHeaderAuthenticationFilter(authenticationManager: AuthenticationManager): RequestHeaderAuthenticationFilter {
        val requestHeaderAuthenticationFilter = RequestHeaderAuthenticationFilter()
        requestHeaderAuthenticationFilter.setPrincipalRequestHeader("Authorization")
        requestHeaderAuthenticationFilter.setAuthenticationManager(authenticationManager)
        requestHeaderAuthenticationFilter.setExceptionIfHeaderMissing(false)
        return requestHeaderAuthenticationFilter
    }

}