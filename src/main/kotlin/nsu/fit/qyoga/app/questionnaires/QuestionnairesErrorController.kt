package nsu.fit.qyoga.app.questionnaires

import nsu.fit.qyoga.core.questionnaires.api.errors.QuestionnaireException
import org.springframework.ui.Model

import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class QuestionnairesErrorController {

    @ExceptionHandler(QuestionnaireException::class)
    fun handleError(
        exception: QuestionnaireException,
        model: Model
    ): String {
        model.addAttribute("errorText", exception.message)
        return "error-page"
    }
}