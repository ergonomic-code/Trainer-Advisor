package nsu.fit.qyoga.core.questionnaires.api.dtos

data class QuestionnaireWithQuestionDto(
    var title: String? = null,
    val questions: MutableList<QuestionWithAnswersDto> = mutableListOf()
)
