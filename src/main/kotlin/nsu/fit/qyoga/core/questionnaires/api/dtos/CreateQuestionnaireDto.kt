package nsu.fit.qyoga.core.questionnaires.api.dtos

data class CreateQuestionnaireDto(
    val id: Long,
    val title: String,
    val question: MutableList<CreateQuestionDto> = mutableListOf(),
    val decoding: MutableList<DecodingDto> = mutableListOf()
) : java.io.Serializable
