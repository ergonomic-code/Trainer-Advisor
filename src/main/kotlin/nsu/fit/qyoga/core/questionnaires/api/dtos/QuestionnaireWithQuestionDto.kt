package nsu.fit.qyoga.core.questionnaires.api.dtos

data class QuestionnaireWithQuestionDto(
    val title: String,
    val questions: List<QuestionWithAnswersDto>
)