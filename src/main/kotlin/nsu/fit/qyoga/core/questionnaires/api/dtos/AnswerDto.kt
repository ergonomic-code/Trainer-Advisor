package nsu.fit.qyoga.core.questionnaires.api.dtos

data class AnswerDto(
    val id: Long = 0,
    val title: String? = null,
    val lowerBound: Int? = null,
    val lowerBoundText: String? = null,
    val upperBound: Int? = null,
    val upperBoundText: String? = null,
    val score: Int? = null,
    val imageId: Long? = null,
    val questionId: Long = 0
)