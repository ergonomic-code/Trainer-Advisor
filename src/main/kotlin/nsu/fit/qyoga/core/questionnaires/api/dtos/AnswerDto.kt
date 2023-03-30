package nsu.fit.qyoga.core.questionnaires.api.dtos

data class AnswerDto(
    val id: Long,
    val title: String?,
    val lowerBound: Int?,
    val lowerBoundText: String?,
    val upperBound: Int?,
    val upperBoundText: String?,
    val score: Int?,
    val imageId: Long?,
    val questionId: Long
)