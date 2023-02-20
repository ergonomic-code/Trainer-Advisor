package nsu.fit.qyoga.core.users.internal

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import nsu.fit.qyoga.core.users.api.AuthService
import nsu.fit.qyoga.core.users.api.BadCredentials
import nsu.fit.qyoga.core.users.api.LoginRequest
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthServiceImpl(
    @Value("\${qyoga.auth.jwt-secret}") secretKey: String,
    private val usersRepo: UsersRepo,
    private val passwordEncoder: PasswordEncoder
) : AuthService {

    private val log = LoggerFactory.getLogger(javaClass)

    private val key = Keys.hmacShaKeyFor(secretKey.encodeToByteArray())

    private val jwtParser = Jwts.parserBuilder()
        .setSigningKey(key)
        .build()

    override fun login(request: LoginRequest): String {
        val user = usersRepo.findByUsername(request.username)

        if (user == null || !passwordEncoder.matches(request.password, user.passwordHash)) {
            throw BadCredentials()
        }

        return Jwts.builder()
            .setClaims(mapOf("roles" to user.roles))
            .setSubject(user.id.toString())
            .setIssuedAt(Date())
            .signWith(key)
            .compact()
    }

    override fun parseJwt(jwtString: String): Jws<Claims> =
        try {
            jwtParser.parseClaimsJws(jwtString)
        } catch (e: JwtException) {
            log.debug("Invalid jwt token received", e)
            throw BadCredentialsException("Cannot parse authorization token", e)
        }

}