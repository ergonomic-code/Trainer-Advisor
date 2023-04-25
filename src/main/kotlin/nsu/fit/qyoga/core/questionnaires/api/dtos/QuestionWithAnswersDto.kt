package nsu.fit.qyoga.core.questionnaires.api.dtos

import nsu.fit.qyoga.core.questionnaires.api.dtos.enums.QuestionType

class QuestionWithAnswersDto(
    var id: Long,
    var title: String?,
    var questionType: QuestionType,
    var imageId: Long?,
    var questionnaireId: Long,
    var answers: MutableList<AnswerDto> = mutableListOf()
) {
    constructor() : this(0, null, QuestionType.SINGLE, null, 0)
}
