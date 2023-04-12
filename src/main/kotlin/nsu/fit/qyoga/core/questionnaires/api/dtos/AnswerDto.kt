package nsu.fit.qyoga.core.questionnaires.api.dtos

data class AnswerDto(
    var id: Long = 0,
    var title: String? = null,
    var lowerBound: Int? = null,
    var lowerBoundText: String? = null,
    var upperBound: Int? = null,
    var upperBoundText: String? = null,
    var score: Int? = null,
    var imageId: Long? = null,
    var questionId: Long = 0
)
