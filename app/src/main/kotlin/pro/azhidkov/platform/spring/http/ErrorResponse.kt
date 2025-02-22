package pro.azhidkov.platform.spring.http

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import pro.azhidkov.platform.errors.DomainError
import java.net.URI
import java.time.Instant

/**
 * Небольшой адаптер для ProblemDetail, добавляющий timestamp к объекту деталей, конструктор, позволяющий
 * сразу полностью проинициализировать объект и несколько утилитных методов.
 */
class ErrorResponse(
    instance: URI?,
    status: Int,
    type: URI,
    title: String,
    detail: String,
    timestamp: Instant = Instant.now()
) : ProblemDetail() {

    constructor(cause: DomainError, status: HttpStatus) : this(
        null,
        status = status.value(),
        type = URI.create(cause.errorCode),
        title = status.name,
        detail = cause.message ?: status.name
    )

    init {
        check(status in 100..599) { "Invalid HTTP status code: $status" }

        super.setStatus(status)
        super.setInstance(instance)
        super.setType(type)
        super.setTitle(title)
        super.setDetail(detail)
        super.setProperty("timestamp", timestamp)
    }


    companion object {

        fun conflict(cause: DomainError): ErrorResponse = ErrorResponse(cause, HttpStatus.CONFLICT)

    }

}

fun ErrorResponse.toResponseEntity(): ResponseEntity<ErrorResponse> = ResponseEntity.of(this).build()
