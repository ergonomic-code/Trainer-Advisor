package nsu.fit.qyoga.core.questionnaires.api.dtos

data class AnswerDto(
    val title: String?,
    var lowerBound: Int?,
    val lowerBoundText: String?,
    val upperBound: Int?,
    val upperBoundText: String?,
    val score: Int?,
    val imageDto: ImageDto?
)