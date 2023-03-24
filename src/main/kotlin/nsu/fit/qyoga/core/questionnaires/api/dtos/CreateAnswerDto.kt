package nsu.fit.qyoga.core.questionnaires.api.dtos

data class CreateAnswerDto(
    val title: String? = null,
    var lowerBound: Int? = null,
    var lowerBoundText: String? = null,
    var upperBound: Int? = null,
    var upperBoundText: String? = null,
    var score: Int? = null,
    val photoId: Long? = null
)
