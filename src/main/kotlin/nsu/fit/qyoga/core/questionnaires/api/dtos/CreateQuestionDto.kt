package nsu.fit.qyoga.core.questionnaires.api.dtos

import nsu.fit.qyoga.core.questionnaires.api.enums.QuestionType

data class CreateQuestionDto(
    val id: Long = 0,
    val text: String = "",
    val questionType: QuestionType = QuestionType.SINGLE,
    val photoId: Long? = null,
    val answers: MutableList<CreateAnswerDto> = mutableListOf()
)