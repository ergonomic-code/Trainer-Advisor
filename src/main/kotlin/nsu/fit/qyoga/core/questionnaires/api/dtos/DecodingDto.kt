package nsu.fit.qyoga.core.questionnaires.api.dtos

data class DecodingDto (
    var id: Long = 0,
    var lowerBound: Int = 0,
    var upperBound: Int = 0,
    var result: String = "",
    var questionnaireId: Long = 0
)