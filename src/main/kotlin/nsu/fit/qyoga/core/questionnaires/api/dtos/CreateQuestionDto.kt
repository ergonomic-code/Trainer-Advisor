package nsu.fit.qyoga.core.questionnaires.api.dtos

import nsu.fit.qyoga.core.questionnaires.api.enums.QuestionType

data class CreateQuestionDto(
    val text: String?,
    val questionType: QuestionType,
    val photoId: Long?,
    val answers: List<CreateAnswerDto>
)