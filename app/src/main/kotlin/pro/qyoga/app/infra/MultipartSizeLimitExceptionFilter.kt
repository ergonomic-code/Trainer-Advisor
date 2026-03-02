package pro.qyoga.app.infra

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException
import org.slf4j.LoggerFactory
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.multipart.MaxUploadSizeExceededException

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class MultipartSizeLimitExceptionFilter : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (ex: Exception) {
            if (!isMultipartSizeLimitExceeded(ex)) {
                throw ex
            }

            log.info("Rejected oversized multipart request: {} {}", request.method, request.requestURI, ex)

            if (response.isCommitted) {
                throw ex
            }

            response.resetBuffer()
            response.status = HttpStatus.PAYLOAD_TOO_LARGE.value()
        }
    }

    private fun isMultipartSizeLimitExceeded(ex: Throwable): Boolean {
        var current: Throwable? = ex

        while (current != null) {
            if (current is MaxUploadSizeExceededException || current is SizeLimitExceededException) {
                current.printStackTrace()
                return true
            }
            current = current.cause
        }

        return false
    }
}
