package nsu.fit.qyoga.core.questionnaires.api.dtos

import nsu.fit.qyoga.core.questionnaires.api.dtos.enums.QuestionType

class QuestionWithAnswersDto(
    var id: Long = 0,
    var title: String? = null,
    var questionType: QuestionType = QuestionType.SINGLE,
    var imageId: Long? = null,
    var questionnaireId: Long = 0,
    var answers: List<AnswerDto> = mutableListOf()
)
