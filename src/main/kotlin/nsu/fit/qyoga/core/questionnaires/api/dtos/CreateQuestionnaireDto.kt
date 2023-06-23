package nsu.fit.qyoga.core.questionnaires.api.dtos

data class CreateQuestionnaireDto(
    val id: Long = 0,
    val title: String = "",
    val question: MutableList<CreateQuestionDto> = mutableListOf(),
    val decoding: MutableList<DecodingDto> = mutableListOf()
)

fun CreateQuestionnaireDto.getQuestionIdxById(questionId: Long) =
    this.question.withIndex().first { questionId == it.value.id }.index

fun CreateQuestionnaireDto.getQuestionByIdOrNull(questionId: Long): CreateQuestionDto? =
    this.question.filter { it.id == questionId }.getOrNull(0)

fun CreateQuestionnaireDto.getAnswerByIdOrNull(questionId: Long, answerId: Long): CreateAnswerDto? {
    val question = this.getQuestionByIdOrNull(questionId)
        ?: return null
    return question.answers.filter { it.id == answerId }.getOrNull(0)
}
