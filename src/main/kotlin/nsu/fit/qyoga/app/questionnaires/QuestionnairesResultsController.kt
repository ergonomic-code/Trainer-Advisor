package nsu.fit.qyoga.app.questionnaires

import jakarta.servlet.http.HttpSession
import nsu.fit.qyoga.core.questionnaires.api.dtos.CreateQuestionnaireDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.DecodingDto
import nsu.fit.qyoga.core.questionnaires.api.errors.ElementNotFound
import nsu.fit.qyoga.core.questionnaires.api.errors.QuestionnaireException
import nsu.fit.qyoga.core.questionnaires.api.services.QuestionnaireService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/therapist/questionnaires")
class QuestionnairesResultsController(
    private val questionnaireService: QuestionnaireService,
    private val httpSession: HttpSession
) {
    val questionnaireFieldName = "questionnaire"

    /**
     * Получить страницу задания результатов опросников
     */
    @GetMapping("/edit/setResult")
    fun getSetResultPage(): String {
        getQuestionnaireFromSession()
        return "questionnaire/questionnaire-decoding"
    }

    /**
     * Удалить результат опросника
     */
    @DeleteMapping("/setResult/{resultId}")
    fun deleteResultRow(
        @PathVariable resultId: Long
    ): String {
        val questionnaire = getQuestionnaireFromSession()
        setQuestionnaireInSession(
            questionnaire.copy(decoding = questionnaire.decoding.filter { it.id != resultId }.toMutableList())
        )
        return "questionnaire/questionnaire-decoding::tableDecoding"
    }

    /**
     * Добавить результат опросника
     */
    @GetMapping("/setResult/addResult")
    fun addResultToQuestionnaire(): String {
        val questionnaire = getQuestionnaireFromSession()
        val lastId = if (questionnaire.decoding.isEmpty()) 0 else questionnaire.decoding.last().id + 1
        setQuestionnaireInSession(
            questionnaire.copy(decoding = (questionnaire.decoding + DecodingDto(id = lastId)).toMutableList())
        )
        return "questionnaire/questionnaire-decoding::tableDecoding"
    }

    /**
     * Сохранение изменений в результатах опросника
     */
    @PostMapping("/setResult/{resultId}/update")
    @ResponseBody
    fun updateResultsTableRow(
        @PathVariable resultId: Long,
        @ModelAttribute("questionnaire") questionnaireDto: CreateQuestionnaireDto
    ): HttpStatus {
        val questionnaire = getQuestionnaireFromSession()
        val changedDecoding = questionnaireDto.decoding.filter { it.id == resultId }.getOrNull(0)
            ?: throw ElementNotFound("Выбранная расшифровка результатов не найдена")
        val decodingList = questionnaire.decoding.map { value ->
            if (value.id == resultId) changedDecoding else value
        }.toMutableList()
        setQuestionnaireInSession(questionnaire.copy(decoding = decodingList))
        return HttpStatus.OK
    }

    /**
     * Задание результатов опросников
     */
    @PostMapping("/edit/setResult")
    fun saveResultsTable(
        @ModelAttribute("questionnaire") questionnaireDto: CreateQuestionnaireDto
    ): String {
        val questionnaire = getQuestionnaireFromSession()
        questionnaireService.saveQuestionnaire(questionnaire.copy(decoding = questionnaireDto.decoding))
        httpSession.removeAttribute(questionnaireFieldName)
        return "redirect:/therapist/questionnaires"
    }

    fun getQuestionnaireFromSession(): CreateQuestionnaireDto {
        return httpSession.getAttribute(questionnaireFieldName) as CreateQuestionnaireDto?
            ?: throw QuestionnaireException("Ошибка извлечения опросника из сессии")
    }

    fun setQuestionnaireInSession(questionnaire: CreateQuestionnaireDto) {
        httpSession.setAttribute(
            questionnaireFieldName,
            questionnaire
        )
    }
}
