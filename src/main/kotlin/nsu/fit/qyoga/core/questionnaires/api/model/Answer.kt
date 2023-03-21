package nsu.fit.qyoga.core.questionnaires.api.model

import org.springframework.data.annotation.Id

data class Answer(
    @Id
    val id: Long = 0,
    val title: String?,
    val lowerBound: Int?,
    val lowerBoundText: String?,
    val upperBound: Int?,
    val upperBoundText: String?,
    val score: Int?,
    val imageId: Long?,
    val questionId: Long
)