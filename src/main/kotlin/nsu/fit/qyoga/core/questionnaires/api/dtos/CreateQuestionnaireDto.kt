package nsu.fit.qyoga.core.questionnaires.api.dtos

data class CreateQuestionnaireDto(
    val title: String,
    val questions: List<CreateQuestionDto>
)