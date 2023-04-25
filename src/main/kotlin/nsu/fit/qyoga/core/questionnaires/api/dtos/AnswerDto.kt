package nsu.fit.qyoga.core.questionnaires.api.dtos

class AnswerDto(
    var id: Long,
    var title: String?,
    var bounds: AnswerBoundsDto,
    var score: Int?,
    var imageId: Long?,
    var questionId: Long
) {
    constructor() : this(0, null, AnswerBoundsDto(), null, null, 0)
}
