package nsu.fit.qyoga.core.questionnaires.api.dtos

data class CreateQuestionnaireDto(
    val id: Long = 0,
    val title: String = "",
    val question: MutableList<CreateQuestionDto> = mutableListOf(),
    val decoding: MutableList<DecodingDto> = mutableListOf()
)

fun CreateQuestionnaireDto.getQuestionIdxById(questionId: Long) =
    this.question.withIndex().first { questionId == it.value.id }.index

fun CreateQuestionnaireDto.getQuestionByIdOrNull(questionId: Long): CreateQuestionDto? {
    return this.question.filter { it.id == questionId }.getOrNull(0)
}
