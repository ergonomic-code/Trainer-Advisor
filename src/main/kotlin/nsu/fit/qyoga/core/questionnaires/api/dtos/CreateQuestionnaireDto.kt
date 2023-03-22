package nsu.fit.qyoga.core.questionnaires.api.dtos

data class CreateQuestionnaireDto(
    val title: String? = null,
    val questions: List<CreateQuestionDto> = mutableListOf()
)