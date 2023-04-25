package nsu.fit.qyoga.core.questionnaires.api.dtos

class DecodingDto(
    var id: Long,
    var lowerBound: Int,
    var upperBound: Int,
    var result: String,
    var questionnaireId: Long
) {
    constructor() : this(0, 0, 0, "", 0)
}
