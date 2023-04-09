package nsu.fit.qyoga.core.questionnaires.api.dtos

data class DecodingDto (
    val id: Long = 0,
    val lowerBound: Int?,
    val upperBound: Int?,
    val result: String,
    val questionnaireId: Long
)