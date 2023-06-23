package nsu.fit.platform.lang

import jakarta.servlet.http.HttpSession
import nsu.fit.qyoga.core.questionnaires.api.dtos.CreateQuestionnaireDto
import nsu.fit.qyoga.core.questionnaires.api.errors.QuestionnaireException

fun HttpSession.getQuestionnaireFromSession(): CreateQuestionnaireDto {
    return this.getAttribute("questionnaire") as CreateQuestionnaireDto?
        ?: throw QuestionnaireException("Ошибка извлечения опросника из сессии")
}

fun HttpSession.setQuestionnaireInSession(questionnaire: CreateQuestionnaireDto) {
    this.setAttribute("questionnaire", questionnaire)
}
