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

    /**
     * Получить страницу задания результатов опросников
     */
    @GetMapping("/edit/setResult")
    fun getSetResultPage(): String {
        httpSession.getAttribute("questionnaire") as CreateQuestionnaireDto?
            ?: throw QuestionnaireException("Невозможно добавить расшифровку результатов опросника")
        return "questionnaire/questionnaire-decoding"
    }

    /**
     * Удалить результат опросника
     */
    @DeleteMapping("/setResult/{resultId}")
    fun deleteResultRow(
        @PathVariable resultId: Long
    ): String {
        val questionnaire = httpSession.getAttribute("questionnaire") as CreateQuestionnaireDto?
            ?: throw ElementNotFound("Невозможно сохранить расшифровку результатов опросника")
        httpSession.setAttribute(
            "questionnaire",
            questionnaire.copy(decoding = questionnaire.decoding.filter { it.id != resultId }.toMutableList())
        )
        return "questionnaire/questionnaire-decoding::tableDecoding"
    }

    /**
     * Добавить результат опросника
     */
    @GetMapping("/setResult/addResult")
    fun addResultToQuestionnaire(): String {
        val questionnaire = httpSession.getAttribute("questionnaire") as CreateQuestionnaireDto?
            ?: throw ElementNotFound("Невозможно сохранить расшифровку результатов опросника")
        val lastId = if (questionnaire.decoding.isEmpty()) 0 else questionnaire.decoding.last().id + 1
        httpSession.setAttribute(
            "questionnaire",
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
        @ModelAttribute("questionnaire") questionnaireDto: CreateQuestionnaireDto,
    ): HttpStatus {
        val questionnaire = httpSession.getAttribute("questionnaire") as CreateQuestionnaireDto?
            ?: throw ElementNotFound("Невозможно сохранить расшифровку результатов опросника")
        val changedDecoding =  getDecodingById(questionnaireDto, resultId)
            ?: throw ElementNotFound("Выбранный расшифровка результатов не найдена")
        val decodingList = questionnaire.decoding.mapIndexed { idx, value ->
            if (value.id == resultId) changedDecoding else value
        }.toMutableList()
        httpSession.setAttribute(
            "questionnaire",
            questionnaire.copy(decoding = decodingList)
        )
        return HttpStatus.OK
    }

    /**
     * Задание результатов опросников
     */
    @PostMapping("/edit/setResult")
    fun saveResultsTable(
        @ModelAttribute("questionnaire") questionnaireDto: CreateQuestionnaireDto,
    ): String {
        val questionnaire = httpSession.getAttribute("questionnaire") as CreateQuestionnaireDto?
            ?: throw ElementNotFound("Невозможно сохранить расшифровку результатов опросника")
        questionnaireService.saveQuestionnaire(questionnaire.copy(decoding = questionnaireDto.decoding))
        httpSession.removeAttribute("questionnaire")
        return "redirect:/questionnaires"
    }

    fun getDecodingById(questionnaire: CreateQuestionnaireDto, decodingId: Long): DecodingDto? {
        return questionnaire.decoding.filter { it.id == decodingId }.getOrNull(0)
    }
}
