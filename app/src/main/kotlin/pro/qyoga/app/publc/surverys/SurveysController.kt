package pro.qyoga.app.publc.surverys

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.request.WebRequest
import pro.azhidkov.platform.spring.http.ErrorResponse
import pro.azhidkov.platform.spring.http.toResponseEntity


@RestController
class SurveysController(
    private val processSurvey: ProcessSurveyOp
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(PATH)
    fun handlePostSurvey(@RequestBody surveyRq: SurveyRq) {
        processSurvey(surveyRq)
    }

    @ExceptionHandler(InvalidSurveyException::class)
    fun handleInvalidSurvey(
        request: WebRequest,
        e: InvalidSurveyException
    ): ResponseEntity<ErrorResponse> {
        log.warn("Invalid create survey request: {}", e.message)
        return ErrorResponse(e, HttpStatus.CONFLICT).toResponseEntity()
    }

    companion object {
        const val PATH = "/surveys"
    }

}