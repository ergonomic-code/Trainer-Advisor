package nsu.fit.qyoga.core.questionnaires.api.dtos

import nsu.fit.qyoga.core.questionnaires.api.dtos.enums.QuestionType

data class CreateQuestionDto(
    var id: Long = 0,
    var title: String? = null,
    var questionType: QuestionType = QuestionType.SINGLE,
    var imageId: Long? = null,
    var answers: List<CreateAnswerDto> = mutableListOf()
) : java.io.Serializable
