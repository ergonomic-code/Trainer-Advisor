package nsu.fit.qyoga.platform.web.errors

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


/**
 * Базовый класс для Spring обработчиков исключений модулей ядра.
 *
 * Главная задача наследника - реализовать метод `mapExceptionToResponse` для отображения доменного исключения на тело HTTP-ответа.
 *
 * Так же наследники должны переопределять метод handle для того, чтобы навесить @ExceptionHandler. Но это чисто технический
 * и быстрый трюк - скорее всего можно найти более элегантное решение.
 */
abstract class MappingExceptionHandler<T : Throwable> : ResponseEntityExceptionHandler() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    open fun handle(ex: T, request: WebRequest): ResponseEntity<Any> {
        logger.error("Request: ", ex)
        val body = mapExceptionToResponse(ex, request)
        return ResponseEntity(body, body.status)
    }

    protected abstract fun mapExceptionToResponse(ex: T, request: WebRequest): ExceptionResponse

}