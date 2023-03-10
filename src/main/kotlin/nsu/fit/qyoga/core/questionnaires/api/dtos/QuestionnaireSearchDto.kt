package nsu.fit.qyoga.core.questionnaires.api.dtos

data class QuestionnaireSearchDto(
    var title: String? = null,
    var orderType: String = "ASC"
)
