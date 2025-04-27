package pro.qyoga.app.infra

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.errors.ResourceNotFoundException
import pro.qyoga.app.platform.notFound


@ControllerAdvice
class GlobalErrorHandler {

    private val log = LoggerFactory.getLogger(javaClass)

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(value = [ResourceNotFoundException::class])
    fun handleResourceNotFound(ex: ResourceNotFoundException): ModelAndView {
        log.info("Resource not found", ex)
        return notFound
    }

}