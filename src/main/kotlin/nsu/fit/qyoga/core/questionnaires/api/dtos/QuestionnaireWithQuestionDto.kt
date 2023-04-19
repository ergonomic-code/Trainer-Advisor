package nsu.fit.qyoga.core.questionnaires.api.dtos

data class QuestionnaireWithQuestionDto(
    val id: Long,
    val title: String,
    var questions: List<QuestionWithAnswersDto> = mutableListOf()
)
