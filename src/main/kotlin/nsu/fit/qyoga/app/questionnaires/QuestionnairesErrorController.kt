package nsu.fit.qyoga.app.questionnaires

import nsu.fit.platform.errors.ResourceNotFound
import nsu.fit.qyoga.core.questionnaires.api.errors.AnswerException
import nsu.fit.qyoga.core.questionnaires.api.errors.ImageException
import nsu.fit.qyoga.core.questionnaires.api.errors.QuestionException
import nsu.fit.qyoga.core.questionnaires.api.errors.QuestionnaireException
import org.springframework.ui.Model

import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class QuestionnairesErrorController {

    @ExceptionHandler(QuestionnaireException::class)
    fun handleQuestionnaireException(
        exception: QuestionnaireException,
        model: Model
    ): String {
        model.addAttribute("errorText", exception.message)
        return "error-page"
    }

    @ExceptionHandler(QuestionException::class, ImageException::class, AnswerException::class)
    fun handleQuestionException(
        exception: ResourceNotFound,
        model: Model
    ): String {
        model.addAttribute("errorText", exception.message)
        return ""
    }
}