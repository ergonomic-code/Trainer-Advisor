package nsu.fit.qyoga.platform.web.errors

import jakarta.validation.ConstraintViolationException
import nsu.fit.qyoga.platform.errors.ResourceNotFound
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest


@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
class GenericWebExceptionHandler : MappingExceptionHandler<Throwable>() {

    @ExceptionHandler(value = [Throwable::class])
    override fun handle(ex: Throwable, request: WebRequest): ResponseEntity<Any> {
        return super.handle(ex, request)
    }

    override fun mapExceptionToResponse(ex: Throwable, request: WebRequest): ExceptionResponse =
        when (ex) {
            is ResourceNotFound -> toExceptionResponse(request, ex, HttpStatus.NOT_FOUND)
            is ConstraintViolationException -> toExceptionResponse(request, ex, HttpStatus.BAD_REQUEST)
            else -> toExceptionResponse(request, ex, HttpStatus.INTERNAL_SERVER_ERROR)
        }

}
