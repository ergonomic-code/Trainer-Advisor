package nsu.fit.qyoga.core.users.ports

import nsu.fit.platform.web.errors.ExceptionResponse
import nsu.fit.platform.web.errors.MappingExceptionHandler
import nsu.fit.platform.web.errors.toExceptionResponse
import nsu.fit.qyoga.core.users.api.AuthService
import nsu.fit.qyoga.core.users.api.BadCredentials
import nsu.fit.qyoga.core.users.api.LoginRequest
import nsu.fit.qyoga.core.users.api.UsersException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.request.WebRequest

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


@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class UsersExceptionHandler : MappingExceptionHandler<UsersException>() {

    @ExceptionHandler(value = [UsersException::class])
    override fun handle(ex: UsersException, request: WebRequest): ResponseEntity<Any> =
        super.handle(ex, request)

    override fun mapExceptionToResponse(ex: UsersException, request: WebRequest): ExceptionResponse =
        when (ex) {
            is BadCredentials -> toExceptionResponse(request, ex, HttpStatus.UNAUTHORIZED)
        }

}
