package nsu.fit.qyoga.core.questionnaires.api.dtos

data class CreateAnswerDto(
    val title: String?,
    var lowerBound: Int?,
    var lowerBoundText: String?,
    var upperBound: Int?,
    var upperBoundText: String?,
    var score: Int?,
    val photoId: Long?
)
