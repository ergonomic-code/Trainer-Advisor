package nsu.fit.qyoga.core.completingQuestionnaires.api.dtos

data class CompletingListDto(
    var questionnaireId: Long,
    var questionnaireTitle: String,
    var resultList: List<CompletingDto> = listOf()
)