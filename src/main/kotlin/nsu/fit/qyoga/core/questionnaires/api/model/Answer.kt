package nsu.fit.qyoga.core.questionnaires.api.model

import nsu.fit.qyoga.core.questionnaires.api.enums.QuestionType
import org.springframework.data.annotation.Id

data class Answer(
    @Id
    val id: Long = 0,
    val title: String?,
    var lowerBound: Int?,
    var lowerBoundText: String?,
    var upperBound: Int?,
    var upperBoundText: String?,
    var score: Int?,
    val imageId: Long?,
    val questionId: Long
)