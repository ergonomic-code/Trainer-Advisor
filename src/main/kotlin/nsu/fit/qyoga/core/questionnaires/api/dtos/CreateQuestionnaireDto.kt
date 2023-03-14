package nsu.fit.qyoga.core.questionnaires.api.dtos

data class CreateQuestionnaireDto(
    val id: Long,
    val title: String?,
    val questions: List<QuestionDto>
)