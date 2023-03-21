package nsu.fit.qyoga.core.questionnaires.api.dtos

import nsu.fit.qyoga.core.questionnaires.api.enums.QuestionType

data class QuestionWithAnswersDto(
    val title: String?,
    val questionType: QuestionType,
    val imageDto: ImageDto?,
    val answers: MutableList<AnswerDto> = mutableListOf()
)
