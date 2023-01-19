package nsu.fit.qyoga.platform.web.errors

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.PastOrPresent
import org.springframework.http.HttpStatus
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import java.time.Instant


data class ExceptionResponse(
    @NotBlank
    val path: String,
    @PastOrPresent
    val timestamp: Instant,
    val status: HttpStatus,
    val message: String?
)

fun toExceptionResponse(
    request: WebRequest,
    ex: Throwable,
    status: HttpStatus,
    message: String? = null
): ExceptionResponse = ExceptionResponse(
    (request as? ServletWebRequest)?.request?.requestURI.toString(),
    Instant.now(),
    status,
    message ?: ex.message ?: ex.toString()
)
