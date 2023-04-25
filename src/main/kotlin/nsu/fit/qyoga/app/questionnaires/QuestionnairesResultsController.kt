package nsu.fit.qyoga.app.questionnaires

import nsu.fit.qyoga.core.questionnaires.api.dtos.DecodingDtoList
import nsu.fit.qyoga.core.questionnaires.api.services.DecodingService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/questionnaires/")
class QuestionnairesResultsController(
    private val decodingService: DecodingService
) {

    /**
     * Получить страницу задания результатов опросников
     */
    @GetMapping("{questionnaireId}/setResult")
    fun getSetResultPage(
        model: Model,
        @PathVariable questionnaireId: Long
    ): String {
        setResult(questionnaireId, model)
        return "questionnaire/questionnaire-decoding"
    }

    /**
     * Удалить результат опросника
     */
    @DeleteMapping("{questionnaireId}/setResult/{resultId}")
    fun deleteResultRow(
        model: Model,
        @PathVariable resultId: Long,
        @PathVariable questionnaireId: Long
    ): String {
        decodingService.deleteDecodingById(resultId)
        setResult(questionnaireId, model)
        return "questionnaire/questionnaire-decoding::tableDecoding"
    }

    /**
     * Добавить результат опросника
     */
    @GetMapping("{questionnaireId}/setResult/addResult")
    fun addResultToQuestionnaire(
        model: Model,
        @PathVariable questionnaireId: Long
    ): String {
        println(questionnaireId)
        decodingService.createNewDecoding(questionnaireId)
        setResult(questionnaireId, model)
        return "questionnaire/questionnaire-decoding::tableDecoding"
    }

    /**
     * Сохранение изменений в результатах опросника
     */
    @PostMapping("setResult/{resultId}/update")
    @ResponseBody
    fun updateResultsTableRow(
        @PathVariable resultId: Long,
        @ModelAttribute("results") results: DecodingDtoList,
    ): HttpStatus {
        results.decodingDtoList.forEach { result ->
            if (result.id == resultId) {
                decodingService.saveDecoding(result)
                return HttpStatus.OK
            }
        }
        return HttpStatus.BAD_REQUEST
    }

    /**
     * Задание результатов опросников
     */
    @PostMapping("setResult")
    fun saveResultsTable(
        @ModelAttribute("results") results: DecodingDtoList,
    ): String {
        decodingService.saveDecodingList(results.decodingDtoList)
        return "redirect:/questionnaires/"
    }

    fun setResult(
        questionnaireId: Long,
        model: Model
    ) {
        model.addAttribute(
            "results",
            DecodingDtoList(decodingService.findDecodingByQuestionnaireId(questionnaireId))
        )
    }
}
