package nsu.fit.qyoga.core.questionnaires.api.dtos

import nsu.fit.qyoga.core.questionnaires.api.dtos.enums.QuestionType

data class QuestionWithAnswersDto(
    val id: Long = 0,
    val title: String? = null,
    val questionType: QuestionType = QuestionType.SINGLE,
    val imageId: Long? = null,
    val questionnaireId: Long = 0,
    var answers: List<AnswerDto> = mutableListOf()
)
