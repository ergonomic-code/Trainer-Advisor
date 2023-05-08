package nsu.fit.qyoga.core.questionnaires.api.dtos

data class CreateAnswerDto(
    var id: Long = 0,
    var title: String? = null,
    var bounds: AnswerBoundsDto = AnswerBoundsDto(),
    var score: Int? = null,
    var imageId: Long? = null
) : java.io.Serializable
