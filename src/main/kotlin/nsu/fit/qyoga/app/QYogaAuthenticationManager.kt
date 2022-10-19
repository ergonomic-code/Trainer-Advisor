package nsu.fit.qyoga.app


import nsu.fit.qyoga.core.users.AuthService
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.stereotype.Component

private const val BEARER = "Bearer "

@Component
class QyogaAuthenticationManager(
    private val authService: AuthService
) : AuthenticationManager {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun authenticate(authentication: Authentication): Authentication {
        val header = authentication.principal.toString()
        if (!header.startsWith(BEARER)) {
            log.debug("No 'Bearer ' prefix found in Authorization header")
            throw BadCredentialsException("Cannot parse authorization token")
        }

        val jwtString = header.substring(BEARER.length)
        val jwt = authService.parseJwt(jwtString)

        val roles = jwt.body["roles"] as? List<*>
        val authorities = roles
            ?.filterIsInstance(String::class.java)
            ?.map { SimpleGrantedAuthority(it) }

        return PreAuthenticatedAuthenticationToken(jwt.body.subject, jwtString, authorities)
    }

}
