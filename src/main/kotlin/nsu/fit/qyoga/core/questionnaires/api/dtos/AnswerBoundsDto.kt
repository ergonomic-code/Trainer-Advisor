package nsu.fit.qyoga.core.questionnaires.api.dtos

data class AnswerBoundsDto(
    var lowerBound: Int? = null,
    var lowerBoundText: String? = null,
    var upperBound: Int? = null,
    var upperBoundText: String? = null
) {
    constructor() : this(null, null, null, null)
}
