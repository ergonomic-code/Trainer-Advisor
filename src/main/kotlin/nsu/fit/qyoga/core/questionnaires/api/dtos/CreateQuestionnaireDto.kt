package nsu.fit.qyoga.core.questionnaires.api.dtos

data class CreateQuestionnaireDto(
    val id: Long = 0,
    val title: String = "",
    val questions: MutableList<CreateQuestionDto> = mutableListOf()
)