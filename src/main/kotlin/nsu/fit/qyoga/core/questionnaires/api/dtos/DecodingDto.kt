package nsu.fit.qyoga.core.questionnaires.api.dtos

data class DecodingDto(
    val id: Long = 0,
    val lowerBound: Int = 0,
    val upperBound: Int = 0,
    val result: String = "",
    val questionnaireId: Long = 0
)
