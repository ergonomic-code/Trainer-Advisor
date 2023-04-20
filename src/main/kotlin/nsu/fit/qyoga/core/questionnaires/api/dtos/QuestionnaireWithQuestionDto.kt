package nsu.fit.qyoga.core.questionnaires.api.dtos

data class QuestionnaireWithQuestionDto(
    var id: Long,
    var title: String,
    var questions: List<QuestionWithAnswersDto> = mutableListOf()
)
