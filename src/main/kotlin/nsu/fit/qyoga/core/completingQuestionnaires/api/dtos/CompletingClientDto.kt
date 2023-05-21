package nsu.fit.qyoga.core.completingQuestionnaires.api.dtos

data class CompletingClientDto(
    var id: Long = 0,
    var firstName: String = "",
    var lastName: String = "",
    var patronymic: String = ""
)