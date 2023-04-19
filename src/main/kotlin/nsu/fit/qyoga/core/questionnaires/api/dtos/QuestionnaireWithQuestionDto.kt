package nsu.fit.qyoga.core.questionnaires.api.dtos

class QuestionnaireWithQuestionDto(
    val id: Long,
    val title: String,
    val questions: MutableList<QuestionWithAnswersDto>
)
