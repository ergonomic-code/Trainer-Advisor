package nsu.fit.qyoga.core.users.web

import nsu.fit.qyoga.core.users.AuthService
import nsu.fit.qyoga.core.users.LoginRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/users")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("login")
    fun login(@RequestBody request: LoginRequest): String {
        return authService.login(request)
    }

}