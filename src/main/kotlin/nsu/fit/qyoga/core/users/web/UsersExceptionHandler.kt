package nsu.fit.qyoga.core.users.web

import nsu.fit.qyoga.core.users.BadCredentials
import nsu.fit.qyoga.core.users.UsersException
import nsu.fit.qyoga.platform.web.errors.ExceptionResponse
import nsu.fit.qyoga.platform.web.errors.MappingExceptionHandler
import nsu.fit.qyoga.platform.web.errors.toExceptionResponse
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest


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