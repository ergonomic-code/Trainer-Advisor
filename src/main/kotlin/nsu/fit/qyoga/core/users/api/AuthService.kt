package nsu.fit.qyoga.core.users.api

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import nsu.fit.qyoga.core.users.internal.User

interface AuthService {

    fun login(request: LoginRequest): String

    fun parseJwt(jwtString: String): Jws<Claims>

}
