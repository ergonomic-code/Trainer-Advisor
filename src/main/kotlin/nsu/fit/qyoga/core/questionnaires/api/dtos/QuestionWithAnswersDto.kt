package nsu.fit.qyoga.core.questionnaires.api.dtos

import nsu.fit.qyoga.core.questionnaires.api.enums.QuestionType

data class QuestionWithAnswersDto(
    val id: Long,
    val title: String?,
    val questionType: QuestionType,
    val imageId: Long?,
    val answers: MutableList<AnswerDto>
)
