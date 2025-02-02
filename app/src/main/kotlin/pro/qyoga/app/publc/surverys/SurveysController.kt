package pro.qyoga.app.publc.surverys

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController


@RestController
class SurveysController(
    private val processSurvey: ProcessSurveyOp
) {

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(PATH)
    fun handlePostSurvey(@RequestBody surveyRq: SurveyRq) {
        processSurvey(surveyRq)
    }

    companion object {
        const val PATH = "/surveys"
    }

}